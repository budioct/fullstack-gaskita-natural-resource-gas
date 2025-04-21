package budhioct.dev.rest.controller;

import budhioct.dev.dto.TransactionDTO;
import budhioct.dev.rest.config.RestResponse;
import budhioct.dev.service.TransactionService;
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
@RequestMapping("/api/v1/transaction")
@PreAuthorize("hasAnyRole('USER')")
public class TransactionRestController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping(
            path = "/proses",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAnyAuthority('user:create')")
    public RestResponse.object<String> createTransaction(
            @RequestParam(name = "sourceSubAgentId") Long sourceSubAgentId,
            @RequestParam(name = "targetFolksyId") Long targetFolksyId,
            @RequestParam(name = "amountGas") Long amountGas,
            @RequestParam(name = "pricePerUnit") Double pricePerUnit
    ) {
        transactionService.createTransaction(sourceSubAgentId, targetFolksyId, amountGas, pricePerUnit);
        return RestResponse.object.<String>builder()
                .data("")
                .status_code(Constants.OK)
                .message(Constants.TRANSACTION_MESSAGE)
                .build();
    }

    @GetMapping(
            path = "/fetch",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAnyAuthority('user:read')")
    public ResponseEntity<Map<String, List<TransactionDTO.TransactionResponse>>> getTransaction() {
        List<TransactionDTO.TransactionResponse> transaction = transactionService.listTransaction();
        Map<String, List<TransactionDTO.TransactionResponse>> response = new HashMap<>();
        response.put("transactions", transaction);
        return ResponseEntity.ok(response);
    }


}
