package com.platform.trading.controller;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class NewUserDTO {
	
	@NotEmpty
	@Size(min=3, max=20)
	private String first_name;
	
	@NotEmpty
	@Size(min=3, max=20)
	private String last_name;
	
	@NotEmpty
	@Email
	@Size(min=3, max=30)
	private String email;
	
	@NotEmpty
	@Size(min=6, max=30)
	private String password;

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
