package com.gym.model;

public class Receptionist extends User{
    private int employeeId;

    public Receptionist() {
        super();
        setRole(UserRole.RECEPTIONIST);
    }

    public Receptionist(int id, String name, String username, String password, int employeeId) {
        super(id, name, username, password, UserRole.RECEPTIONIST);
        this.employeeId = employeeId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    @Override
    public String toString() {
        return "Receptionist{" +
                "employeeId=" + employeeId +
                "} " + super.toString();
    }
}
