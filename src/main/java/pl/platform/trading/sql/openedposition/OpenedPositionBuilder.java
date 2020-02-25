package pl.platform.trading.sql.openedposition;

import java.math.BigDecimal;

public class OpenedPositionBuilder {

    private Long uid;
    private String currencyPair;
    private BigDecimal amount;
    private BigDecimal openingPrice;
    private boolean longPosition;

    public OpenedPositionBuilder setUid(Long uid) {
        this.uid = uid;
        return this;
    }

    public OpenedPositionBuilder setCurrencyPair(String currencyPair) {
        this.currencyPair = currencyPair;
        return this;
    }

    public OpenedPositionBuilder setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public OpenedPositionBuilder setOpeningPrice(BigDecimal openingPrice) {
        this.openingPrice = openingPrice;
        return this;
    }

    public OpenedPositionBuilder setLongPosition(boolean longPosition) {
        this.longPosition = longPosition;
        return this;
    }

    public OpenedPosition build() {
        return new OpenedPosition(uid, currencyPair, amount, openingPrice, longPosition);
    }
}
