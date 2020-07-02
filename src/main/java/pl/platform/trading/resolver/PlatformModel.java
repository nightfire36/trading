package pl.platform.trading.resolver;

import pl.platform.trading.controller.UserDto;
import pl.platform.trading.rates.ExchangeRate;
import pl.platform.trading.sql.closedposition.ClosedPosition;
import pl.platform.trading.sql.openedposition.OpenedPosition;
import pl.platform.trading.sql.pendingorder.PendingOrder;
import pl.platform.trading.sql.user.User;

import java.math.BigDecimal;
import java.util.List;

public interface PlatformModel {
    Integer registerUser(UserDto userDto);
    User userInfo();
    List<ExchangeRate> getRates();
    ExchangeRate getPairRate(String pair);
    List<OpenedPosition> getOpenedPositions();
    List<PendingOrder> getPendingOrders();
    List<ClosedPosition> getClosedPositions();
    Integer takeLongPosition(String pair, BigDecimal amount);
    Integer takeShortPosition(String pair, BigDecimal amount);
    Integer placeOrderForLongPosition(String pair, BigDecimal amount, boolean trigger, BigDecimal price);
    Integer placeOrderForShortPosition(String pair, BigDecimal amount, boolean trigger, BigDecimal price);
    Integer closePosition(Long tid);
    Integer placeOrderForPositionClosure(Long tid, boolean trigger, BigDecimal price);
    Integer cancelOrder(Long oid);
}
