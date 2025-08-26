package com.mycompany.employeemanagementsystem;
public class EmployeeManagementSystem {
    public static void main(String[] args) {
        EmployeeList employeeList = new EmployeeList();
        LoginFrame loginFrame = new LoginFrame(employeeList);
        loginFrame.setVisible(true);
    }
}