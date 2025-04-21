package budhioct.dev.dto;

import budhioct.dev.entity.LogStock;
import budhioct.dev.entity.Transaction;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class TransactionDTO {

    @Setter
    @Getter
    @Builder
    public static class TransactionResponse {
        private Long id;
        private Long amountGas;
        private Double totalPrice;
        private String transactionStatus; // dari LogStock.status
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime transactionDate;
    }

    public static TransactionResponse toTransactionResponse(Transaction transaction) {
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
