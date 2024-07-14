package com.example.springdata.services;

import com.example.springdata.dto.AccountDTO;
import com.example.springdata.exceptions.AccountNotFoundException;
import com.example.springdata.model.Account;
import com.example.springdata.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }


    public Iterable<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public List<Account> findAccountByName(String name) {
        return accountRepository.findAccountsByName(name);
    }

    public Account findAccountById(long id) {
        Account account = accountRepository.findById(id).orElse(null);
        if (account == null)
            throw new AccountNotFoundException();
        return account;
    }

    public boolean createAccount(String name) {
        AccountDTO accountDTO = AccountDTO.builder().name(name).amount(BigDecimal.ZERO).build();
        try {
            accountRepository.save(AccountDTO.toAccountEntity(accountDTO));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // try it by yourself
    public boolean createAccount(AccountDTO accountDTO) {
        try {
            accountRepository.save(AccountDTO.toAccountEntity(accountDTO));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void deleteAccountById(long id) throws RuntimeException{
            accountRepository.deleteById(id);

    }
}
