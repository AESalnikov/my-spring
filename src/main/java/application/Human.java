package application;

import java.math.BigDecimal;

/**
 * @author Anton Salnikov
 */
public interface Human {
    String getName();

    void withdrawMoney(BigDecimal money);

    void putMoney(BigDecimal money);
}
