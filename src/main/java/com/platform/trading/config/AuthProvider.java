package com.platform.trading.config;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.platform.trading.sql.User;
import com.platform.trading.sql.UserRepository;

@Component
public class AuthProvider implements AuthenticationProvider {
	
	@Autowired
	private UserRepository usersDao;
	
	@Override
	public Authentication authenticate(Authentication auth) throws BadCredentialsException
	{
		String username = auth.getName();
		String password = auth.getCredentials().toString();
		User user = null;
		try
		{
			user = usersDao.findByEmail(username);
		}
		catch(Exception e)
		{
			throw new BadCredentialsException("Invalid username or password");
		}
		byte[] hash = new SHA256Hash().getSHA256Hash(password);
		if(hash != null && user != null)
		{
			if(username.equals(user.getEmail()) && Arrays.equals(user.getPassword(), hash))
			{
				List<GrantedAuthority> grantedAuth = new ArrayList<>();
				grantedAuth.add(new SimpleGrantedAuthority("ROLE_USER"));
				return new UsernamePasswordAuthenticationToken(username, password, grantedAuth);
			}
			else
			{
				throw new BadCredentialsException("Invalid username or password");
			}
		}
		else
		{
			throw new BadCredentialsException("Calculating hash error!");
		}
		
	}
	
	@Override
	public boolean supports(Class<?> authentication)
	{
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
}
