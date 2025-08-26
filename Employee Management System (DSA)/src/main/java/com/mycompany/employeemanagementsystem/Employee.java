package com.mycompany.employeemanagementsystem;

import java.time.LocalDate;

public class Employee {
    private int id;
    private String name;
    private String department;
    private String phoneNumber; 
    private String position;
    private String gender; 
    private int salary;
    private LocalDate date; // Date of joining or any relevant date
    public Employee next;

    public Employee(int id, String name, String department, String phoneNumber, String position, String gender, int salary, LocalDate date) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.phoneNumber = phoneNumber;
        this.position = position;
        this.gender = gender;
        this.salary = salary;
        this.date = date;
        this.next = null;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPosition() {
        return position;
    }

    public String getGender() {
        return gender;
    }

    public int getSalary() {
        return salary;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}

