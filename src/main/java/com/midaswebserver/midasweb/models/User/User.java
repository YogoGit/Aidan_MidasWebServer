package com.midaswebserver.midasweb.models.User;

import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


/**
 * @version 0.0.1
 * User is the model object holding basic user data like username, hashedPassword, email ect.
 * @Author Aidan Scott
 * @since 0.0.1
 */
@Entity
@Table(name = "app_user")
public class User {
    private static final long serialVersionUID = 1L;//this is what identifies the data
    private static final String EOL = System.lineSeparator();
    private static final String TAB = "\t";
    @Id//primary key!! this is what makes it primary! (can be composite)
    @GeneratedValue
    private Long id;//must be an Integer so the ID isnt 0 if the object hasnt been set (should use Long)
    @Column(name = "username", nullable = false, unique = true)//these parts on the end are for data verification
    private String username;
    @Column(name = "password", nullable = false)
    private String hashedPassword;
    @Column(name = "email", nullable = true)
    private String email;
    @Column(name = "phonenumber", nullable = true)
    private String phoneNumber;
    @OneToMany(mappedBy = "user")//this is bad practice, you WILL change it back.
    @Cascade({org.hibernate.annotations.CascadeType.MERGE, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    private Set<Symbol> symbols = new HashSet<>();

    public User() {
    }

    /**
     * @param username
     * @param hashedPassword password must be hashed before given.
     * @Author Aidan Scott
     * @since 0.0.1
     */
    public User(String username, String hashedPassword) {
        this.username = username;
        this.hashedPassword = hashedPassword;
    }

    /**
     * @param username
     * @param hashedPassword password must be hashed before given.
     * @param email
     * @param phoneNumber    maximum length of phone number is 15
     * @Author Aidan Scott
     * @since 0.0.1
     */
    public User(String username, String hashedPassword, String email, String phoneNumber) {
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public Set<Symbol> getSymbol() {
        return symbols;
    }

    public void setSetting(Set<Symbol> symbols) {
        this.symbols = symbols;
    }

    public void addSymbol(Symbol symbol) {
        this.symbols.add(symbol);
    }

    /**
     * @param ticker example: "EUC"
     * @param user   {@link User}
     * @Author Aidan Scott
     * @version 0.0.1
     * adds symbols to user
     * @since 0.0.1
     */
    public void addTicker(String ticker, User user) {
        Symbol symbol = new Symbol(ticker);
        symbol.setUserId(user);
        this.symbols.add(symbol);
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    @Override
    public String toString() {
        String builder = "Login @ " + super.toString() + "[" + EOL +
                TAB + "username=" + username + EOL +
                TAB + "hashedPassword=" + "****" + EOL +
                "]" + EOL;
        return builder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        final User login = (User) o;
        return username.equals(login.username) && hashedPassword.equals(login.hashedPassword);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, hashedPassword);
    }

}
