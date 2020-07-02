package pl.platform.trading.mobile.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pl.platform.trading.mobile.http.dto.LoginDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

public class JsonAuthFilter extends UsernamePasswordAuthenticationFilter {

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) {
        try {
            System.out.println("Authentication filter");

            String requestBody = request.getReader().lines().collect(Collectors.joining());
            System.out.println(requestBody);
            if(requestBody != null && requestBody.length() > 0) {
                LoginDto loginDto = new ObjectMapper().readValue(requestBody, LoginDto.class);

                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(loginDto.getLogin(), loginDto.getPassword());
                return this.getAuthenticationManager().authenticate(authenticationToken);
            }
            else {
                throw new AuthenticationCredentialsNotFoundException("Request body not found");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new AuthenticationServiceException(e.getMessage());
        }
    }
}
