package com.mycompany.employeemanagementsystem;

import java.util.ArrayList;
import java.util.List;

public class EmployeeList {
    private Employee head;

    public void addEmployee(int id, String name, String department, String phoneNumber, String position, String gender, int salary, java.time.LocalDate date) {
        Employee newEmployee = new Employee(id, name, department, phoneNumber, position, gender, salary, date);

        if (head == null) {
            head = newEmployee;
        } else {
            Employee current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newEmployee;
        }
    }

    public Employee searchEmployee(int id) {
        Employee current = head;
        while (current != null) {
            if (current.getId() == id) {
                return current;
            }
            current = current.next;
        }
        return null;
    }

    public boolean deleteEmployee(int id) {
        if (head == null) {
            return false;
        }

        if (head.getId() == id) {
            head = head.next;
            return true;
        }

        Employee current = head;
        while (current.next != null) {
            if (current.next.getId() == id) {
                current.next = current.next.next;
                return true;
            }
            current = current.next;
        }

        return false;
    }

    public List<Employee> getAllEmployees() {
        List<Employee> list = new ArrayList<>();
        Employee current = head;
        while (current != null) {
            list.add(current);
            current = current.next;
        }
        return list;
    }
}
