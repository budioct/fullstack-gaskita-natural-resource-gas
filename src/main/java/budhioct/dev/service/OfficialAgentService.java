package budhioct.dev.service;

import budhioct.dev.dto.OfficialAgentDTO;

import java.util.List;

public interface OfficialAgentService {

    List<OfficialAgentDTO.OfficialAgentResponse> listOfficialAgent();
    OfficialAgentDTO.OfficialAgentDetailResponse detailOfficialAgent(Long id);
}
