package pl.platform.trading.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import pl.platform.trading.resolver.ModelStatusCode;
import pl.platform.trading.resolver.PlatformModel;

@Controller
@RequestMapping("/user")
public class MvcController {

    @Autowired
    PlatformModel platformModel;


    @GetMapping("/register")
    public String registerUserView() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("form") @Valid UserDto userDto, BindingResult result, Model model) {
        if (!result.hasErrors()) {

            int statusCode = platformModel.registerUser(userDto);

            if(statusCode == ModelStatusCode.STATUS_SUCCESS) {
                return "redirect:login?registrationSuccessful";
            } else if(statusCode == ModelStatusCode.ERROR_EMAIL_ALREADY_EXISTS) {
                model.addAttribute("message", 1);
                return "register";
            } else if(statusCode == ModelStatusCode.ERROR_DB_SAVE_EXCEPTION) {
                model.addAttribute("message", 2);
                return "register";
            } else {
                model.addAttribute("message", 2);
                return "register";
            }

        } else {
            List<FieldError> errorsList = result.getFieldErrors();
            model.addAttribute("message", 2);
            for (FieldError error : errorsList) {
                model.addAttribute(error.getField() + "_help",
                        "Wrong field value: " + error.getDefaultMessage());
            }
            return "register";
        }
    }

    @GetMapping("/login")
    public String loginView(ServletRequest request, Model model) {
        Map<String, String[]> param = request.getParameterMap();

        if (param.containsKey("error")) {
            model.addAttribute("message", 1);
        } else if (param.containsKey("registrationSuccessful")) {
            model.addAttribute("message", 2);
        } else if (param.containsKey("logoutSuccessful")) {
            model.addAttribute("message", 3);
        }
        return "login";
    }

    @GetMapping("/info")
    public ModelAndView userInfoView() {
        return new ModelAndView("userinfo")
                .addObject("userinfo", platformModel.userInfo());
    }

    @GetMapping("/description")
    public String descriptionView() {
        return "description";
    }

    @GetMapping("/opened_positions")
    public ModelAndView openedPositionsView() {
        return new ModelAndView("opened_positions")
                .addObject("opened", platformModel.getOpenedPositions());
    }

    @GetMapping("/closed_positions")
    public ModelAndView closedPositionsView() {
        return new ModelAndView("closed_positions")
                .addObject("closed", platformModel.getClosedPositions());
    }

    @GetMapping("/pending_orders")
    public ModelAndView pendingOrdersView() {
        return new ModelAndView("pending_orders")
                .addObject("pending", platformModel.getPendingOrders());
    }

    @GetMapping("/trade")
    public String tradeView() {
        return "trade";
    }
}
