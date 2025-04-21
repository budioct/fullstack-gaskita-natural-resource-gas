package budhioct.dev.service.impl;

import budhioct.dev.dto.StockDTO;
import budhioct.dev.entity.Stock;
import budhioct.dev.repository.*;
import budhioct.dev.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    private final SubAgentRepository subAgentRepository;
    private final StockRepository stockRepository;
    private final TransactionRepository transactionRepository;
    private final FolksyRepository folksyRepository;

    @Transactional(readOnly = true)
    public StockDTO.StockResponse monitorStockLevels(Long agentId) {
        Stock stock = stockRepository.findById(agentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "stock not found"));

        if (stock.getStock_amount() < 10) {  // Threshold example
            System.out.println("Warning: Stock is running low!");
        }

        return StockDTO.toStockResponse(stock);

    }

    @Transactional
    public void recordSale(Long subAgentId, Long folksyId, double amountGas) {
//        SubAgent subAgent = subAgentRepository.findById(subAgentId)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "sub agent not found"));
//        Folksy folksy = folksyRepository.findById(folksyId)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "folksy not found"));
//
//        if (subAgent.getStock().getStock_amount() < amountGas) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient stock for sale");
//        }
//
//        subAgent.setGasStock(subAgent.getGasStock() - amountGas);
//        subAgent.setStock(subAgent.getStock() - amountGas);
//        Transaction transaction = new Transaction();
//        transaction.setSubAgent(subAgent);
//        transaction.setFolksy(folksy);
//        transaction.setAmountGas((long) amountGas);
//        transaction.setDate(LocalDateTime.now());
//        transaction.setTotalPrice(amountGas * 15000);  // Example price
//
//        transactionRepository.save(transaction);
    }

}
