package budhioct.dev.dto;

import budhioct.dev.entity.LogStock;
import budhioct.dev.entity.Stock;
import budhioct.dev.utilities.Ownership;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

public class StockDTO {

    @Setter
    @Getter
    @Builder
    public static class StockResponse {
        private Long id;
        private Ownership ownership;
        private Long stock_amount;
        private List<LogStockResponse> logStocks;
    }

    @Setter
    @Getter
    @Builder
    public static class LogStockResponse {
        private Long id;
        private Long numberOfChanges;
        private String status;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime date;
    }

    public static StockResponse toStockResponse(Stock stock) {
        return StockResponse.builder()
                .id(stock.getId())
                .ownership(stock.getOwnership())
                .stock_amount(stock.getStock_amount())
                .logStocks(stock.getLogStocks().stream().map(StockDTO::toLogStockResponse).toList())
                .build();
    }

    public static LogStockResponse toLogStockResponse(LogStock logStock) {
        return LogStockResponse.builder()
                .id(logStock.getId())
                .numberOfChanges(logStock.getNumberOfChanges())
                .status(logStock.getStatus())
                .date(logStock.getDate())
                .build();
    }

}
