package com.midaswebserver.midasweb.forms;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * @version 0.0.1
 * UserForm contains user from the client
 * @Author Aidan Scott
 * @since 0.0.1
 */
public class UserForm {

    @NotNull(message = "Please enter a username")
    @NotBlank(message = "Please enter a username")
    @Size(min = 6, max = 20, message = "Username must be at least 6 characters long")
    private String username;

    @NotNull(message = "Please enter a password")
    @NotBlank(message = "Please enter a password")
    @Size(min = 8, max = 30, message = "Password must be at least 8 characters long")
    private String password;

    @NotNull(message = "Please repeat password")
    @NotBlank(message = "Please repeat password")
    @Size(min = 8, max = 30, message = "Passwords must be the same")
    private String confirmPass;
    @NotNull(message = "Please enter an email")
    @NotBlank(message = "Please enter an email")
    @Email(message = "That email was found to be invalid")
    private String email;

    @NotNull(message = "Please enter an phone number")
    @NotBlank(message = "Please enter an phone number")
    @Size(min = 10, max = 15, message = "Phone number to be too long/short")
    private String phoneNumber;

    public UserForm() {
    }

    //this is a bean used for validating incoming data
    public UserForm(String username, String password, String confirmPass, String email, String phoneNumber) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.confirmPass = confirmPass;
    }

    public String getConfirmPass() {
        return confirmPass;
    }

    public void setConfirmPass(String confirmPass) {
        this.confirmPass = confirmPass;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
