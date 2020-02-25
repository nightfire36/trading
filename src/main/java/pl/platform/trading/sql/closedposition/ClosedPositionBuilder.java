package pl.platform.trading.sql.closedposition;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class ClosedPositionBuilder {

    private Long tid;
    private Long uid;
    private String currencyPair;
    private BigDecimal amount;
    private BigDecimal openingPrice;
    private BigDecimal closingPrice;
    private Timestamp openingTimestamp;
    private BigDecimal profit;
    private boolean longPosition;

    public ClosedPositionBuilder setTid(Long tid) {
        this.tid = tid;
        return this;
    }

    public ClosedPositionBuilder setUid(Long uid) {
        this.uid = uid;
        return this;
    }

    public ClosedPositionBuilder setCurrencyPair(String currencyPair) {
        this.currencyPair = currencyPair;
        return this;
    }

    public ClosedPositionBuilder setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public ClosedPositionBuilder setOpeningPrice(BigDecimal openingPrice) {
        this.openingPrice = openingPrice;
        return this;
    }

    public ClosedPositionBuilder setClosingPrice(BigDecimal closingPrice) {
        this.closingPrice = closingPrice;
        return this;
    }

    public ClosedPositionBuilder setOpeningTimestamp(Timestamp openingTimestamp) {
        this.openingTimestamp = openingTimestamp;
        return this;
    }

    public ClosedPositionBuilder setProfit(BigDecimal profit) {
        this.profit = profit;
        return this;
    }

    public ClosedPositionBuilder setLongPosition(boolean longPosition) {
        this.longPosition = longPosition;
        return this;
    }

    public ClosedPosition build() {
        return new ClosedPosition(tid, uid, currencyPair, amount, openingPrice, closingPrice,
                openingTimestamp, profit, longPosition);
    }
}
