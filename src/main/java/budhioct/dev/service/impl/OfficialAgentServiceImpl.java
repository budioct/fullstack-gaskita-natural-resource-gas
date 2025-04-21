package budhioct.dev.service.impl;

import budhioct.dev.dto.OfficialAgentDTO;
import budhioct.dev.entity.OfficialAgent;
import budhioct.dev.repository.OfficialAgentRepository;
import budhioct.dev.service.OfficialAgentService;
import budhioct.dev.utilities.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OfficialAgentServiceImpl implements OfficialAgentService {

    private final OfficialAgentRepository officialAgentRepository;
    private final ValidationService validation;

    @Transactional(readOnly = true)
    public List<OfficialAgentDTO.OfficialAgentResponse> listOfficialAgent() {

        List<OfficialAgentDTO.OfficialAgentResponse> list = officialAgentRepository.findAll()
                .stream()
                .map(OfficialAgentDTO::toOfficialAgentResponse)
                .toList();

        if (list.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "official agent not found");
        }

        return list;
    }

    @Transactional(readOnly = true)
    public OfficialAgentDTO.OfficialAgentDetailResponse detailOfficialAgent(Long id) {
        validation.validate(id);
        OfficialAgent officialAgent = officialAgentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "official agent not found"));
        return OfficialAgentDTO.toOfficialAgentDetailResponse(officialAgent);
    }

}
