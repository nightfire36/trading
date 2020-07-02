package pl.platform.trading.mobile.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import pl.platform.trading.config.AuthProvider;

@EnableWebSecurity(debug = true)
@Configuration
@Order(1)
public class MobileSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthProvider authProvider;

    @Autowired
    private LoginSuccessHandler loginSuccessHandler;

    @Autowired
    private LoginFailureHandler loginFailureHandler;

    @Override
    public void configure(HttpSecurity security) throws Exception {
        security
                .antMatcher("/mobile/**")
                .authorizeRequests()
                    .antMatchers("/mobile/login", "/mobile/register")
                    .permitAll()
                .and()
                    .authorizeRequests()
                    .antMatchers("/mobile/**")
                    .fullyAuthenticated()
                .and()
                    .csrf()
                    .ignoringAntMatchers("/mobile/**")
                .and()
                    .formLogin()
                    .loginProcessingUrl("/mobile/login")
                .and()
                    .logout()
                    .logoutUrl("/mobile/logout")
                .and()
                    .addFilterBefore(jsonAuthFilterBean(), UsernamePasswordAuthenticationFilter.class)
                    .exceptionHandling()
                    .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
    }

    @Override
    public void configure(AuthenticationManagerBuilder authManager) throws Exception {
        authManager.authenticationProvider(authProvider);
    }

    @Bean
    public JsonAuthFilter jsonAuthFilterBean() throws Exception {
        JsonAuthFilter jsonAuthFilter = new JsonAuthFilter();

        jsonAuthFilter.setAuthenticationManager(super.authenticationManagerBean());
        jsonAuthFilter.setRequiresAuthenticationRequestMatcher(
                new AntPathRequestMatcher("/mobile/login", "POST")
        );

        jsonAuthFilter.setAuthenticationSuccessHandler(loginSuccessHandler);
        jsonAuthFilter.setAuthenticationFailureHandler(loginFailureHandler);

        return jsonAuthFilter;
    }
}
