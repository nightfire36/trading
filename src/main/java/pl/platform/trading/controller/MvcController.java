package pl.platform.trading.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import pl.platform.trading.sql.closedposition.ClosedPosition;
import pl.platform.trading.sql.closedposition.ClosedPositionRepository;
import pl.platform.trading.sql.openedposition.OpenedPosition;
import pl.platform.trading.sql.openedposition.OpenedPositionRepository;
import pl.platform.trading.sql.pendingorder.PendingOrder;
import pl.platform.trading.sql.pendingorder.PendingOrderRepository;
import pl.platform.trading.sql.user.User;
import pl.platform.trading.sql.user.UserPrototype;
import pl.platform.trading.sql.user.UserRepository;

@Controller
@RequestMapping("/user")
public class MvcController {

    @Autowired
    private UserRepository usersDao;

    @Autowired
    private OpenedPositionRepository openedPositionsDao;

    @Autowired
    private ClosedPositionRepository closedPositionsDao;

    @Autowired
    private PendingOrderRepository pendingOrdersDao;

    @GetMapping("/register")
    public String registerUserView() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("form") @Valid UserDto userDto,
                               BindingResult result, Model model) {

        if (!result.hasErrors()) {
            User user = new UserPrototype().cloneFromUserDto(userDto);

            if (user != null) {
                try {
                    usersDao.save(user);
                } catch (DataIntegrityViolationException e) {
                    e.printStackTrace();
                    model.addAttribute("message", "<font color=\"red\">Registration failed! "
                            + "Account with provided <br /> e-mail address already exists.</font>");
                    return "register";
                } catch (Exception e) {
                    e.printStackTrace();
                    model.addAttribute("message", "<font color=\"red\">Registration failed!</font>");
                    return "register";
                }

                return "redirect:login?registrationSuccessful";
            } else {
                model.addAttribute("message", "<font color=\"red\">Registration failed!</font>");
                return "register";
            }
        } else {
            List<FieldError> errorsList = result.getFieldErrors();
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
            model.addAttribute("message", "<font color=\"red\">Invalid username or password!</font>");
        } else if (param.containsKey("registrationSuccessful")) {
            model.addAttribute("message", "<font color=\"green\">Registration successful.<br />"
                    + " Now you can log in on your account.</font>");
        } else if (param.containsKey("logoutSuccessful")) {
            model.addAttribute("message", "<font color=\"green\">Logout successful.</font>");
        }

        return "login";
    }

    public User initUser(String userEmail) {
        User user;
        try {
            user = usersDao.findByEmail(userEmail);
        } catch (Exception e) {
            return null;
        }
        return user;
    }

    @GetMapping("/info")
    public ModelAndView userInfoView(Authentication auth) {
        User user = initUser(auth.getName());

        ModelAndView model = new ModelAndView("userinfo");
        model.addObject("userinfo", user);
        return model;
    }

    @GetMapping("/description")
    public String descriptionView() {
        return "description";
    }

    @GetMapping("/opened_positions")
    public ModelAndView openedPositionsView(Authentication auth) {
        ModelAndView model = new ModelAndView("opened_positions");
        User user = initUser(auth.getName());

        List<OpenedPosition> opened = openedPositionsDao.findByUid(user.getUid());
        if (opened != null) model.addObject("opened", opened);
        return model;
    }

    @GetMapping("/closed_positions")
    public ModelAndView closedPositionsView(Authentication auth) {
        ModelAndView model = new ModelAndView("closed_positions");
        User user = initUser(auth.getName());

        List<ClosedPosition> closed = closedPositionsDao.findByUid(user.getUid());
        if (closed != null) model.addObject("closed", closed);
        return model;
    }

    @GetMapping("/pending_orders")
    public ModelAndView pendingOrdersView(Authentication auth) {
        ModelAndView model = new ModelAndView("pending_orders");
        User user = initUser(auth.getName());

        List<PendingOrder> pending = pendingOrdersDao.findByUid(user.getUid());
        if (pending != null) model.addObject("pending", pending);
        return model;
    }

    @GetMapping("/trade")
    public String tradeView() {
        return "trade";
    }
}
