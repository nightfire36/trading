package pl.platform.trading.mobile.http.dto;

import java.math.BigDecimal;

public class OrderClosureDto {

    private Long tid;
    private boolean trigger;
    private BigDecimal price;

    public Long getTid() {
        return tid;
    }

    public boolean isTrigger() {
        return trigger;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
