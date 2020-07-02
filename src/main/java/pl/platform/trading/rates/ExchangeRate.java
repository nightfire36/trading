package pl.platform.trading.rates;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(allowSetters = true, value = {"Name", "Bid", "Ask", "Min", "Max", "Time", "ChartDirection"})
public class ExchangeRate {
	
	@JsonProperty("Name")
	private String currencyPair;
	
	@JsonProperty("Bid")
	private BigDecimal bid;
	
	@JsonProperty("Ask")
	private BigDecimal ask;
	
	@JsonProperty("Min")
	private BigDecimal min;
	
	@JsonProperty("Max")
	private BigDecimal max;
	
	@JsonProperty("Time")
	private Timestamp timestamp;
	
	@JsonProperty("ChartDirection")
	private int chartDirection;


	@JsonGetter("currencyPair")
	public String getCurrencyPair() {
		return currencyPair;
	}

	@JsonGetter("bid")
	public BigDecimal getBid() {
		return bid;
	}

	@JsonGetter("ask")
	public BigDecimal getAsk() {
		return ask;
	}

	public BigDecimal getMin() {
		return min;
	}

	public BigDecimal getMax() {
		return max;
	}

	@JsonGetter("timestamp")
	public Timestamp getTimestamp() {
		return timestamp;
	}

	public int getChartDirection() {
		return chartDirection;
	}
}
