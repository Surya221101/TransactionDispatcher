package com.example.transactiondispatcher.controller;

import com.example.transactiondispatcher.entity.Transaction;
import com.example.transactiondispatcher.service.TransactionService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<?> createTransaction(@RequestBody TransactionRequest request) {
        Transaction transaction = transactionService.createTransaction(request.getUserId(), request.getAmount());

        return ResponseEntity.ok(Map.of(
                "transactionId", transaction.getId(),
                "assignedAcquirer", transaction.getAcquirer().getName(),
                "status", transaction.getStatus()
        ));
    }

    @GetMapping("/grouped")
    public ResponseEntity<Map<String, List<Transaction>>> getGroupedTransactions() {
        return ResponseEntity.ok(transactionService.getTransactionsGroupedByAcquirer());
    }
}

@Data
class TransactionRequest {
    private Integer userId;
    private BigDecimal amount;
}
