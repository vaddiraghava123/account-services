package com.accounts.services;

/**
 *  AccountServices
 *  
 *  1. create Account
 *  2. Retrieve account information
 *  3. Update account information
 *  4. Delete account information ( Set status from Active to Suspended )
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accounts.constants.AccConstants;
import com.accounts.entity.Accounts;
import com.accounts.repository.AccountRepository;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.Optional;

import javax.security.auth.login.AccountException;

@Service
public class AccountServices {

    private final AccountRepository accountRepository;
  
    public AccountServices(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Accounts  createAccount(Accounts account) throws AccountException {
    	BigDecimal defaultBalance = new BigDecimal(AccConstants.DEFAULT_BALANCE_AMOUNT);// Default balance 1000
    	if(	account.getBalance().compareTo(defaultBalance) == 0 ) {
    		account.setBalance(defaultBalance);
    	} else if(	account.getBalance().compareTo(defaultBalance) > 0 ) {
    		account.setBalance(account.getBalance());
    	} else {
    		throw new AccountException(MessageFormat.format(AccConstants.INSUFFICIENT_BALANCE, account.getBalance()));
    	}
        return accountRepository.save(account);
    }

    public Optional<Accounts> getAccount(Long accountId) throws AccountException {
    	if(accountRepository.findById(accountId).isPresent()) {
    		return accountRepository.findById(accountId);
    	} else {
    		throw new AccountException(MessageFormat.format(AccConstants.ACC_NOT_FOUND, accountId));
    	}
    }

    public Accounts updateAccount(Long accountId, Accounts updatedAccount) {
        Accounts account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException(AccConstants.ACC_NOT_FOUND));

        account.setAcc_name(updatedAccount.getAcc_name());
        account.setUpdated_at(LocalDateTime.now());
        return accountRepository.save(account);
    }

    public void suspendAccount(Long accountId) {
        Accounts account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException(AccConstants.ACC_NOT_FOUND));
        //Verify account status
        if(account.getStatus() != null && 
        		account.getStatus().equals(AccConstants.ACTIVE_STATUS)) {
        account.setStatus(AccConstants.SUSPENDED_STATUS);
        } 
        accountRepository.save(account);
    }
}
