package budhioct.dev.dto;

import budhioct.dev.utilities.Ownership;

import java.time.LocalDateTime;

public interface SubAgentTransactionProjection {
    Long getSubAgentId();
    String getSubAgentName();
    String getAddress();
    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();

    Long getStockid();
    Ownership getStockOwnership();
    Long getStockStock_amount();

    Long getOfficialAgentId();
    String getOfficialAgentAgentName();
    String getOfficialAgentAddress();
    Long getOfficialAgentStock_owner_id();
    Long getOfficialAgentStock_amount_gas();
    String getOfficialAgentSubholdingGroupAffiliate();

    Long getTransactionId();
    Long getAmountGas();
    Double getTotalPrice();
    LocalDateTime getTransactionDate();
}
