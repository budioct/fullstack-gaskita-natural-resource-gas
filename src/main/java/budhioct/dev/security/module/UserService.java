package budhioct.dev.security.module;

public interface UserService {
    UserDTO.RegisterResponse register(UserDTO.RegisterRequest request);
    UserDTO.LoginResponse login(UserDTO.LoginRequest request);
}
