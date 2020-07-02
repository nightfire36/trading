package pl.platform.trading.mobile.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import pl.platform.trading.mobile.http.response.login.LoginResponseFacade;
import pl.platform.trading.resolver.ModelStatusCode;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) {
        System.out.println("Login successful");
        clearAuthenticationAttributes(request);

        new LoginResponseFacade(response).writeResponse(HttpServletResponse.SC_OK,
                ModelStatusCode.STATUS_SUCCESS);
    }
}
