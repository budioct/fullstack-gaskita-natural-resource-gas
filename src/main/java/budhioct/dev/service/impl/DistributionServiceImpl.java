package budhioct.dev.service.impl;

import budhioct.dev.entity.Distribution;
import budhioct.dev.entity.LogStock;
import budhioct.dev.entity.Stock;
import budhioct.dev.repository.DistributionRepository;
import budhioct.dev.repository.LogStockRepository;
import budhioct.dev.repository.StockRepository;
import budhioct.dev.service.DistributionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DistributionServiceImpl implements DistributionService {

    private final DistributionRepository distributionRepository;
    private final StockRepository stockRepository;
    private final LogStockRepository logStockRepository;

    @Transactional
    public void distribute(Long senderStockId, Long recipientStockId, Long amount) {
        Stock senderStock = stockRepository.findById(senderStockId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sender stock not found"));

        Stock recipientStock = stockRepository.findById(recipientStockId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipient stock not found"));

        if (senderStock.getStock_amount() < amount) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient stock to distribute");
        }

        senderStock.setStock_amount(senderStock.getStock_amount() - amount);
        stockRepository.save(senderStock);

        recipientStock.setStock_amount(recipientStock.getStock_amount() + amount);
        stockRepository.save(recipientStock);

        logDistribution(senderStock, recipientStock, amount);

        logStockChange(senderStock, -amount, "STOCK OUT");
        logStockChange(recipientStock, amount, "STOCK IN");
    }

    private void logDistribution(Stock senderStock, Stock recipientStock,Long amount) {
        Distribution distribution = new Distribution();
        distribution.setSender(senderStock);
        distribution.setRecipient(recipientStock);
        distribution.setAmountGas(amount);
        distribution.setDate(LocalDateTime.now());
        distributionRepository.save(distribution);
    }

    private void logStockChange(Stock stock, Long changeAmount, String status) {
        LogStock logStock = new LogStock();
        logStock.setStock(stock);
        logStock.setNumberOfChanges(changeAmount);
        logStock.setStatus(status);
        logStock.setDate(LocalDateTime.now());
        logStockRepository.save(logStock);
    }
}
