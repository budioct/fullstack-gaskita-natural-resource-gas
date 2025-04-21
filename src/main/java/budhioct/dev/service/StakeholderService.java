package budhioct.dev.service;

import budhioct.dev.dto.StakeholderDTO;

import java.util.List;

public interface StakeholderService {

    List<StakeholderDTO.StakeholderResponse> listStakeholder();
    StakeholderDTO.StakeholderDetailResponse detailStakeholder(Long id);
    StakeholderDTO.StakeholderDetailResponse reProductionStock(Long id);

}
