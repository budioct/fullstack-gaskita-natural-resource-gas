package budhioct.dev.rest.controller;

import budhioct.dev.dto.OfficialAgentDTO;
import budhioct.dev.rest.config.RestResponse;
import budhioct.dev.service.OfficialAgentService;
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
@RequestMapping(path = "/api/v1/official-agent")
@PreAuthorize("hasAnyRole('USER')")
public class OfficialAgentRestController {

    @Autowired
    private OfficialAgentService officialAgentService;

    @GetMapping(
            path = "/fetch",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAnyAuthority('user:read')")
    public ResponseEntity<Map<String, List<OfficialAgentDTO.OfficialAgentResponse>>> listOfficialAgent(){
        List<OfficialAgentDTO.OfficialAgentResponse> officialAgent = officialAgentService.listOfficialAgent();
        Map<String, List<OfficialAgentDTO.OfficialAgentResponse>> response = new HashMap<>();
        response.put("officialAgent", officialAgent);
        return ResponseEntity.ok(response);
    }

    @GetMapping(
            path = "{id}/detail",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAnyAuthority('user:read')")
    public RestResponse.object<OfficialAgentDTO.OfficialAgentDetailResponse> detailOfficialAgent(@PathVariable(name = "id") Long id) {
        OfficialAgentDTO.OfficialAgentDetailResponse officialAgent = officialAgentService.detailOfficialAgent(id);
        return RestResponse.object.<OfficialAgentDTO.OfficialAgentDetailResponse>builder()
                .data(officialAgent)
                .status_code(Constants.OK)
                .message(Constants.ITEM_EXIST_MESSAGE)
                .build();
    }

}
