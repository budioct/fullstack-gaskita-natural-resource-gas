package budhioct.dev.service.impl;

import budhioct.dev.dto.FolksyDTO;
import budhioct.dev.entity.Folksy;
import budhioct.dev.repository.FolksyRepository;
import budhioct.dev.service.FolksyService;
import budhioct.dev.utilities.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class FolksyServiceImpl implements FolksyService {

    private final FolksyRepository folksyRepository;
    private final ValidationService validation;

    @Transactional(readOnly = true)
    public FolksyDTO.FolksyResponse detailFolksy(Long id) {
        validation.validate(id);
        Folksy folksy = folksyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "folksy not found"));
        return FolksyDTO.toFolksyResponse(folksy);
    }

}
