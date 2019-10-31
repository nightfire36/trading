package com.platform.trading.sql;

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

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public Long getTid() {
		return tid;
	}

	public void setTid(Long tid) {
		this.tid = tid;
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

	public BigDecimal getOrderPrice() {
		return orderPrice;
	}

	public void setOrderPrice(BigDecimal orderPrice) {
		this.orderPrice = orderPrice;
	}

	public boolean isLongPosition() {
		return longPosition;
	}

	public void setLongPosition(boolean longPosition) {
		this.longPosition = longPosition;
	}

	public Long getOid() {
		return oid;
	}

	public Timestamp getOrderTimestamp() {
		return orderTimestamp;
	}

	public boolean isTriggeredAbove() {
		return triggeredAbove;
	}

	public void setTriggeredAbove(boolean triggeredAbove) {
		this.triggeredAbove = triggeredAbove;
	}
}
