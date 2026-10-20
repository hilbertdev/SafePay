package com.safepay;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Controller layer: HTTP endpoints for transactions.
 */
@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    public record TransactionRiskResponse(Transaction transaction, String risk) {
    }

    @PostMapping
    public ResponseEntity<TransactionRiskResponse> create(@RequestBody TransactionDto transactionDto) {
        Transaction saved = transactionService.save(new Transaction(transactionDto));
        String risk = transactionService.assessRisk(saved);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new TransactionRiskResponse(saved, risk));
    }

    @GetMapping
    public List<Transaction> findAll() {
        return transactionService.findAll();
    }

    @GetMapping("/{id}")
    public Transaction findById(@PathVariable String id) {
        return transactionService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
