package budhioct.dev.service.impl;

import budhioct.dev.dto.StakeholderDTO;
import budhioct.dev.entity.LogStock;
import budhioct.dev.entity.Stakeholder;
import budhioct.dev.entity.Stock;
import budhioct.dev.repository.LogStockRepository;
import budhioct.dev.repository.StakeholderRepository;
import budhioct.dev.service.StakeholderService;
import budhioct.dev.utilities.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StakeholderServiceImpl implements StakeholderService {

    private final StakeholderRepository stakeholderRepository;
    private final LogStockRepository logStockRepository;
    private final ValidationService validation;

    @Transactional(readOnly = true)
    public List<StakeholderDTO.StakeholderResponse> listStakeholder() {
        List<StakeholderDTO.StakeholderResponse> list = stakeholderRepository.findAll()
                .stream()
                .map(StakeholderDTO::toStakeholderResponse)
                .toList();

        if (list.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "stakeholder not found");
        }

        return list;
    }

    @Transactional(readOnly = true)
    public StakeholderDTO.StakeholderDetailResponse detailStakeholder(Long id) {
        validation.validate(id);
        Stakeholder stakeholder = stakeholderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "stakeholder not found"));
        return StakeholderDTO.toStakeholderDetailResponse(stakeholder);
    }

    @Transactional
    public StakeholderDTO.StakeholderDetailResponse reProductionStock(Long id) {
        validation.validate(id);
        Stakeholder stakeholder = stakeholderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "stakeholder not found"));

        if (stakeholder.getStock().getStock_amount() > 10000L) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "stakeholder stock must be less than or equal to 10000");
        }

        Stock stock = stakeholder.getStock();
        Long reproduceAmount = 30000L;
        stock.setStock_amount(stock.getStock_amount() + reproduceAmount);

        logStockChange(stock, reproduceAmount, "RE-PRODUCTION");

        return StakeholderDTO.toStakeholderDetailResponse(stakeholder);
    }

    private void logStockChange(Stock stock, Long amount, String status) {
        LogStock logStock = new LogStock();
        logStock.setStock(stock);
        logStock.setNumberOfChanges(amount);
        logStock.setStatus(status);
        logStock.setDate(LocalDateTime.now());
        logStockRepository.save(logStock);
    }

}
