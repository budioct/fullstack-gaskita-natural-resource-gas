package budhioct.dev.repository;

import budhioct.dev.dto.SubAgentTransactionProjection;
import budhioct.dev.entity.SubAgent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SubAgentRepository extends JpaRepository<SubAgent, Long> {

    @Query("""
             SELECT s FROM SubAgent s 
             LEFT JOIN FETCH s.transactions 
             WHERE s.id = :id
            """)
    Optional<SubAgent> findSubAgentWithTransactions(@Param("id") Long id);

    @Query(value = """
        SELECT sa.id AS subAgentId, sa.sub_agent_name AS subAgentName, sa.address, 
               sa.created_at AS createdAt, sa.updated_at AS updatedAt,
               t.id AS transactionId, t.amount_gas AS amountGas, 
               t.total_price AS totalPrice, t.date AS transactionDate, 
               t.created_at AS transactionCreatedAt, t.updated_at AS transactionUpdatedAt
        FROM sub_agent sa
        LEFT JOIN transaction t ON sa.id = t.sub_agent_id
        WHERE sa.id = :id
        """, nativeQuery = true)
    List<SubAgentTransactionProjection> findSubAgentWithTransactionsRaw(@Param("id") Long id);

}
