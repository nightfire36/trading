package pl.platform.trading.mobile.http.response.account;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class AccountInfo {

    private String firstName;
    private String lastName;
    private String email;
    private Timestamp createdAt;
    private BigDecimal accountBalance;

    public AccountInfo(String firstName, String lastName, String email, Timestamp createdAt, BigDecimal accountBalance) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.createdAt = createdAt;
        this.accountBalance = accountBalance;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public BigDecimal getAccountBalance() {
        return accountBalance;
    }
}
