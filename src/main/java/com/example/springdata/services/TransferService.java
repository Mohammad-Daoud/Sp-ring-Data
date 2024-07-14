package com.example.springdata.services;

import com.example.springdata.exceptions.AccountNotFoundException;
import com.example.springdata.model.Account;
import com.example.springdata.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class TransferService {

    private final AccountRepository accountRepository;

    @Autowired
    public TransferService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public void transferMoney(long idSender, long idReceiver, BigDecimal amount){
        Account sender =
                accountRepository.findById(idSender).orElse(null);
        if (sender == null)
            throw new AccountNotFoundException();

        Account receiver =
                accountRepository.findById(idReceiver).orElse(null);
        if (receiver == null)
            throw new AccountNotFoundException();

        BigDecimal senderNewAmount = sender.getAmount().subtract(amount);
        BigDecimal receiverNewAmount = receiver.getAmount().add(amount);

        accountRepository.changeAmount(idSender, senderNewAmount);
        accountRepository.changeAmount(idReceiver, receiverNewAmount);
    }


}
