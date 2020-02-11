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

    public Long getTid() {
        return tid;
    }

    public void setTid(Long tid) {
        this.tid = tid;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getCurrencyPair() {
        return currencyPair;
    }

    public void setCurrencyPair(String currencyPair) {
        this.currencyPair = currencyPair;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getOpeningPrice() {
        return openingPrice;
    }

    public void setOpeningPrice(BigDecimal openingPrice) {
        this.openingPrice = openingPrice;
    }

    public BigDecimal getClosingPrice() {
        return closingPrice;
    }

    public void setClosingPrice(BigDecimal closingPrice) {
        this.closingPrice = closingPrice;
    }

    public Timestamp getOpeningTimestamp() {
        return openingTimestamp;
    }

    public void setOpeningTimestamp(Timestamp openingTimestamp) {
        this.openingTimestamp = openingTimestamp;
    }

    public BigDecimal getProfit() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }

    public boolean isLongPosition() {
        return longPosition;
    }

    public void setLongPosition(boolean longPosition) {
        this.longPosition = longPosition;
    }

    public Timestamp getClosingTimestamp() {
        return closingTimestamp;
    }
}
