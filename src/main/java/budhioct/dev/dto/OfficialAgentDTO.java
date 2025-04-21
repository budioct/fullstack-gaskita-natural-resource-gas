package budhioct.dev.dto;

import budhioct.dev.entity.OfficialAgent;
import budhioct.dev.entity.Stakeholder;
import budhioct.dev.entity.Stock;
import budhioct.dev.entity.SubAgent;
import budhioct.dev.utilities.Ownership;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

public class OfficialAgentDTO {

    @Getter
    @Setter
    @Builder
    public static class OfficialAgentResponse {
        private Long id;
        private String agentName;
        private String address;
        private Long stock_amount_gas;
        private String subholdingGroupAffiliate;
        private List<String> subAgentName;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime createdAt;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime updatedAt;
    }

    @Getter
    @Setter
    @Builder
    public static class OfficialAgentDetailResponse {
        private Long id;
        private String agentName;
        private String address;
        private StockResponse stock;
        private StakeholderResponse stakeholder;
        private List<SubAgentResponse> subAgentName;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime createdAt;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime updatedAt;
    }

    @Getter
    @Setter
    @Builder
    public static class StakeholderResponse {
        private Long id;
        private String subholdingGroupAffiliate;
        private String address;
        private String contact;
        private Long stock_owner_id;
        private Long stock_amount_gas;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime createdAt;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime updatedAt;
    }

    @Getter
    @Setter
    @Builder
    public static class SubAgentResponse {
        private Long id;
        private String subAgentName;
        private String address;
        private Long stock_owner_id;
        private Long stock_amount_gas;
    }

    @Getter
    @Setter
    @Builder
    public static class StockResponse {
        private Long id;
        private Ownership ownership;
        private Long stock_amount;
    }

    public static OfficialAgentResponse toOfficialAgentResponse(OfficialAgent officialAgent) {
        return OfficialAgentResponse.builder()
                .id(officialAgent.getId())
                .agentName(officialAgent.getAgentName())
                .address(officialAgent.getAddress())
                .stock_amount_gas(officialAgent.getStock().getStock_amount())
                .subholdingGroupAffiliate(officialAgent.getStakeholder().getSubholdingGroupAffiliate())
                .subAgentName(officialAgent.getSubAgents().stream().map(SubAgent::getSubAgentName).toList())
                .createdAt(officialAgent.getCreatedAt())
                .updatedAt(officialAgent.getUpdatedAt())
                .build();
    }

    public static OfficialAgentDetailResponse toOfficialAgentDetailResponse(OfficialAgent officialAgent) {
        return OfficialAgentDetailResponse.builder()
                .id(officialAgent.getId())
                .agentName(officialAgent.getAgentName())
                .address(officialAgent.getAddress())
                .stock(toStockResponse(officialAgent.getStock()))
                .stakeholder(toStakeholderResponse(officialAgent.getStakeholder()))
                .subAgentName(officialAgent.getSubAgents().stream().map(OfficialAgentDTO::toSubAgentResponse).toList())
                .createdAt(officialAgent.getCreatedAt())
                .updatedAt(officialAgent.getUpdatedAt())
                .build();
    }

    public static StakeholderResponse toStakeholderResponse(Stakeholder stakeholder) {
        return StakeholderResponse.builder()
                .id(stakeholder.getId())
                .subholdingGroupAffiliate(stakeholder.getSubholdingGroupAffiliate())
                .address(stakeholder.getAddress())
                .contact(stakeholder.getContact())
                .stock_owner_id(stakeholder.getStock().getId())
                .stock_amount_gas(stakeholder.getStock().getStock_amount())
                .createdAt(stakeholder.getCreatedAt())
                .updatedAt(stakeholder.getUpdatedAt())
                .build();
    }

    public static SubAgentResponse toSubAgentResponse(SubAgent subAgent) {
        return SubAgentResponse.builder()
                .id(subAgent.getId())
                .subAgentName(subAgent.getSubAgentName())
                .address(subAgent.getAddress())
                .stock_owner_id(subAgent.getStock().getId())
                .stock_amount_gas(subAgent.getStock().getStock_amount())
                .build();
    }

    public static StockResponse toStockResponse(Stock stock) {
        return StockResponse.builder()
                .id(stock.getId())
                .ownership(stock.getOwnership())
                .stock_amount(stock.getStock_amount())
                .build();
    }

}
