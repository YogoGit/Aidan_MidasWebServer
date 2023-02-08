package com.midaswebserver.midasweb.models;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "USER_DATA")
public class Login {
    private static final long serialVersionUID = 1L;
    public Login() {
    }

    public Login(String username, String rawPassword) {
        this.username = username;
        setRawPassword(rawPassword);
    }
    public void setRawPassword(String rawPassword) {
        // XXX - This should *NEVER* be done in a real project
        this.hashedPassword = Integer.toString(rawPassword.hashCode());// im going to assume the negetive part of this action is hashing
        //with the java hash and doing it in a way that isnt salted
    }
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "username", nullable = false, unique = true)//these parts on the end are for data verification
    private String username;

    @Column(name = "password", nullable = false)
    private String hashedPassword;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    private static final String EOL = System.lineSeparator();
    private static final String TAB = "\t";

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Login @ ").append(super.toString()).append("[").append(EOL);
        builder.append(TAB).append("username=").append(username).append(EOL);
        builder.append(TAB).append("hashedPassword=").append("****").append(EOL);
        builder.append("]").append(EOL);
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        final Login login = (Login)o;
        return username.equals(login.username) && hashedPassword.equals(login.hashedPassword);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, hashedPassword);
    }
}
