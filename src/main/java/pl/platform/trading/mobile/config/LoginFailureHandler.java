package pl.platform.trading.mobile.config;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import pl.platform.trading.mobile.http.response.login.LoginResponseFacade;
import pl.platform.trading.resolver.ModelStatusCode;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) {
        System.out.println("Login failed");
        System.out.println("exception: " + exception);
        // set response to failed
        if(exception.getClass().equals(BadCredentialsException.class)) {
            new LoginResponseFacade(response).writeResponse(HttpServletResponse.SC_OK,
                    ModelStatusCode.ERROR_BAD_CREDENTIALS);
        } else {
            new LoginResponseFacade(response).writeResponse(HttpServletResponse.SC_OK,
                    ModelStatusCode.ERROR_UNKNOWN_LOGIN_ERROR);
        }

    }
}
