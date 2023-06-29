package application.impl;

import application.Account;
import application.Human;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.PostConstruct;
import org.springframework.beans.factory.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author Anton Salnikov
 */
@Component
public class BankClient implements Human {
    private String name;
    private BigDecimal cash;
    @Autowired
    private Account bankAccount;

    @PostConstruct
    public void init() {
        name = "Иванов Иван Иванович";
        cash = new BigDecimal("500");
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void withdrawMoney(BigDecimal money) {
        cash.add(bankAccount.withdrawAccountMoney(money));
    }

    @Override
    public void putMoney(BigDecimal money) {
        bankAccount.saveAccountMoney(money);
    }

}
