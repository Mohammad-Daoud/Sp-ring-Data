package com.example.springdata.controllers;


import com.example.springdata.dto.MessageResponse;
import com.example.springdata.dto.TransferRequest;
import com.example.springdata.exceptions.AccountNotFoundException;
import com.example.springdata.model.Account;
import com.example.springdata.services.AccountService;
import com.example.springdata.services.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class AccountController {

    @Autowired
    private TransferService transferService;
    @Autowired
    private AccountService accountService;

    @GetMapping("/accounts")
    public Iterable<Account> getAllAccounts(@RequestParam(required = false) String name) {
        if (name == null) {
            return accountService.getAllAccounts();
        } else {
            return accountService.findAccountByName(name);
        }
    }

    @PutMapping("/transfer")
    public ResponseEntity<MessageResponse> transferMoney(@RequestBody TransferRequest request){
        try {
            transferService.transferMoney(
                    request.getAccountSenderId(),
                    request.getAccountReceiverId(),
                    request.getAmount()
            );
            MessageResponse response = MessageResponse.builder()
                    .status(HttpStatus.OK.value())
                    .message("Money transferred successfully!")
                    .data(Map.of("sender id", request.getAccountSenderId(), "recieverId", request.getAccountReceiverId()))
                    .build();
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(response);

        }catch (Exception e){
            MessageResponse response = MessageResponse.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Failed while transfer money")
                    .data("")
                    .build();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }

    }

    @PostMapping("/create/{name}")
    public ResponseEntity<MessageResponse> createAccount(@PathVariable String name){
        boolean created = accountService.createAccount(name);
        if (created){
            MessageResponse response = MessageResponse.builder()
                    .status(HttpStatus.CREATED.value())
                    .message("Account created successfully!")
                    .data(accountService.findAccountByName(name))
                    .build();
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(response);

        }else {
            MessageResponse response = MessageResponse.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Failed while creating account")
                    .build();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<MessageResponse> deleteAccount(@PathVariable long id){
        try {
            Account targetAccount = accountService.findAccountById(id);
            accountService.deleteAccountById(id);
            MessageResponse response = MessageResponse.builder()
                    .status(HttpStatus.OK.value())
                    .message("Account deleted successfully!")
                    .data(targetAccount)
                    .build();
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(response);
        }catch (AccountNotFoundException e){
            MessageResponse response = MessageResponse.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("Failed while deleting account (account not found)")
                    .data("")
                    .build();
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(response);
        }catch (RuntimeException e){
            MessageResponse response = MessageResponse.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Failed while deleting account")
                    .data("")
                    .build();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);

        }

    }


}
