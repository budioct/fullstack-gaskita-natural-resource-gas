package budhioct.dev.service;

public interface DistributionService {
    void distribute(Long senderStockId, Long recipientStockId, Long amount);
}
