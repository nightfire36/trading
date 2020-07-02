package pl.platform.trading.mobile.http.response.account;

import pl.platform.trading.sql.user.User;

public class AccountInfoPrototype {

    private User user;

    public AccountInfoPrototype(User user) {
        this.user = user;
    }

    public AccountInfo cloneFromUser() {
        return new AccountInfo(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getAccountBalance()
        );
    }
}
