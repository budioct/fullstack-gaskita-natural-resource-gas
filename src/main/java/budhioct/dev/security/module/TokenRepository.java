package budhioct.dev.security.module;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByToken(String token);

    @Query(value = """
            select t from Token t inner join User u
            on t.user.id = u.id
            where u.id = :user_id and (t.expired = false or t.revoked = false)
            """)
    List<Token> findAllValidTokenByUser(@Param(value = "user_id" ) Long user_id);

}
