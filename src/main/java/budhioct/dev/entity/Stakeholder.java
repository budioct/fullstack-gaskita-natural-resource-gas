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
@Table(name = "stakeholder")
public class Stakeholder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "subholding_group_affiliate")
    private String subholdingGroupAffiliate;
    @Column(name = "address")
    private String address;
    @Column(name = "contact")
    private String contact;
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    @LastModifiedDate
    @Column(name = "updated_at", insertable = false)
    private LocalDateTime updatedAt;
    @OneToMany(mappedBy = "stakeholder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OfficialAgent> officialAgents;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "stock_id", referencedColumnName = "id")
    private Stock stock;

    public Stakeholder(String subholdingGroupAffiliate, String address, String contact, List<OfficialAgent> officialAgents, Stock stock) {
        this.subholdingGroupAffiliate = subholdingGroupAffiliate;
        this.address = address;
        this.contact = contact;
        this.officialAgents = officialAgents;
        this.stock = stock;
    }

}
