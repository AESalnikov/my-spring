package application;

import java.math.BigDecimal;

/**
 * @author Anton Salnikov
 */
public interface Bank {
    BigDecimal saveMoney(BigDecimal money);

    BigDecimal getMoney(BigDecimal money);
}
