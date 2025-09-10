package com.example.transactiondispatcher.service;

import com.example.transactiondispatcher.entity.Acquirer;
import com.example.transactiondispatcher.entity.Transaction;
import com.example.transactiondispatcher.repository.AcquirerRepository;
import com.example.transactiondispatcher.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    @Autowired
    private AcquirerRepository acquirerRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public Transaction createTransaction(Integer userId, BigDecimal amount) {
        List<Acquirer> acquirers = acquirerRepository.findAll();

        List<TransactionRepository.AcquirerTransactionCount> counts = transactionRepository.countTransactionsByAcquirer();

        long totalTx = counts.stream().mapToLong(TransactionRepository.AcquirerTransactionCount::getCount).sum();

        Map<Integer, Long> acquirerTxCountMap = counts.stream()
                .collect(Collectors.toMap(TransactionRepository.AcquirerTransactionCount::getAcquirerId, TransactionRepository.AcquirerTransactionCount::getCount));

        Acquirer selectedAcquirer = null;
        double maxGap = -Double.MAX_VALUE;

        for (Acquirer acquirer : acquirers) {
            long actualCount = acquirerTxCountMap.getOrDefault(acquirer.getId(), 0L);
            double expectedCount = totalTx * (acquirer.getPercentShare() / 100.0);
            double gap = expectedCount - actualCount;

            if (gap > maxGap) {
                maxGap = gap;
                selectedAcquirer = acquirer;
            }
        }

        if (selectedAcquirer == null) {
            selectedAcquirer = acquirers.get(0);
        }

        Transaction transaction = new Transaction();
        transaction.setUserId(userId);
        transaction.setAmount(amount);
        transaction.setAcquirer(selectedAcquirer);
        transaction.setStatus("SUCCESS");

        return transactionRepository.save(transaction);
    }

    public Map<String, List<Transaction>> getTransactionsGroupedByAcquirer() {
        List<Acquirer> acquirers = acquirerRepository.findAll();
        Map<String, List<Transaction>> grouped = new HashMap<>();

        for (Acquirer a : acquirers) {
            grouped.put(a.getName(), transactionRepository.findByAcquirer(a));
        }
        return grouped;
    }
}
