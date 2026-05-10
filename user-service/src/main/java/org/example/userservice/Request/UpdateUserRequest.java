package org.example.userservice.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UpdateUserRequest {

    @Email(message = "Email must be valid")
    @Size(max = 255, message = "Email must be less than 255 characters")
    private String email;

    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    private String password;

    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    @Pattern(
            regexp = "^[A-Za-zА-Яа-я\\-\\s]+$",
            message = "First name contains invalid characters"
    )
    private String firstName;

    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    @Pattern(
            regexp = "^[A-Za-zА-Яа-я\\-\\s]+$",
            message = "Last name contains invalid characters"
    )
    private String lastName;

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
}
