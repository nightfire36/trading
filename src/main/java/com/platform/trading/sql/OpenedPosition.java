package com.platform.trading.sql;

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

	public boolean isLongPosition() {
		return longPosition;
	}

	public void setLongPosition(boolean longPosition) {
		this.longPosition = longPosition;
	}

	public Long getTid() {
		return tid;
	}

	public Timestamp getOpeningTimestamp() {
		return openingTimestamp;
	}

	public BigDecimal getCurrentProfit() {
		return currentProfit;
	}

	public void setCurrentProfit(BigDecimal currentProfit) {
		this.currentProfit = currentProfit;
	}
}
