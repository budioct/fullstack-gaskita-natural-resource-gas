package budhioct.dev.rest.controller;

import budhioct.dev.dto.StockDTO;
import budhioct.dev.rest.config.RestResponse;
import budhioct.dev.service.StockService;
import budhioct.dev.utilities.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/stock")
@PreAuthorize("hasAnyRole('USER')")
public class StockRestController {

    @Autowired
    StockService stockService;

    @GetMapping(
            path = "/{id}/monitoring-stock",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAnyAuthority('user:read')")
    public RestResponse.object<StockDTO.StockResponse> monitorStockLevels(@PathVariable(name = "id") Long id) {
        StockDTO.StockResponse stock = stockService.monitorStockLevels(id);
        return RestResponse.object.<StockDTO.StockResponse>builder()
                .data(stock)
                .status_code(Constants.OK)
                .message(Constants.ITEM_EXIST_MESSAGE)
                .build();
    }


}
