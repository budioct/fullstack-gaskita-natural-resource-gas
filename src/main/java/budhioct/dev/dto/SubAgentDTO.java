package budhioct.dev.dto;

import budhioct.dev.entity.*;
import budhioct.dev.utilities.Ownership;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

public class SubAgentDTO {

    @Getter
    @Setter
    @Builder
    public static class SubAgentResponse {
        private Long id;
        private String subAgentName;
        private String address;
        private Long stock_amount_gas;
        private String officialAgent;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime createdAt;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime updatedAt;
    }

    @Getter
    @Setter
    @Builder
    public static class SubAgentDetailResponse {
        private Long id;
        private String subAgentName;
        private String address;
        private StockResponse stock;
        private OfficialAgentResponse officialAgent;
        private List<TransactionResponse> transaction;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime createdAt;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime updatedAt;
    }

    @Getter
    @Setter
    @Builder
    public static class OfficialAgentResponse {
        private Long id;
        private String agentName;
        private String address;
        private Long stock_owner_id;
        private Long stock_amount_gas;
        private String subholdingGroupAffiliate;
    }

    @Getter
    @Setter
    @Builder
    public static class StockResponse {
        private Long id;
        private Ownership ownership;
        private Long stock_amount;
    }

    @Getter
    @Setter
    @Builder
    public static class TransactionResponse {
        private Long id;
        private Long amountGas;
        private Double totalPrice;
        private String transactionStatus;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime transactionDate;
    }

    public static SubAgentResponse toSubAgentResponse(SubAgent subAgent) {
        return SubAgentResponse.builder()
                .id(subAgent.getId())
                .subAgentName(subAgent.getSubAgentName())
                .address(subAgent.getAddress())
                .stock_amount_gas(subAgent.getStock().getStock_amount())
                .officialAgent(subAgent.getOfficialAgent().getAgentName())
                .createdAt(subAgent.getCreatedAt())
                .updatedAt(subAgent.getUpdatedAt())
                .build();
    }

    public static SubAgentDetailResponse toSubAgentDetailResponse(SubAgent subAgent) {
        return SubAgentDetailResponse.builder()
                .id(subAgent.getId())
                .subAgentName(subAgent.getSubAgentName())
                .address(subAgent.getAddress())
                .stock(toStockResponse(subAgent.getStock()))
                .officialAgent(toOfficialAgentResponse(subAgent.getOfficialAgent()))
                .transaction(subAgent.getTransactions().stream().map(SubAgentDTO::toTransactionResponse).toList())
                .createdAt(subAgent.getCreatedAt())
                .updatedAt(subAgent.getUpdatedAt())
                .build();
    }

    public static OfficialAgentResponse toOfficialAgentResponse(OfficialAgent officialAgent) {
        return OfficialAgentResponse.builder()
                .id(officialAgent.getId())
                .agentName(officialAgent.getAgentName())
                .address(officialAgent.getAddress())
                .stock_owner_id(officialAgent.getStock().getId())
                .stock_amount_gas(officialAgent.getStock().getStock_amount())
                .subholdingGroupAffiliate(officialAgent.getStakeholder().getSubholdingGroupAffiliate())
                .build();
    }

    public static StockResponse toStockResponse(Stock stock) {
        return StockResponse.builder()
                .id(stock.getId())
                .ownership(stock.getOwnership())
                .stock_amount(stock.getStock_amount())
                .build();
    }

    public static TransactionResponse toTransactionResponse(Transaction transaction){
        LogStock logStock = transaction.getSubAgent().getStock().getLogStocks()
                .stream()
                .filter(log -> log.getDate().isBefore(transaction.getDate()) || log.getDate().isEqual(transaction.getDate()))
                .reduce((first, second) -> second)
                .orElse(null);
        return TransactionResponse.builder()
                .id(transaction.getId())
                .amountGas(transaction.getAmountGas())
                .totalPrice(transaction.getTotalPrice())
                .transactionStatus(logStock != null ? logStock.getStatus() : "UNKNOWN")
                .transactionDate(transaction.getDate())
                .build();
    }
}
