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
@Table(name = "official_agent")
public class OfficialAgent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "agent_name")
    private String agentName;
    @Column(name = "address")
    private String address;
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    @LastModifiedDate
    @Column(name = "updated_at", insertable = false)
    private LocalDateTime updatedAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stakeholder_id", referencedColumnName = "id")
    private Stakeholder stakeholder;
    @OneToMany(mappedBy = "officialAgent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubAgent> subAgents;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "stock_id", referencedColumnName = "id")
    private Stock stock;

    public OfficialAgent(String agentName, String address, Stakeholder stakeholder, Stock stock) {
        this.agentName = agentName;
        this.address = address;
        this.stakeholder = stakeholder;
        this.stock = stock;
    }

}
