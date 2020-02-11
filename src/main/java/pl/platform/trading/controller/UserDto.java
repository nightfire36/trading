package pl.platform.trading.controller;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class UserDto {

    @NotEmpty
    @Size(min = 3, max = 20)
    private String first_name;

    @NotEmpty
    @Size(min = 3, max = 20)
    private String last_name;

    @NotEmpty
    @Email
    @Size(min = 3, max = 30)
    private String email;

    @NotEmpty
    @Size(min = 6, max = 30)
    private String password;

    public String getFirstName() {
        return first_name;
    }

    public String getLastName() {
        return last_name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

}
