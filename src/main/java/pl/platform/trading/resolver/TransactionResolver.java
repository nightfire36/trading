package pl.platform.trading.resolver;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import pl.platform.trading.rates.ExchangeRate;
import pl.platform.trading.rates.ExchnageRatesProvider;
import pl.platform.trading.sql.closedposition.ClosedPosition;
import pl.platform.trading.sql.closedposition.ClosedPositionBuilder;
import pl.platform.trading.sql.closedposition.ClosedPositionRepository;
import pl.platform.trading.sql.openedposition.OpenedPosition;
import pl.platform.trading.sql.openedposition.OpenedPositionBuilder;
import pl.platform.trading.sql.openedposition.OpenedPositionRepository;
import pl.platform.trading.sql.pendingorder.PendingOrder;
import pl.platform.trading.sql.pendingorder.PendingOrderRepository;
import pl.platform.trading.sql.user.User;
import pl.platform.trading.sql.user.UserRepository;

@Component
@Transactional
public class TransactionResolver {

    @Autowired
    private PendingOrderRepository pendingOrdersDao;

    @Autowired
    private OpenedPositionRepository openedPositionsDao;

    @Autowired
    private ClosedPositionRepository closedPositionsDao;

    @Autowired
    private UserRepository usersDao;

    @Autowired
    private ExchnageRatesProvider rates;

    public BigDecimal calculateProfit(OpenedPosition opened, ExchangeRate rate) {
        BigDecimal openingPrice = opened.getOpeningPrice();
        BigDecimal amount = opened.getAmount();

        if (opened.isLongPosition()) {
            BigDecimal currentPrice = rate.getBid();
            BigDecimal currentProfit = amount.multiply(currentPrice.divide(
                    openingPrice, 16, RoundingMode.HALF_UP)).setScale(5, RoundingMode.HALF_UP);

            return currentProfit.subtract(amount);
        } else {
            BigDecimal currentPrice = rate.getAsk();
            BigDecimal currentProfit = amount.multiply(openingPrice.divide(
                    currentPrice, 16, RoundingMode.HALF_UP)).setScale(5, RoundingMode.HALF_UP);

            return currentProfit.subtract(amount);
        }
    }

    public boolean updateAccountBalance(Long uid, BigDecimal updateAmount) {
        User user = null;
        try {
            user = usersDao.findByUid(uid);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        BigDecimal accountBalance = user.getAccountBalance().add(updateAmount);
        user.setAccountBalance(accountBalance);
        usersDao.save(user);

        return true;
    }

    public boolean closePosition(Long tid) {
        OpenedPosition opened = openedPositionsDao.findByTid(tid);

        if (opened != null) {
            BigDecimal closingPrice;

            BigDecimal transactionProfit = calculateProfit(opened, rates.getPairRate(opened.getCurrencyPair()));

            if (opened.isLongPosition()) {
                closingPrice = rates.getPairRate(opened.getCurrencyPair()).getBid();
            } else {
                closingPrice = rates.getPairRate(opened.getCurrencyPair()).getAsk();
            }

            ClosedPosition closed = new ClosedPositionBuilder()
                    .setTid(tid)
                    .setUid(opened.getUid())
                    .setCurrencyPair(opened.getCurrencyPair())
                    .setAmount(opened.getAmount())
                    .setOpeningPrice(opened.getOpeningPrice())
                    .setClosingPrice(closingPrice)
                    .setOpeningTimestamp(opened.getOpeningTimestamp())
                    .setProfit(transactionProfit)
                    .setLongPosition(opened.isLongPosition())
                    .build();

            closedPositionsDao.save(closed);

            // update user account balance
            updateAccountBalance(opened.getUid(), opened.getAmount().add(transactionProfit));

            openedPositionsDao.deleteByTid(tid);

            return true;
        } else {
            return false;
        }
    }

    public boolean openPosition(Long uid, String currencyPair, BigDecimal amount, boolean longPosition) {
        User user = usersDao.findByUid(uid);

        if (user.getAccountBalance().compareTo(amount) >= 0) {
            OpenedPositionBuilder openedBuilder = new OpenedPositionBuilder()
                    .setUid(uid)
                    .setCurrencyPair(currencyPair)
                    .setAmount(amount)
                    .setLongPosition(longPosition);

            if (longPosition) {
                openedBuilder.setOpeningPrice(rates.getPairRate(currencyPair).getAsk());
            } else {
                openedBuilder.setOpeningPrice(rates.getPairRate(currencyPair).getBid());
            }

            openedPositionsDao.save(openedBuilder.build());

            // update user amount of money
            updateAccountBalance(uid, amount.negate());

            return true;
        } else {
            return false;
        }
    }

    private void executeOrders(List<PendingOrder> orders) {
        for (PendingOrder order : orders) {
            if (order.getTid() != null) {
                try {
                    pendingOrdersDao.deleteByOid(order.getOid());
                    closePosition(order.getTid());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (order.getAmount() != null) {
                try {
                    pendingOrdersDao.deleteByOid(order.getOid());
                    openPosition(order.getUid(), order.getCurrencyPair(), order.getAmount(),
                            order.isLongPosition());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Bad order!");
            }
        }
    }

    public void resolve() {
        List<ExchangeRate> ratesList = rates.getRates();

        for (ExchangeRate rate : ratesList) {
            String currencyPair = rate.getCurrencyPair();
            BigDecimal bid = rate.getBid();
            BigDecimal ask = rate.getAsk();

            List<PendingOrder> positionsAboveLong = pendingOrdersDao
                    .findByCurrencyPairAndOrderPriceLessThanAndTriggeredAboveAndLongPosition(
                            currencyPair, ask, true, true);
            executeOrders(positionsAboveLong);

            List<PendingOrder> positionsAboveShort = pendingOrdersDao
                    .findByCurrencyPairAndOrderPriceLessThanAndTriggeredAboveAndLongPosition(
                            currencyPair, bid, true, false);
            executeOrders(positionsAboveShort);

            List<PendingOrder> positionsBelowLong = pendingOrdersDao
                    .findByCurrencyPairAndOrderPriceGreaterThanAndTriggeredAboveAndLongPosition(
                            currencyPair, ask, false, true);
            executeOrders(positionsBelowLong);

            List<PendingOrder> positionsBelowShort = pendingOrdersDao
                    .findByCurrencyPairAndOrderPriceGreaterThanAndTriggeredAboveAndLongPosition(
                            currencyPair, bid, false, false);
            executeOrders(positionsBelowShort);
        }
    }
}
