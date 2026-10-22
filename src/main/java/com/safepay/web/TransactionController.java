package com.safepay.web;

import java.io.IOException;
import java.nio.file.Path;
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

import com.safepay.application.TransactionService;
import com.safepay.domain.Transaction;
import com.safepay.infrastructure.CsvTransactionReader;

/**
 * Ring: web adapter
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

    public record ImportResult(int imported, List<String> errors) {
    }

    /**
     * Mapping the JSON DTO to the domain object is the web ring's job.
     */
    private static Transaction toDomain(TransactionDto dto) {
        return new Transaction(
                dto.transactionId(),
                dto.customerId(),
                dto.amount(),
                dto.currency(),
                dto.location(),
                dto.timestamp());
    }

    @PostMapping
    public ResponseEntity<TransactionRiskResponse> create(@RequestBody TransactionDto transactionDto) {
        Transaction saved = transactionService.save(toDomain(transactionDto));
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

    /**
     * Reading the file is infrastructure; deciding what to save is the application service.
     */
    @PostMapping("/import")
    public ImportResult importCsv() throws IOException {
        CsvTransactionReader reader = new CsvTransactionReader();
        List<Transaction> transactions = reader.read(Path.of("data/transactions.csv"));
        for (Transaction transaction : transactions) {
            transactionService.save(transaction);
        }
        return new ImportResult(transactions.size(), reader.getErrors());
    }
}
