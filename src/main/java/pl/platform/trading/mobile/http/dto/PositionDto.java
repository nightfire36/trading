package pl.platform.trading.mobile.http.dto;

import java.math.BigDecimal;

public class PositionDto {

    private String pair;
    private BigDecimal amount;

    public String getPair() {
        return pair;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
