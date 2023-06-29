package application;

import java.math.BigDecimal;

/**
 * @author Anton Salnikov
 */
public interface Account {
    int getAccountNumber();
    void setAccountNumber(int accountNumber);
    BigDecimal withdrawAccountMoney(BigDecimal money);
    void saveAccountMoney(BigDecimal money);
    BigDecimal checkAccount();
}
