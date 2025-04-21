package budhioct.dev.service;

import budhioct.dev.dto.TransactionDTO;

import java.util.List;

public interface TransactionService {
    void createTransaction(Long subAgentId, Long folksyId, Long amountGas, Double pricePerUnit);
    List<TransactionDTO.TransactionResponse> listTransaction();
}
