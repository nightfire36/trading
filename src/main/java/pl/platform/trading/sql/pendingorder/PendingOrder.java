package pl.platform.trading.sql.pendingorder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@Table(name = "pending_orders")
@EntityListeners(AuditingEntityListener.class)
public class PendingOrder implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long oid;

    private Long uid;

    private Long tid;

    @Column(name = "currency_pair")
    private String currencyPair;

    private BigDecimal amount;

    @Column(name = "order_price")
    private BigDecimal orderPrice;

    @Column(name = "order_timestamp", insertable = false)
    private Timestamp orderTimestamp;

    @Column(name = "long_position")
    private boolean longPosition;

    @Column(name = "triggered_above")
    private boolean triggeredAbove;

    public PendingOrder() {
    }

    public PendingOrder(Long uid, Long tid, String currencyPair, BigDecimal amount,
                        BigDecimal orderPrice, boolean longPosition, boolean triggeredAbove) {
        this.uid = uid;
        this.tid = tid;
        this.currencyPair = currencyPair;
        this.amount = amount;
        this.orderPrice = orderPrice;
        this.longPosition = longPosition;
        this.triggeredAbove = triggeredAbove;
    }

    public Long getOid() {
        return oid;
    }

    public Long getUid() {
        return uid;
    }

    public Long getTid() {
        return tid;
    }

    public String getCurrencyPair() {
        return currencyPair;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public Timestamp getOrderTimestamp() {
        return orderTimestamp;
    }

    public boolean isLongPosition() {
        return longPosition;
    }

    public boolean isTriggeredAbove() {
        return triggeredAbove;
    }
}
