package application.impl;

import application.Account;
import org.springframework.beans.factory.annotation.PostConstruct;
import org.springframework.beans.factory.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author Anton Salnikov
 */
@Component
public class BankAccount implements Account {
    private int accountNumber;
    private BigDecimal account;

    @PostConstruct
    public void init() {
        accountNumber = 123;
        account = new BigDecimal(1000000);
    }

    @Override
    public int getAccountNumber() {
        return accountNumber;
    }

    @Override
    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    @Override
    public BigDecimal withdrawAccountMoney(BigDecimal money) {
        account.subtract(money);
        return money;
    }

    @Override
    public void saveAccountMoney(BigDecimal money) {
        account.add(money);
    }

    @Override
    public BigDecimal checkAccount() {
        return account;
    }

}
