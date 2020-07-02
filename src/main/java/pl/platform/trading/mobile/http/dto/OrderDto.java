package pl.platform.trading.mobile.http.dto;

import java.math.BigDecimal;

public class OrderDto {

    private String pair;
    private BigDecimal amount;
    private boolean trigger;
    private BigDecimal price;

    public String getPair() {
        return pair;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public boolean isTrigger() {
        return trigger;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
