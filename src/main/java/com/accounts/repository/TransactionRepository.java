package com.accounts.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.accounts.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccountId(Long accountId);
    
    @Query("SELECT t FROM Transaction t WHERE t.account.Id = :accountId " +
    	       "AND (:startDate IS NULL OR t.timestamp >= :startDate) " +
    	       "AND (:endDate IS NULL OR t.timestamp <= :endDate) " +
    	       "AND (:type IS NULL OR t.transactionType = :type)")
    	Page<Transaction> findByAccountIdAndFilters(
    	        @Param("accountId") Long accountId,
    	        @Param("startDate") LocalDateTime startDate,
    	        @Param("endDate") LocalDateTime endDate,
    	        @Param("type") String type,
    	        Pageable pageable
    	);
}

