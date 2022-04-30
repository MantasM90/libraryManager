package com.company;

import com.company.enums.Role;

import static com.company.enums.Role.SIMPLE;

public class User {

    private String username;
    private String name;
    private String surname;
    private String password;
    private Role role;

    public User(String username, String name, String surname, String password) {
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.role = SIMPLE;
    }

    public User(String username, String name, String surname, String password, Role role) {
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.role = role;
    }


    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }
}
