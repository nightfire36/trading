package pl.platform.trading.controller;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class UserDto {

    @NotEmpty
    @Size(min = 3, max = 20)
    private String firstName;

    @NotEmpty
    @Size(min = 3, max = 20)
    private String lastName;

    @NotEmpty
    @Email
    @Size(min = 3, max = 30)
    private String email;

    @NotEmpty
    @Size(min = 6, max = 30)
    private String password;

    public UserDto() {
    }

    public UserDto(String first_name, String last_name, String email, String password) {
        this.firstName = first_name;
        this.lastName = last_name;
        this.email = email;
        this.password = password;
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

    public String getPassword() {
        return password;
    }
}
