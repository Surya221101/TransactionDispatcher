package com.example.transactiondispatcher.repository;

import com.example.transactiondispatcher.entity.Acquirer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AcquirerRepository extends JpaRepository<Acquirer, Integer> {
}
