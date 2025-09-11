package com.example.transactiondispatcher.service;
import com.example.transactiondispatcher.entity.Acquirer;
import com.example.transactiondispatcher.entity.Transaction;
import com.example.transactiondispatcher.repository.AcquirerRepository;
import com.example.transactiondispatcher.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final AcquirerRepository acquirerRepository;
    private final TransactionRepository transactionRepository;


    private List<Acquirer> weightedAcquirers = new ArrayList<>();
    private final AtomicInteger pointer = new AtomicInteger(0);


    private synchronized void loadWeightedAcquirers() {
        List<Acquirer> acquirers = acquirerRepository.findAll();
        weightedAcquirers.clear();
        for (Acquirer acquirer : acquirers) {
            int weight = acquirer.getPercentShare(); // percent as weight units
            for (int i = 0; i < weight; i++) {
                weightedAcquirers.add(acquirer);
            }
        }
    }


    public synchronized Transaction createTransaction(Integer userId, BigDecimal amount) {
        if (weightedAcquirers.isEmpty()) {
            loadWeightedAcquirers();
        }
        int idx = pointer.getAndUpdate(i -> (i + 1) % weightedAcquirers.size());
        Acquirer selectedAcquirer = weightedAcquirers.get(idx);

        Transaction transaction = Transaction.builder()
                .userId(userId)
                .amount(amount)
                .acquirer(selectedAcquirer)
                .status("SUCCESS")
                .build();

        return transactionRepository.save(transaction);
    }


    public synchronized void refreshWeightedAcquirers() {
        pointer.set(0);
        loadWeightedAcquirers();
    }


    public Map<String, List<Transaction>> getTransactionsGroupedByAcquirer() {
        List<Acquirer> acquirers = acquirerRepository.findAll();
        Map<String, List<Transaction>> grouped = new HashMap<>();
        for (Acquirer acquirer : acquirers) {
            List<Transaction> txs = transactionRepository.findByAcquirer(acquirer);
            grouped.put(acquirer.getName(), txs);
        }
        return grouped;
    }
}
