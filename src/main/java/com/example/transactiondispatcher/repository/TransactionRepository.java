package com.example.transactiondispatcher.repository;

import com.example.transactiondispatcher.entity.Acquirer;
import com.example.transactiondispatcher.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    @Query("SELECT t.acquirer.id as acquirerId, COUNT(t) as count FROM Transaction t GROUP BY t.acquirer.id")
    List<AcquirerTransactionCount> countTransactionsByAcquirer();

    interface AcquirerTransactionCount {
        Integer getAcquirerId();
        Long getCount();
    }

    List<Transaction> findByAcquirer(Acquirer acquirer);
}
