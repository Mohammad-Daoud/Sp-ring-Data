package com.example.springdata.dto;


import com.example.springdata.model.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountDTO {

    private String name;
    private BigDecimal amount;

    public static Account toAccountEntity(AccountDTO accountDTO){
        return Account.builder()
                .name(accountDTO.getName())
                .amount(accountDTO.getAmount())
                .build();
    }

}
