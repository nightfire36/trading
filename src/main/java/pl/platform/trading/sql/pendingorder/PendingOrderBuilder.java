package pl.platform.trading.sql.pendingorder;

import java.math.BigDecimal;

public class PendingOrderBuilder {

    private Long uid;
    private Long tid;
    private String currencyPair;
    private BigDecimal amount;
    private BigDecimal orderPrice;
    private boolean longPosition;
    private boolean triggeredAbove;

    public PendingOrderBuilder setUid(Long uid) {
        this.uid = uid;
        return this;
    }

    public PendingOrderBuilder setTid(Long tid) {
        this.tid = tid;
        return this;
    }

    public PendingOrderBuilder setCurrencyPair(String currencyPair) {
        this.currencyPair = currencyPair;
        return this;
    }

    public PendingOrderBuilder setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public PendingOrderBuilder setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
        return this;
    }

    public PendingOrderBuilder setLongPosition(boolean longPosition) {
        this.longPosition = longPosition;
        return this;
    }

    public PendingOrderBuilder setTriggeredAbove(boolean triggeredAbove) {
        this.triggeredAbove = triggeredAbove;
        return this;
    }

    public PendingOrder build() {
        return new PendingOrder(uid, tid, currencyPair, amount, orderPrice, longPosition, triggeredAbove);
    }
}
