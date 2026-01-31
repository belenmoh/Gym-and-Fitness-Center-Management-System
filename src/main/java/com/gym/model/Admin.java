package com.gym.model;

public class Admin extends User{
    private int adminId;

    public Admin() {
        super();
        setRole(UserRole.ADMIN);
    }

    public Admin(int id, String name, String username, String password, int adminId) {
        super(id, name, username, password, UserRole.ADMIN);
        this.adminId = adminId;
    }
}
