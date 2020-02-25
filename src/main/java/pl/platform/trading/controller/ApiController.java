package pl.platform.trading.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.platform.trading.rates.ExchangeRate;
import pl.platform.trading.rates.ExchnageRatesProvider;
import pl.platform.trading.resolver.TransactionResolver;
import pl.platform.trading.sql.openedposition.OpenedPosition;
import pl.platform.trading.sql.openedposition.OpenedPositionRepository;
import pl.platform.trading.sql.pendingorder.PendingOrder;
import pl.platform.trading.sql.pendingorder.PendingOrderBuilder;
import pl.platform.trading.sql.pendingorder.PendingOrderRepository;
import pl.platform.trading.sql.user.User;

@Transactional
@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private PendingOrderRepository pendingOrdersDao;

    @Autowired
    private OpenedPositionRepository openedPositionsDao;

    @Autowired
    private TransactionResolver resolver;

    @Autowired
    private ExchnageRatesProvider rates;

    @Autowired
    private SessionStore sessionStore;



    @GetMapping("/rates")
    public List<ExchangeRate> getRates() {
        return rates.getRates();
    }

    @GetMapping("/rate/{pair}")
    public ExchangeRate getPairRate(@PathVariable("pair") String pair) {
        return rates.getPairRate(pair);
    }

    @GetMapping("/opened_positions")
    public List<OpenedPosition> openedPositions() {
        User user = sessionStore.getCurrentUser();

        List<OpenedPosition> openedList = openedPositionsDao.findByUid(user.getUid());
        if (openedList != null) {
            for (OpenedPosition opened : openedList) {
                ExchangeRate exchangeRate = rates.getPairRate(opened.getCurrencyPair());
                if(exchangeRate != null) {
                    BigDecimal profit = resolver.calculateProfit(opened, exchangeRate);
                    opened.setCurrentProfit(profit);
                }
                else {
                    opened.setCurrentProfit(null);
                }
            }
            return openedList;
        } else {
            return null;
        }
    }

    @GetMapping("/pending_orders")
    public List<PendingOrder> pendingOrders() {
        User user = sessionStore.getCurrentUser();

        return pendingOrdersDao.findByUid(user.getUid());
    }

    @PostMapping("/long/{pair}/{amount}")
    public String longPosition(@PathVariable("pair") String pair,
                               @PathVariable("amount") BigDecimal amount) {
        User user = sessionStore.getCurrentUser();

        if (resolver.openPosition(user.getUid(), pair, amount, true)) {
            return "Success";
        } else {
            return "Error: not enough money";
        }
    }

    @PostMapping("/short/{pair}/{amount}")
    public String shortPosition(@PathVariable("pair") String pair,
                                @PathVariable("amount") BigDecimal amount) {
        User user = sessionStore.getCurrentUser();

        if (resolver.openPosition(user.getUid(), pair, amount, false)) {
            return "Success";
        } else {
            return "Error: not enough money";
        }
    }

    @PostMapping("/order_long/{pair}/{amount}/{trigger}/{price}")
    public String orderLong(@PathVariable("pair") String pair,
                            @PathVariable("amount") BigDecimal amount,
                            @PathVariable("trigger") boolean trigger,
                            @PathVariable("price") BigDecimal price) {
        User user = sessionStore.getCurrentUser();

        PendingOrder order = new PendingOrderBuilder()
                .setUid(user.getUid())
                .setTid(null)
                .setCurrencyPair(pair)
                .setAmount(amount)
                .setOrderPrice(price)
                .setLongPosition(true)
                .setTriggeredAbove(trigger)
                .build();

        pendingOrdersDao.save(order);

        resolver.resolve();

        return "success";
    }

    @PostMapping("/order_short/{pair}/{amount}/{trigger}/{price}")
    public String orderShort(@PathVariable("pair") String pair,
                             @PathVariable("amount") BigDecimal amount, @PathVariable("trigger") boolean trigger,
                             @PathVariable("price") BigDecimal price) {
        User user = sessionStore.getCurrentUser();

        PendingOrder order = new PendingOrderBuilder()
                .setUid(user.getUid())
                .setTid(null)
                .setCurrencyPair(pair)
                .setAmount(amount)
                .setOrderPrice(price)
                .setLongPosition(false)
                .setTriggeredAbove(trigger)
                .build();

        pendingOrdersDao.save(order);

        resolver.resolve();

        return "success";
    }

    @PostMapping("/close/{tid}")
    public String closePosition(@PathVariable("tid") Long tid) {
        User user = sessionStore.getCurrentUser();;

        OpenedPosition opened = openedPositionsDao.findByTid(tid);

        if (opened != null) {
            if (user.getUid() == opened.getUid()) {
                pendingOrdersDao.deleteByTid(tid);

                resolver.closePosition(tid);

                return "Position closed successfully";
            } else {
                return "Position belongs to another user";
            }
        } else {
            return "Position closing failure";
        }
    }

    @PostMapping("/order_closure/{tid}/{trigger}/{price}")
    public String orderClosure(@PathVariable("tid") Long tid, @PathVariable("trigger") boolean trigger,
                               @PathVariable("price") BigDecimal price) {
        User user = sessionStore.getCurrentUser();

        OpenedPosition opened = openedPositionsDao.findByTid(tid);
        if (opened != null) {
            if (user.getUid() == opened.getUid()) {

                PendingOrder order = new PendingOrderBuilder()
                        .setUid(opened.getUid())
                        .setTid(tid)
                        .setCurrencyPair(opened.getCurrencyPair())
                        .setAmount(null)
                        .setOrderPrice(price)
                        .setLongPosition(opened.isLongPosition())
                        .setTriggeredAbove(trigger)
                        .build();

                pendingOrdersDao.save(order);

                resolver.resolve();

                return "Order placed successfully";
            } else {
                return "Position belongs to another user";
            }
        } else {
            return "Order placing failure";
        }
    }

    @PostMapping("/cancel/{oid}")
    public String cancelOrder(@PathVariable("oid") Long oid) {
        User user = sessionStore.getCurrentUser();

        PendingOrder pending = pendingOrdersDao.findByOid(oid);

        if (pending != null) {
            if (user.getUid() == pending.getUid()) {
                pendingOrdersDao.deleteByOid(oid);
                return "Order cancelled successfully";
            } else {
                return "Order belongs to another user";
            }
        } else {
            return "Order closing failure";
        }
    }
}
