package pl.platform.trading.mobile.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.platform.trading.controller.UserDto;
import pl.platform.trading.mobile.http.dto.*;
import pl.platform.trading.mobile.http.response.ResponseStatus;
import pl.platform.trading.mobile.http.response.account.AccountInfo;
import pl.platform.trading.mobile.http.response.account.AccountInfoPrototype;
import pl.platform.trading.mobile.http.response.register.InvalidFieldCode;
import pl.platform.trading.mobile.http.response.register.RegisterResponse;
import pl.platform.trading.rates.ExchangeRate;
import pl.platform.trading.resolver.ModelStatusCode;
import pl.platform.trading.resolver.PlatformModel;
import pl.platform.trading.sql.closedposition.ClosedPosition;
import pl.platform.trading.sql.openedposition.OpenedPosition;
import pl.platform.trading.sql.pendingorder.PendingOrder;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/mobile")
public class MobileController {

    @Autowired
    private PlatformModel platformModel;


    @PostMapping("/login_check")
    public ResponseStatus loginCheck() {
        return new ResponseStatus(ModelStatusCode.STATUS_SUCCESS);
    }

    @PostMapping("/register")
    public RegisterResponse register(@RequestBody @Valid UserDto userDto, BindingResult result) {
        if(!result.hasErrors()) {
            return new RegisterResponse(platformModel.registerUser(userDto), 0);
        }
        else {
            int invalidFields = 0;
            List<FieldError> errorsList = result.getFieldErrors();
            for (FieldError error : errorsList) {
                // bitmask
                switch (error.getField()) {
                    case "firstName":
                        invalidFields |= InvalidFieldCode.INVALID_FIRST_NAME;
                        break;
                    case "lastName":
                        invalidFields |= InvalidFieldCode.INVALID_LAST_NAME;
                        break;
                    case "email":
                        invalidFields |= InvalidFieldCode.INVALID_EMAIL;
                        break;
                    case "password":
                        invalidFields |= InvalidFieldCode.INVALID_PASSWORD;
                        break;
                }
            }
            return new RegisterResponse(ModelStatusCode.ERROR_INVALID_FIELD_VALUE, invalidFields);
        }
    }

    @PostMapping("/account_info")
    public AccountInfo getAccountInfo() {
        return new AccountInfoPrototype(platformModel.userInfo())
                .cloneFromUser();
    }

    @PostMapping("/rates")
    public List<ExchangeRate> getRates() {
        return platformModel.getRates();
    }

    @PostMapping("/closed_positions")
    public List<ClosedPosition> getClosedPositions() {
        return platformModel.getClosedPositions();
    }

    @PostMapping("/opened_positions")
    public List<OpenedPosition> getOpenedPositions() {
        return platformModel.getOpenedPositions();
    }

    @PostMapping("/pending_orders")
    public List<PendingOrder> getPendingOrders() {
        return platformModel.getPendingOrders();
    }

    @PostMapping("/long_position")
    public ResponseStatus takeLongPosition(@RequestBody PositionDto positionDto) {
        return new ResponseStatus(
                platformModel.takeLongPosition(
                        positionDto.getPair(),
                        positionDto.getAmount()));
    }

    @PostMapping("/short_position")
    public ResponseStatus takeShortPosition(@RequestBody PositionDto positionDto) {
        return new ResponseStatus(
                platformModel.takeShortPosition(
                        positionDto.getPair(),
                        positionDto.getAmount()));
    }

    @PostMapping("/order_long")
    public ResponseStatus makeOrderLong(@RequestBody OrderDto orderDto) {
        return new ResponseStatus(
                platformModel.placeOrderForLongPosition(
                        orderDto.getPair(),
                        orderDto.getAmount(),
                        orderDto.isTrigger(),
                        orderDto.getPrice()));
    }

    @PostMapping("/order_short")
    public ResponseStatus makeOrderShort(@RequestBody OrderDto orderDto) {
        return new ResponseStatus(
                platformModel.placeOrderForShortPosition(
                        orderDto.getPair(),
                        orderDto.getAmount(),
                        orderDto.isTrigger(),
                        orderDto.getPrice()));
    }

    @PostMapping("/close")
    public ResponseStatus closePosition(@RequestBody IdDto idDto) {
        return new ResponseStatus(
                platformModel.closePosition(idDto.getId()));
    }

    @PostMapping("/order_closure")
    public ResponseStatus orderClosure(@RequestBody OrderClosureDto orderClosureDto) {
        return new ResponseStatus(
                platformModel.placeOrderForPositionClosure(
                        orderClosureDto.getTid(),
                        orderClosureDto.isTrigger(),
                        orderClosureDto.getPrice()));
    }

    @PostMapping("/cancel_order")
    public ResponseStatus cancelOrder(@RequestBody IdDto idDto) {
        return new ResponseStatus(
                platformModel.cancelOrder(idDto.getId()));
    }
}
