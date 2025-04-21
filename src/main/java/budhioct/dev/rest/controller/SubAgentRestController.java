package budhioct.dev.rest.controller;

import budhioct.dev.dto.SubAgentDTO;
import budhioct.dev.rest.config.RestResponse;
import budhioct.dev.service.SubAgentService;
import budhioct.dev.utilities.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/sub-agent")
@PreAuthorize("hasAnyRole('USER')")
public class SubAgentRestController {

    @Autowired
    SubAgentService subAgentService;

    @GetMapping(
            path = "/fetch",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAnyAuthority('user:read')")
    public ResponseEntity<Map<String, List<SubAgentDTO.SubAgentResponse>>> listSubAgent() {
        List<SubAgentDTO.SubAgentResponse> subAgent = subAgentService.listSubAgent();
        Map<String, List<SubAgentDTO.SubAgentResponse>> response = new HashMap<>();
        response.put("subAgent", subAgent);
        return ResponseEntity.ok(response);
    }

    @GetMapping(
            path = "/{id}/detail",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAnyAuthority('user:read')")
    public RestResponse.object<SubAgentDTO.SubAgentDetailResponse> detailSubAgent(@PathVariable(name = "id") Long id) {
        SubAgentDTO.SubAgentDetailResponse subAgent = subAgentService.detailSubAgentDetail(id);
        return RestResponse.object.<SubAgentDTO.SubAgentDetailResponse>builder()
                .data(subAgent)
                .status_code(Constants.OK)
                .message(Constants.ITEM_EXIST_MESSAGE)
                .build();
    }

    @GetMapping(
            path = "/{id}/detail/projection",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAnyAuthority('user:read')")
    public RestResponse.object<SubAgentDTO.SubAgentDetailResponse> detailSubAgentWithTransactions(@PathVariable(name = "id") Long id) {
        SubAgentDTO.SubAgentDetailResponse subAgent = subAgentService.getSubAgentWithTransactions(id);
        return RestResponse.object.<SubAgentDTO.SubAgentDetailResponse>builder()
                .data(subAgent)
                .status_code(Constants.OK)
                .message(Constants.ITEM_EXIST_MESSAGE)
                .build();
    }

}
