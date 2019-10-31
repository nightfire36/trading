package com.platform.trading.rates;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class XchgRate {
	
	private String currencyPair;
	private BigDecimal ask, bid;
	private Timestamp timestamp;
	
	public XchgRate(String currencyPair)
	{
		this.currencyPair = currencyPair;
	}
	
	public String getCurrencyPair() {
		return currencyPair;
	}

	public BigDecimal getAsk() {
		return ask;
	}

	public void setAsk(BigDecimal ask) {
		this.ask = ask;
	}

	public BigDecimal getBid() {
		return bid;
	}

	public void setBid(BigDecimal bid) {
		this.bid = bid;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
}
