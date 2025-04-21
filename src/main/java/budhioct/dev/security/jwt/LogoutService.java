package budhioct.dev.security.jwt;

import budhioct.dev.security.module.TokenRepository;
import budhioct.dev.security.module.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogoutService implements LogoutHandler {

    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        try {
            final String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                log.warn("Logout failed: Authorization header is missing or invalid");
                SecurityContextHolder.clearContext();
                throw new AccessDeniedException("Forbidden: Invalid Authorization header");
            }

            final String jwt_token = authHeader.substring(7);
            tokenRepository.findByToken(jwt_token).ifPresentOrElse(token -> {
                if (token.isExpired() || token.isRevoked()) {
                    log.warn("Logout failed: Token is already expired or revoked");
                    SecurityContextHolder.clearContext();
                    throw new AccessDeniedException("Forbidden: Token is expired or revoked");
                }

                String user_email = jwtService.extractUsername(jwt_token);
                userRepository.findFirstByEmail(user_email).orElseThrow(() -> {
                    log.warn("Logout failed: User not found for token");
                    return new AccessDeniedException("Forbidden: User not found");
                });

                token.setExpired(true);
                token.setRevoked(true);
                tokenRepository.save(token);
                log.info("Logout successful for user: {}", user_email);

                SecurityContextHolder.clearContext();

            }, () -> {
                log.warn("Logout failed: Token not found in database");
                SecurityContextHolder.clearContext();
                throw new AccessDeniedException("Forbidden: Token not found");
            });

        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

}

