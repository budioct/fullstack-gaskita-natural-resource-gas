package budhioct.dev.service;

import budhioct.dev.dto.StockDTO;

public interface StockService {

    StockDTO.StockResponse monitorStockLevels(Long agentId);
    void recordSale(Long subAgentId, Long folksyId, double amountGas);
}
