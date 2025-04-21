package budhioct.dev.rest.controller;

import budhioct.dev.dto.StakeholderDTO;
import budhioct.dev.rest.config.RestResponse;
import budhioct.dev.service.StakeholderService;
import budhioct.dev.utilities.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/v1/stakeholder")
@PreAuthorize("hasAnyRole('USER')")
public class StakeholderRestController {

    @Autowired
    private StakeholderService stakeholderService;

    @GetMapping(
            path = "/fetch",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAnyAuthority('user:read')")
    public ResponseEntity<Map<String, List<StakeholderDTO.StakeholderResponse>>> listStakeholder() {
        List<StakeholderDTO.StakeholderResponse> stakeholder = stakeholderService.listStakeholder();
        Map<String, List<StakeholderDTO.StakeholderResponse>> response = new HashMap<>();
        response.put("stakeholders", stakeholder);
        return ResponseEntity.ok(response);
    }

    @GetMapping(
            path = "{id}/detail",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAnyAuthority('user:read')")
    public RestResponse.object<StakeholderDTO.StakeholderDetailResponse> detailStakeholder(@PathVariable(name = "id") Long id) {
        StakeholderDTO.StakeholderDetailResponse stakeholder = stakeholderService.detailStakeholder(id);
        return RestResponse.object.<StakeholderDTO.StakeholderDetailResponse>builder()
                .data(stakeholder)
                .status_code(Constants.OK)
                .message(Constants.ITEM_EXIST_MESSAGE)
                .build();
    }

    @PostMapping(
            path = "{id}/re-production",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAnyAuthority('user:create')")
    public RestResponse.object<StakeholderDTO.StakeholderDetailResponse> reProductionStock(@PathVariable(name = "id") Long id) {
        StakeholderDTO.StakeholderDetailResponse stakeholder = stakeholderService.reProductionStock(id);
        return RestResponse.object.<StakeholderDTO.StakeholderDetailResponse>builder()
                .data(stakeholder)
                .status_code(Constants.OK)
                .message(Constants.ITEM_EXIST_MESSAGE)
                .build();
    }

}
