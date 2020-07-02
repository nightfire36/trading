package pl.platform.trading.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.platform.trading.rates.ExchangeRate;
import pl.platform.trading.resolver.PlatformModel;
import pl.platform.trading.resolver.ModelStatusCode;
import pl.platform.trading.sql.openedposition.OpenedPosition;
import pl.platform.trading.sql.pendingorder.PendingOrder;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private PlatformModel platformModel;


    @GetMapping("/rates")
    public List<ExchangeRate> getRates() {
        return platformModel.getRates();
    }

    @GetMapping("/rate/{pair}")
    public ExchangeRate getPairRate(@PathVariable("pair") String pair) {
        return platformModel.getPairRate(pair);
    }

    @GetMapping("/opened_positions")
    public List<OpenedPosition> openedPositions() {
        return platformModel.getOpenedPositions();
    }

    @GetMapping("/pending_orders")
    public List<PendingOrder> pendingOrders() {
        return platformModel.getPendingOrders();
    }

    @PostMapping("/long/{pair}/{amount}")
    public String longPosition(@PathVariable("pair") String pair,
                               @PathVariable("amount") BigDecimal amount) {

        if (platformModel.takeLongPosition(pair, amount) == ModelStatusCode.STATUS_SUCCESS) {
            return "Success";
        } else {
            return "Error: not enough money";
        }
    }

    @PostMapping("/short/{pair}/{amount}")
    public String shortPosition(@PathVariable("pair") String pair,
                                @PathVariable("amount") BigDecimal amount) {

        if (platformModel.takeShortPosition(pair, amount) == ModelStatusCode.STATUS_SUCCESS) {
            return "Success";
        } else {
            return "Error: not enough money";
        }
    }

    @PostMapping("/order_long/{pair}/{amount}/{trigger}/{price}")
    public String orderLong(@PathVariable("pair") String pair,
                            @PathVariable("amount") BigDecimal amount,
                            @PathVariable("trigger") boolean trigger,
                            @PathVariable("price") BigDecimal price) {

        if(platformModel.placeOrderForLongPosition(pair, amount, trigger, price) == ModelStatusCode.STATUS_SUCCESS) {
            return "success";
        } else {
            return "unknown error";
        }
    }

    @PostMapping("/order_short/{pair}/{amount}/{trigger}/{price}")
    public String orderShort(@PathVariable("pair") String pair,
                             @PathVariable("amount") BigDecimal amount,
                             @PathVariable("trigger") boolean trigger,
                             @PathVariable("price") BigDecimal price) {

        if(platformModel.placeOrderForShortPosition(pair, amount, trigger, price) == ModelStatusCode.STATUS_SUCCESS) {
            return "success";
        } else {
            return "unknown error";
        }
    }

    @PostMapping("/close/{tid}")
    public String closePosition(@PathVariable("tid") Long tid) {

        int statusCode = platformModel.closePosition(tid);

        if (statusCode == ModelStatusCode.STATUS_SUCCESS) {
            return "Position closed successfully";
        } else if(statusCode == ModelStatusCode.ERROR_BELONGS_TO_ANOTHER_USER) {
            return "Position belongs to another user";
        } else {
            return "Position closing failure";
        }
    }

    @PostMapping("/order_closure/{tid}/{trigger}/{price}")
    public String orderClosure(@PathVariable("tid") Long tid,
                               @PathVariable("trigger") boolean trigger,
                               @PathVariable("price") BigDecimal price) {

        int statusCode = platformModel.placeOrderForPositionClosure(tid, trigger, price);

        if(statusCode == ModelStatusCode.STATUS_SUCCESS) {
            return "Order closed successfully";
        } else if(statusCode == ModelStatusCode.ERROR_BELONGS_TO_ANOTHER_USER) {
            return "Position belongs to another user";
        } else {
            return "Order placing failure";
        }
    }

    @PostMapping("/cancel/{oid}")
    public String cancelOrder(@PathVariable("oid") Long oid) {

        int statusCode = platformModel.cancelOrder(oid);

        if(statusCode == ModelStatusCode.STATUS_SUCCESS) {
            return "Order cancelled successfully";
        } else if(statusCode == ModelStatusCode.ERROR_BELONGS_TO_ANOTHER_USER) {
            return "Order belongs to another user";
        } else {
            return "Order closing failure";
        }
    }
}
