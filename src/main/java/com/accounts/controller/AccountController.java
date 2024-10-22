package com.accounts.controller;

/**
 * 	AccountController
 * POST /accounts: Create a new account with an initial balance.
- GET /accounts/{accountId}: Get the account details and balance.
- PUT /accounts/{accountId}: Edit the account details only.
- DELETE /accounts/{accountId}: Suspend the account 

@author gvrdr
 */
import org.springframework.web.bind.annotation.*;

import com.accounts.entity.Accounts;
import com.accounts.services.AccountServices;

import java.util.Optional;

import javax.security.auth.login.AccountException;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountServices accountService;

    public AccountController(AccountServices accountService) {
        this.accountService = accountService;
    }

    // Create a new account
    @PostMapping
    public Accounts createAccount(@RequestBody Accounts account) throws AccountException {
        return accountService.createAccount(account);
    }

    // Retrieve account details as based upon accountId
    @GetMapping("/{accountId}")
    public Optional<Accounts> getAccount(@PathVariable Long accountId) throws AccountException {
        return accountService.getAccount(accountId);
    }

    // Update account information based upon accountId
    @PutMapping("/{accountId}")
    public Accounts updateAccount(@PathVariable Long accountId, @RequestBody Accounts account) {
        return accountService.updateAccount(accountId, account);
    }

    // Delete account information based upon accountId
    @DeleteMapping("/{accountId}")
    public void suspendAccount(@PathVariable Long accountId) {
        accountService.suspendAccount(accountId);
    }
}
