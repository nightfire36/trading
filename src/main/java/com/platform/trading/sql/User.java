package com.platform.trading.sql;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
public class User implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long uid;
	
	@Column(name = "first_name")
	@NotBlank
	private String firstName;
	
	@Column(name = "last_name")
	@NotBlank
	private String lastName;
	
	@NotBlank
	private String email;
	
	private byte[] password;
	
	@Column(name = "created_at", insertable = false)
	private Timestamp createdAt;
	
	@Column(name = "account_balance")
	private BigDecimal accountBalance;
	
	private int status;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public byte[] getPassword() {
		return password;
	}

	public void setPassword(byte[] password) {
		this.password = password;
	}

	public BigDecimal getAccountBalance() {
		return accountBalance;
	}

	public void setAccountBalance(BigDecimal accountBalance) {
		this.accountBalance = accountBalance;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Long getUid() {
		return uid;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}
}
