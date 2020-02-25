package pl.platform.trading.sql.openedposition;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "opened_positions")
@EntityListeners(AuditingEntityListener.class)
public class OpenedPosition implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tid;

    private Long uid;

    @Column(name = "currency_pair")
    private String currencyPair;

    private BigDecimal amount;

    @Column(name = "opening_price")
    private BigDecimal openingPrice;

    @Column(name = "opening_timestamp", insertable = false)
    private Timestamp openingTimestamp;

    @Column(name = "long_position")
    private boolean longPosition;

    @Transient
    private BigDecimal currentProfit;

    public OpenedPosition() {
    }

    public OpenedPosition(Long uid, String currencyPair, BigDecimal amount,
                          BigDecimal openingPrice, boolean longPosition) {
        this.uid = uid;
        this.currencyPair = currencyPair;
        this.amount = amount;
        this.openingPrice = openingPrice;
        this.longPosition = longPosition;
    }

    public void setCurrentProfit(BigDecimal currentProfit) {
        this.currentProfit = currentProfit;
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

    public Timestamp getOpeningTimestamp() {
        return openingTimestamp;
    }

    public boolean isLongPosition() {
        return longPosition;
    }

    public BigDecimal getCurrentProfit() {
        return currentProfit;
    }
}
