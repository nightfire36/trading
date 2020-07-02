package pl.platform.trading.resolver;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;
import pl.platform.trading.controller.SessionStore;
import pl.platform.trading.controller.UserDto;
import pl.platform.trading.rates.ExchangeRate;
import pl.platform.trading.rates.ExchnageRatesProvider;
import pl.platform.trading.sql.closedposition.ClosedPosition;
import pl.platform.trading.sql.closedposition.ClosedPositionRepository;
import pl.platform.trading.sql.openedposition.OpenedPosition;
import pl.platform.trading.sql.openedposition.OpenedPositionRepository;
import pl.platform.trading.sql.pendingorder.PendingOrder;
import pl.platform.trading.sql.pendingorder.PendingOrderBuilder;
import pl.platform.trading.sql.pendingorder.PendingOrderRepository;
import pl.platform.trading.sql.user.User;
import pl.platform.trading.sql.user.UserPrototype;
import pl.platform.trading.sql.user.UserRepository;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PlatformModelImpl implements PlatformModel {

    @Autowired
    private UserRepository usersDao;

    @Autowired
    private PendingOrderRepository pendingOrdersDao;

    @Autowired
    private OpenedPositionRepository openedPositionsDao;

    @Autowired
    private ClosedPositionRepository closedPositionsDao;

    @Autowired
    private TransactionResolver resolver;

    @Autowired
    private ExchnageRatesProvider rates;

    @Autowired
    private SessionStore sessionStore;


    public Integer registerUser(UserDto userDto) {
        User user = new UserPrototype(userDto).cloneFromUserDto();
        if (user != null) {
            try {
                usersDao.save(user);
            } catch (DataIntegrityViolationException e) {
                return ModelStatusCode.ERROR_EMAIL_ALREADY_EXISTS;
            } catch (ConstraintViolationException e) {
                return ModelStatusCode.ERROR_CONSTRAINT_VIOLATION;
            } catch (Exception e) {
                return ModelStatusCode.ERROR_DB_SAVE_EXCEPTION;
            }
            return ModelStatusCode.STATUS_SUCCESS;
        } else {
            return ModelStatusCode.ERROR_INVALID_USER_DATA;
        }
    }

    public User userInfo() {
        return sessionStore.getCurrentUser();
    }

    public List<ExchangeRate> getRates() {
        return rates.getRates();
    }

    public ExchangeRate getPairRate(String pair) {
        return rates.getPairRate(pair);
    }

    public List<OpenedPosition> getOpenedPositions() {
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

    public List<PendingOrder> getPendingOrders() {
        User user = sessionStore.getCurrentUser();

        return pendingOrdersDao.findByUid(user.getUid());
    }

    public List<ClosedPosition> getClosedPositions() {
        User user = sessionStore.getCurrentUser();

        return closedPositionsDao.findByUid(user.getUid());
    }

    public Integer takeLongPosition(String pair, BigDecimal amount) {
        User user = sessionStore.getCurrentUser();

        if (resolver.openPosition(user.getUid(), pair, amount, true)) {
            return ModelStatusCode.STATUS_SUCCESS;
        } else {
            return ModelStatusCode.ERROR_NOT_ENOUGH_MONEY;
        }
    }

    public Integer takeShortPosition(String pair, BigDecimal amount) {
        User user = sessionStore.getCurrentUser();

        if (resolver.openPosition(user.getUid(), pair, amount, false)) {
            return ModelStatusCode.STATUS_SUCCESS;
        } else {
            return ModelStatusCode.ERROR_NOT_ENOUGH_MONEY;
        }
    }

    public Integer placeOrderForLongPosition(String pair, BigDecimal amount, boolean trigger, BigDecimal price) {
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

        try {
            pendingOrdersDao.save(order);
        } catch (Exception e) {
            e.printStackTrace();
            return ModelStatusCode.ERROR_DB_SAVE_EXCEPTION;
        }

        resolver.resolve();

        return ModelStatusCode.STATUS_SUCCESS;
    }

    public Integer placeOrderForShortPosition(String pair, BigDecimal amount, boolean trigger, BigDecimal price) {
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

        try {
            pendingOrdersDao.save(order);
        } catch(Exception e) {
            e.printStackTrace();
            return ModelStatusCode.ERROR_DB_SAVE_EXCEPTION;
        }

        resolver.resolve();

        return ModelStatusCode.STATUS_SUCCESS;
    }

    public Integer closePosition(Long tid) {
        User user = sessionStore.getCurrentUser();;

        OpenedPosition opened = openedPositionsDao.findByTid(tid);

        if (opened != null) {
            if (user.getUid() == opened.getUid()) {

                try {
                    pendingOrdersDao.deleteByTid(tid);
                } catch (Exception e) {
                    return ModelStatusCode.ERROR_DB_DELETE_EXCEPTION;
                }
                resolver.closePosition(tid);

                return ModelStatusCode.STATUS_SUCCESS;
            } else {
                return ModelStatusCode.ERROR_BELONGS_TO_ANOTHER_USER;
            }
        } else {
            return ModelStatusCode.ERROR_CANNOT_FIND_POSITION;
        }
    }

    public Integer placeOrderForPositionClosure(Long tid, boolean trigger, BigDecimal price) {
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

                try {
                    pendingOrdersDao.save(order);
                } catch (Exception e) {
                    e.printStackTrace();
                    return ModelStatusCode.ERROR_DB_SAVE_EXCEPTION;
                }
                resolver.resolve();

                return ModelStatusCode.STATUS_SUCCESS;

            } else {
                return ModelStatusCode.ERROR_BELONGS_TO_ANOTHER_USER;
            }
        } else {
            return ModelStatusCode.ERROR_CANNOT_FIND_POSITION;
        }
    }

    public Integer cancelOrder(Long oid) {
        User user = sessionStore.getCurrentUser();

        PendingOrder pending = pendingOrdersDao.findByOid(oid);

        if (pending != null) {
            if (user.getUid() == pending.getUid()) {
                try {
                    pendingOrdersDao.deleteByOid(oid);
                } catch (Exception e) {
                    e.printStackTrace();
                    return ModelStatusCode.ERROR_DB_DELETE_EXCEPTION;
                }
                return ModelStatusCode.STATUS_SUCCESS;
            } else {
                return ModelStatusCode.ERROR_BELONGS_TO_ANOTHER_USER;
            }
        } else {
            return ModelStatusCode.ERROR_CANNOT_FIND_ORDER;
        }
    }
}
