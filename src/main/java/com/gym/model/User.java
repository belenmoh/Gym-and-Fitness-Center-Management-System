package com.gym.model;

public abstract class User {
    private int id;
    private String name;
    private String username;
    private String password;
    private UserRole role;

    public User() {}

    public User(int id, String name, String username, String password, UserRole role) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.role = role;
    }
}
