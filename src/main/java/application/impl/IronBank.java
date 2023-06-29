package application;

import java.math.BigDecimal;

/**
 * @author Anton Salnikov
 */
public class IronBank implements Bank{

    private BigDecimal total = new BigDecimal("1000000000000");

    public IronBank() {
    }

    @Override
    public BigDecimal saveMoney(BigDecimal money) {
        return total.add(money);
    }

    @Override
    public BigDecimal getMoney(BigDecimal money) {
        return total.add(-money);
    }
}
