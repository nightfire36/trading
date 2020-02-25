package pl.platform.trading.sql.closedposition;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.*;
import java.math.BigDecimal;
import java.sql.Timestamp;


@Entity
@Table(name = "closed_positions")
@EntityListeners(AuditingEntityListener.class)
public class ClosedPosition implements Serializable {

    @Id
    private Long tid;

    private Long uid;

    @Column(name = "currency_pair")
    private String currencyPair;

    private BigDecimal amount;

    @Column(name = "opening_price")
    private BigDecimal openingPrice;

    @Column(name = "closing_price")
    private BigDecimal closingPrice;

    @Column(name = "opening_timestamp")
    private Timestamp openingTimestamp;

    @Column(name = "closing_timestamp", insertable = false)
    private Timestamp closingTimestamp;

    private BigDecimal profit;

    @Column(name = "long_position")
    private boolean longPosition;

    public ClosedPosition() {
    }

    public ClosedPosition(Long tid, Long uid, String currencyPair, BigDecimal amount,
                          BigDecimal openingPrice, BigDecimal closingPrice, Timestamp openingTimestamp,
                          BigDecimal profit, boolean longPosition) {
        this.tid = tid;
        this.uid = uid;
        this.currencyPair = currencyPair;
        this.amount = amount;
        this.openingPrice = openingPrice;
        this.closingPrice = closingPrice;
        this.openingTimestamp = openingTimestamp;
        this.profit = profit;
        this.longPosition = longPosition;
    }

    public Long getTid() {
        return tid;
    }

    public Long getUid() {
        return uid;
    }

    public String getCurrencyPair() {
        return currencyPair;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getOpeningPrice() {
        return openingPrice;
    }

    public BigDecimal getClosingPrice() {
        return closingPrice;
    }

    public Timestamp getOpeningTimestamp() {
        return openingTimestamp;
    }

    public Timestamp getClosingTimestamp() {
        return closingTimestamp;
    }

    public BigDecimal getProfit() {
        return profit;
    }

    public boolean isLongPosition() {
        return longPosition;
    }
}
