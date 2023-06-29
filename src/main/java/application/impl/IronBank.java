package application.impl;

import application.Account;
import application.Bank;
import application.Human;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.PostConstruct;
import org.springframework.beans.factory.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author Anton Salnikov
 */
@Component
public class IronBank implements Bank {

    @Autowired
    private Account bankAccount;
    @Autowired
    private Human bankClient;
    private BigDecimal total;

    @PostConstruct
    public void init() {
        total = new BigDecimal("1000000000000");
    }

    @Override
    public BigDecimal saveMoney(BigDecimal money) {
        return total.add(money);
    }

    @Override
    public BigDecimal getMoney(BigDecimal money) {
        return total.subtract(money);
    }

}
