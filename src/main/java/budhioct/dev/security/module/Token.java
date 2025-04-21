package budhioct.dev.security.module;

import budhioct.dev.security.token.TokenType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "tokens")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;
    @Column(name = "token",columnDefinition = "text", nullable = false)
    private String token;
    @Enumerated(EnumType.STRING)
    public TokenType tokenType = TokenType.BEARER;
    @Column(name = "revoked")
    private boolean revoked;
    @Column(name = "expired")
    private boolean expired;
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createAt;
    @LastModifiedDate
    @Column(name = "updated_at", insertable = false)
    private LocalDateTime updateAt;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
