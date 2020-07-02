package pl.platform.trading.resolver;

import pl.platform.trading.rates.ExchangeRate;
import pl.platform.trading.sql.openedposition.OpenedPosition;

import java.math.BigDecimal;

public interface TransactionResolver {
    BigDecimal calculateProfit(OpenedPosition opened, ExchangeRate rate);
    boolean updateAccountBalance(Long uid, BigDecimal updateAmount);
    boolean closePosition(Long tid);
    boolean openPosition(Long uid, String currencyPair, BigDecimal amount, boolean longPosition);
    void resolve();
}
