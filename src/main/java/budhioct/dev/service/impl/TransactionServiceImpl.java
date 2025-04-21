package budhioct.dev.service.impl;

import budhioct.dev.dto.TransactionDTO;
import budhioct.dev.entity.*;
import budhioct.dev.repository.*;
import budhioct.dev.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final SubAgentRepository subAgentRepository;
    private final FolksyRepository folksyRepository;
    private final TransactionRepository transactionRepository;
    private final StockRepository stockRepository;
    private final LogStockRepository logStockRepository;

    @Transactional
    public void createTransaction(Long subAgentId, Long folksyId, Long amountGas, Double pricePerUnit) {
        SubAgent subAgent = subAgentRepository.findById(subAgentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "sub agent not found"));
        Stock subAgentStock = subAgent.getStock();


        if (subAgentStock.getStock_amount() < amountGas){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient stock for sub agent");
        }

        Folksy folksy = folksyRepository.findById(folksyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "folksy not found"));

        subAgentStock.setStock_amount(subAgentStock.getStock_amount() - amountGas);
        stockRepository.save(subAgentStock);

        logStockChange(subAgentStock, amountGas, "SOLD");

        logTransaction(subAgent, folksy, amountGas, pricePerUnit);

    }

    @Transactional(readOnly = true)
    public List<TransactionDTO.TransactionResponse> listTransaction() {

        List<TransactionDTO.TransactionResponse> list = transactionRepository.findAll()
                .stream()
                .map(TransactionDTO::toTransactionResponse)
                .toList();

        return list;
    }

    private void logStockChange(Stock stock, Long changeAmount, String status) {
        LogStock logStock = new LogStock();
        logStock.setStock(stock);
        logStock.setNumberOfChanges(changeAmount);
        logStock.setStatus(status);
        logStock.setDate(LocalDateTime.now());
        logStockRepository.save(logStock);
    }

    private void logTransaction(SubAgent subAgent, Folksy folksy,  Long amountGas, Double pricePerUnit){
        Transaction transaction = new Transaction();
        transaction.setSubAgent(subAgent);
        transaction.setFolksy(folksy);
        transaction.setDate(LocalDateTime.now());
        transaction.setAmountGas(amountGas);
        transaction.setTotalPrice(amountGas * pricePerUnit);
        transactionRepository.save(transaction);
    }

}
