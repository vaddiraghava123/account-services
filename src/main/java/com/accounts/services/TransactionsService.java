package com.accounts.services;

/**
 * Transactions Services
 * 
 * 1. Do transaction using transactional management
 * 2. Get transaction history
 * 3. Get Transaction history using Page
 * 4. Detect fraud
 */
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.security.auth.login.AccountException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.accounts.constants.AccConstants;
import com.accounts.entity.Accounts;
import com.accounts.entity.Transaction;
import com.accounts.exception.TransactionException;
import com.accounts.repository.TransactionRepository;

@Service
public class TransactionsService {

    private final TransactionRepository transactionRepository;
    private final AccountServices accountService;

    public TransactionsService(TransactionRepository transactionRepository, AccountServices accountService) {
        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
    }

    @Transactional
    public Transaction makeTransaction(Long accountId, Transaction transaction) throws AccountException, RuntimeException {
        Accounts account = accountService.getAccount(accountId)
                .orElseThrow(() -> new RuntimeException(AccConstants.ACC_NOT_FOUND));

        if (transaction.getTransactionType().equals(AccConstants.DEBIT_TYPE)) {
            if (account.getBalance().compareTo(transaction.getAmount()) < 0) {
                throw new TransactionException(AccConstants.INSUFFICIENT_FUNDS);
            }
            account.setBalance(account.getBalance().subtract(transaction.getAmount()));
        } else if (transaction.getTransactionType().equals(AccConstants.CREDIT_TYPE)) {
            account.setBalance(account.getBalance().add(transaction.getAmount()));
        }

        accountService.updateAccount(account.getId(), account);
        transaction.setAccount(account);
        return transactionRepository.save(transaction);
    }

    public List<Transaction> getTransactions(Long accountId) throws AccountException, RuntimeException {
    	Accounts account = accountService.getAccount(accountId)
                .orElseThrow(() -> new RuntimeException(AccConstants.ACC_NOT_FOUND));
        return transactionRepository.findByAccountId(accountId);
    }
    
    /**
     * Get transactions history information
     * Optional Startdate and enddate
     * Optional Transaction Type ( type)
     * @param accountId
     * @param startDate
     * @param endDate
     * @param type
     * @param pageable
     * @return
     */
    public Page<Transaction> getTransactions(Long accountId, 
    											LocalDateTime startDate, LocalDateTime endDate, String type, Pageable pageable) {
        return transactionRepository.findByAccountIdAndFilters(accountId, startDate, endDate, type, pageable);
    }

    /**
     * Detection Fruad
     * compare with default balance amount and debit amount 
     * @param accountId
     * @return
     * @throws AccountException
     * @throws RuntimeException
     */
    public boolean detectFraud(Long accountId) throws AccountException, RuntimeException {
        List<Transaction> transactions = getTransactions(accountId);
        BigDecimal totalDailyWithdrawals = transactions.stream()
                .filter(t -> t.getTransactionType().equals(AccConstants.DEBIT_TYPE) && t.getTimestamp().toLocalDate().equals(LocalDate.now()))
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return totalDailyWithdrawals.compareTo(new BigDecimal(AccConstants.DEFAULT_BALANCE_AMOUNT)) > 0;
    }
}

