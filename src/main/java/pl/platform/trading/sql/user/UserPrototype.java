package pl.platform.trading.sql.user;

import pl.platform.trading.config.Sha256Hash;
import pl.platform.trading.controller.UserDto;

import java.math.BigDecimal;

public class UserPrototype {

    private UserDto userDto;

    public UserPrototype(UserDto userDto) {
        this.userDto = userDto;
    }

    public User cloneFromUserDto() {
        byte[] hash = new Sha256Hash().getSHA256Hash(userDto.getPassword());

        if (hash != null) {
            User user = new User();

            user.setFirstName(userDto.getFirstName());
            user.setLastName(userDto.getLastName());
            user.setEmail(userDto.getEmail());
            user.setPassword(hash);
            user.setAccountBalance(new BigDecimal(100000.0));
            user.setStatus(0);

            return user;
        }
        return null;
    }
}
