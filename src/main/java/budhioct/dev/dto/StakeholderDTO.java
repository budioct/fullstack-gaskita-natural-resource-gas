package budhioct.dev.dto;

import budhioct.dev.entity.OfficialAgent;
import budhioct.dev.entity.Stakeholder;
import budhioct.dev.entity.Stock;
import budhioct.dev.utilities.Ownership;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

public class StakeholderDTO {

    @Getter
    @Setter
    @Builder
    public static class StakeholderResponse {
        private Long id;
        private String subholdingGroupAffiliate;
        private String address;
        private String contact;
        private Long stock_amount_gas;
        private List<String> officialAgentName;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime createdAt;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime updatedAt;
    }

    @Getter
    @Setter
    @Builder
    public static class StakeholderDetailResponse {
        private Long id;
        private String subholdingGroupAffiliate;
        private String address;
        private String contact;
        private StockResponse stock;
        private List<OfficialAgentResponse> officialAgents;
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
    }

    @Getter
    @Setter
    @Builder
    public static class StockResponse {
        private Long id;
        private Ownership ownership;
        private Long stock_amount;
    }

    public static StakeholderResponse toStakeholderResponse(Stakeholder stakeholder) {
        return StakeholderResponse.builder()
                .id(stakeholder.getId())
                .subholdingGroupAffiliate(stakeholder.getSubholdingGroupAffiliate())
                .address(stakeholder.getAddress())
                .contact(stakeholder.getContact())
                .createdAt(stakeholder.getCreatedAt())
                .updatedAt(stakeholder.getUpdatedAt())
                .officialAgentName(stakeholder.getOfficialAgents().stream().map(OfficialAgent::getAgentName).toList())
                .stock_amount_gas(stakeholder.getStock().getStock_amount())
                .build();
    }

    public static StakeholderDetailResponse toStakeholderDetailResponse(Stakeholder stakeholder) {
        return StakeholderDetailResponse.builder()
                .id(stakeholder.getId())
                .subholdingGroupAffiliate(stakeholder.getSubholdingGroupAffiliate())
                .address(stakeholder.getAddress())
                .contact(stakeholder.getContact())
                .officialAgents(stakeholder.getOfficialAgents().stream().map(StakeholderDTO::toOfficialAgentResponse).toList())
                .stock(toStockResponse(stakeholder.getStock()))
                .createdAt(stakeholder.getCreatedAt())
                .updatedAt(stakeholder.getUpdatedAt())
                .build();
    }

    public static OfficialAgentResponse toOfficialAgentResponse(OfficialAgent officialAgent){
        return OfficialAgentResponse.builder()
                .id(officialAgent.getId())
                .agentName(officialAgent.getAgentName())
                .stock_owner_id(officialAgent.getStock().getId())
                .stock_amount_gas(officialAgent.getStock().getStock_amount())
                .address(officialAgent.getAddress())
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
