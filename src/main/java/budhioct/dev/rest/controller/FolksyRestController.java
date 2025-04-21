package budhioct.dev.rest.controller;

import budhioct.dev.dto.FolksyDTO;
import budhioct.dev.rest.config.RestResponse;
import budhioct.dev.service.FolksyService;
import budhioct.dev.utilities.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/folksy")
@PreAuthorize("hasAnyRole('USER')")
public class FolksyRestController {

    @Autowired
    private FolksyService folksyService;

    @GetMapping(
            path = "{id}/detail",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAnyAuthority('user:read')")
    public RestResponse.object<FolksyDTO.FolksyResponse> detailFolksy(@PathVariable(name = "id") Long id) {
        FolksyDTO.FolksyResponse folksy = folksyService.detailFolksy(id);
        return RestResponse.object.<FolksyDTO.FolksyResponse>builder()
                .data(folksy)
                .status_code(Constants.OK)
                .message(Constants.ITEM_EXIST_MESSAGE)
                .build();
    }

}
