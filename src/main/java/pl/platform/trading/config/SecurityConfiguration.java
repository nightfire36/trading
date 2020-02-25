package pl.platform.trading.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthProvider authProvider;

    @Override
    public void configure(HttpSecurity security) throws Exception {
        security
                .authorizeRequests()
                .antMatchers("/user/register", "/user/login")
                .permitAll()
                .and()
                .authorizeRequests()
                .antMatchers("/*", "/user/*", "/api/*")
                .fullyAuthenticated()
                .and()
                .formLogin()
                .loginPage("/user/login")
                .defaultSuccessUrl("/user/description", true)
                .and()
                .logout()
                .logoutUrl("/user/logout")
                .logoutSuccessUrl("/user/login?logoutSuccessful");
    }

    @Override
    public void configure(AuthenticationManagerBuilder authManager) throws Exception {
        authManager.authenticationProvider(authProvider);
    }
}
