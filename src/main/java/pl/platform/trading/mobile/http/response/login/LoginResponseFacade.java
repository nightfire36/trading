package pl.platform.trading.mobile.http.response.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import pl.platform.trading.mobile.http.response.ResponseStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginResponseFacade {

    private HttpServletResponse response;

    public LoginResponseFacade(HttpServletResponse response) {
        this.response = response;
    }

    public boolean writeResponse(int httpCode, int responseCode) {
        response.setStatus(httpCode);

        try {
            response.getWriter().write(
                    new ObjectMapper().writeValueAsString(
                            new ResponseStatus(responseCode))
            );
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
