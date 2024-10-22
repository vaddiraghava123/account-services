package com.accounts.controller;

/**
 * Transaction Controller
 * 1.  do transactions
 * 2.  Identify Fraud transaction
 * 3.  Get Transaction History
 * 4.  Get Transaction History using pagination
 */
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.security.auth.login.AccountException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.accounts.entity.Transaction;
import com.accounts.services.TransactionsService;

@RestController
@RequestMapping("/accounts/{accountId}/transactions")
public class TransactionController {

    private final TransactionsService transactionService;

    public TransactionController(TransactionsService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public Transaction makeTransaction(@PathVariable Long accountId, @RequestBody Transaction transaction) throws AccountException, RuntimeException {
        return transactionService.makeTransaction(accountId, transaction);
    }
    
    @GetMapping("/fraud-detection")
    public boolean detectFraud(@PathVariable Long accountId) throws AccountException, RuntimeException {
        return transactionService.detectFraud(accountId);
    }
    
    /**
     * Get transaction history 
     * Ex- /accounts/1/transactions?page=0&size=10&startDate=2024-01-01T00:00:00
     * 					&endDate=2024-01-31T23:59:59&type=CREDIT
     * @param accountId
     * @param startDate
     * @param endDate
     * @param type
     * @param page
     * @param size
     * @return
     * @throws AccountException
     * @throws RuntimeException
     */
    @GetMapping
    public Page<Transaction> getTransactions(
            @PathVariable Long accountId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size) throws AccountException, RuntimeException  {
        Pageable pageable = PageRequest.of(page, size);
        
        LocalDateTime start = (startDate != null) ? LocalDateTime.parse(startDate, DateTimeFormatter.ISO_DATE_TIME) : null;
        LocalDateTime end = (endDate != null) ? LocalDateTime.parse(endDate, DateTimeFormatter.ISO_DATE_TIME) : null;

        return transactionService.getTransactions(accountId, start, end, type, pageable);
    }

    //Retrieve Transaction History
    @GetMapping("/getTranHistory")
    public List<Transaction> getTransactions(@PathVariable Long accountId) throws AccountException, RuntimeException {
        return transactionService.getTransactions(accountId);
    }

   
}
