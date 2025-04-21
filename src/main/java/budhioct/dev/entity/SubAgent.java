package budhioct.dev.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners({AuditingEntityListener.class})
@Entity
@Table(name = "sub_agent")
public class SubAgent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "sub_agent_name")
    private String subAgentName;
    @Column(name = "address")
    private String address;
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    @LastModifiedDate
    @Column(name = "updated_at", insertable = false)
    private LocalDateTime updatedAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "official_agent_id", referencedColumnName = "id")
    private OfficialAgent officialAgent;
    @OneToMany(mappedBy = "subAgent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "stock_id", referencedColumnName = "id")
    private Stock stock;

    public SubAgent(String subAgentName, String address, OfficialAgent officialAgent, Stock stock) {
        this.subAgentName = subAgentName;
        this.address = address;
        this.officialAgent = officialAgent;
        this.stock = stock;
    }

}
