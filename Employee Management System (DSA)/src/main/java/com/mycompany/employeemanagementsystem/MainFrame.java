package com.mycompany.employeemanagementsystem;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.List;

public class MainFrame extends JFrame {
    private final EmployeeList employeeList;
    private DefaultTableModel tableModel;
    private JTable employeesTable;
    private JTextField idField;
    private JTextField nameField;
    private JTextField deptField;
    private JTextField phoneField;
    private JComboBox<String> positionComboBox;
    private JComboBox<String> genderComboBox;
    private JTextField salaryField;
    private JSpinner dateSpinner;
    private JButton addEmployeeButton;
    private JTabbedPane tabbedPane;

    // Attendance panel components
    private DefaultTableModel attendanceTableModel;
    private JTable attendanceTable;

    // Map to hold attendance data per employee: key = employee ID, value = boolean array for days (true = present)
    private Map<Integer, boolean[]> employeeAttendanceMap = new HashMap<>();

    public MainFrame(EmployeeList employeeList) {
        this.employeeList = employeeList;
        setTitle("Employee Management System");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(new Color(255, 69, 58));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFont(new Font("Arial", Font.BOLD, 14));
        logoutButton.setFocusPainted(false);
        logoutButton.addActionListener(e -> {
            this.dispose();
            new LoginFrame(employeeList).setVisible(true);
        });
        topPanel.add(logoutButton, BorderLayout.EAST);
        topPanel.setBackground(new Color(240, 248, 255));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(topPanel, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();
        JPanel manageEmployeesPanel = createManageEmployeesPanel();
        JPanel viewEmployeesPanel = createViewEmployeesPanel();
        JPanel attendancePanel = createAttendancePanel();

        tabbedPane.addTab("Add Employees", manageEmployeesPanel);
        tabbedPane.addTab("View Employees", viewEmployeesPanel);
        tabbedPane.addTab("Employee Attendance", attendancePanel);

        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int selectedIndex = tabbedPane.getSelectedIndex();
                String selectedTitle = tabbedPane.getTitleAt(selectedIndex);
                if ("Add Employees".equals(selectedTitle)) {
                    updateNextEmployeeIdField();
                    if (nameField != null) {
                        nameField.requestFocusInWindow();
                    }
                }
                if ("Employee Attendance".equals(selectedTitle)) {
                    refreshAttendanceTable();
                }
                if ("View Employees".equals(selectedTitle)) {
                    refreshViewEmployeesTable();
                }
            }
        });

        add(tabbedPane, BorderLayout.CENTER);
    }

    // ========== Add Employees panel ==========
    private JPanel createManageEmployeesPanel() {
        ImageIcon backgroundImage = new ImageIcon("C:\\Users\\HP RAFAY\\Documents\\NetBeansProjects\\EmployeeManagementSystems\\src\\main\\java\\com\\mycompany\\employeemanagementsystem\\Image\\Employee_Page.jpg");
        Image image = backgroundImage.getImage();
        JPanel manageEmployeesPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            }
        };
        manageEmployeesPanel.setOpaque(false);
        manageEmployeesPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel headerLabel = new JLabel("Add New Employee", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 28));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setOpaque(true);
        headerLabel.setBackground(new Color(0, 0, 0, 150));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        manageEmployeesPanel.add(headerLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setColor(new Color(255, 255, 255, 150));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createTitledBorder("Employee Details"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 15, 8, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel idLabel = new JLabel("Employee ID:");
        idField = new JTextField();
        idField.setPreferredSize(new Dimension(200, 30));
        idField.setEditable(false);
        idField.setFocusable(false);
        idField.setCaretColor(idField.getBackground());

        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField();
        nameField.setPreferredSize(new Dimension(200, 30));

        JLabel deptLabel = new JLabel("Department:");
        deptField = new JTextField();
        deptField.setPreferredSize(new Dimension(200, 30));

        JLabel phoneLabel = new JLabel("Phone Number:");
        phoneField = new JTextField();
        phoneField.setPreferredSize(new Dimension(200, 30));

        JLabel positionLabel = new JLabel("Position:");
        String[] positions = {"IT", "Graphic Designer", "HR", "Admin", "Marketing Head"};
        positionComboBox = new JComboBox<>(positions);

        JLabel genderLabel = new JLabel("Gender:");
        String[] genders = {"Male", "Female", "Other"};
        genderComboBox = new JComboBox<>(genders);

        JLabel salaryLabel = new JLabel("Salary:");
        salaryField = new JTextField();
        salaryField.setPreferredSize(new Dimension(200, 30));

        JLabel dateLabel = new JLabel("Date:");
        SpinnerDateModel dateModel = new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH);
        dateSpinner = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(dateEditor);
        dateSpinner.setPreferredSize(new Dimension(200, 30));

        Font labelFontBold = new Font("Arial", Font.BOLD, 14);
        idLabel.setFont(labelFontBold);
        nameLabel.setFont(labelFontBold);
        deptLabel.setFont(labelFontBold);
        phoneLabel.setFont(labelFontBold);
        positionLabel.setFont(labelFontBold);
        genderLabel.setFont(labelFontBold);
        salaryLabel.setFont(labelFontBold);
        dateLabel.setFont(labelFontBold);

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(idLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(idField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(nameLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(deptLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(deptField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(phoneLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(phoneField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(positionLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(positionComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(genderLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(genderComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        formPanel.add(salaryLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(salaryField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        formPanel.add(dateLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(dateSpinner, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setColor(new Color(255, 255, 255, 150));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        buttonPanel.setOpaque(false);
        addEmployeeButton = new JButton("Add Employee");
        addEmployeeButton.setBackground(new Color(0, 102, 204));
        addEmployeeButton.setForeground(Color.WHITE);
        addEmployeeButton.setFont(new Font("Arial", Font.BOLD, 14));
        addEmployeeButton.setPreferredSize(new Dimension(200, 40));
        addEmployeeButton.setFocusPainted(false);
        addEmployeeButton.addActionListener(e -> {
            addEmployee();
            refreshViewEmployeesTable();
            refreshAttendanceTable();
        });

        buttonPanel.add(addEmployeeButton);
        manageEmployeesPanel.add(formPanel, BorderLayout.CENTER);
        manageEmployeesPanel.add(buttonPanel, BorderLayout.SOUTH);

        SwingUtilities.invokeLater(() -> {
            updateNextEmployeeIdField();
            nameField.requestFocusInWindow();
        });

        manageEmployeesPanel.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                .put(KeyStroke.getKeyStroke("ENTER"), "addEmployee");
        manageEmployeesPanel.getActionMap()
                .put("addEmployee", new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        addEmployeeButton.doClick();
                    }
                });

        return manageEmployeesPanel;
    }

    private void addEmployee() {
        String name = nameField.getText().trim();
        String department = deptField.getText().trim();
        String phoneNumber = phoneField.getText().trim();
        String position = (String) positionComboBox.getSelectedItem();
        String gender = (String) genderComboBox.getSelectedItem();
        String salaryText = salaryField.getText().trim();
        Date selectedDate = (Date) dateSpinner.getValue();
        LocalDate localDate = selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        if (name.isEmpty() || department.isEmpty() || phoneNumber.isEmpty() || position == null || gender == null || salaryText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!phoneNumber.matches("\\d{12}")) {
            JOptionPane.showMessageDialog(this, "Phone number must be 12 digits!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!name.matches("[a-zA-Z ]+") || !department.matches("[a-zA-Z ]+")) {
            JOptionPane.showMessageDialog(this, "Name and Department must contain only characters!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int salary;
        try {
            salary = Integer.parseInt(salaryText);
            if (salary < 0) {
                JOptionPane.showMessageDialog(this, "Salary must be a non-negative integer!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Salary must be a valid integer!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int id;
        try {
            id = Integer.parseInt(idField.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid Employee ID.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        boolean exists = (employeeList.searchEmployee(id) != null);
        if (exists) {
            JOptionPane.showMessageDialog(this, "Employee with this ID already exists!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        employeeList.addEmployee(id, name, department, phoneNumber, position, gender, salary, localDate);
        JOptionPane.showMessageDialog(this, "Employee added successfully!");

        // Clear fields
        nameField.setText("");
        deptField.setText("");
        phoneField.setText("");
        salaryField.setText("");
        dateSpinner.setValue(new Date());

        updateNextEmployeeIdField();
        nameField.requestFocusInWindow();
    }

    private void updateNextEmployeeIdField() {
        int nextId = findNextAvailableId();
        idField.setText(String.valueOf(nextId));
    }

    private int findNextAvailableId() {
        Set<Integer> existingIds = new HashSet<>();
        List<Employee> employees = employeeList.getAllEmployees();
        for (Employee emp : employees) {
            existingIds.add(emp.getId());
        }
        int id = 1;
        while (existingIds.contains(id)) {
            id++;
        }
        return id;
    }

    // ========== View Employees panel ==========
    private JPanel createViewEmployeesPanel() {
        JPanel viewEmployeesPanel = new JPanel(new BorderLayout());
        viewEmployeesPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        viewEmployeesPanel.setBackground(new Color(240, 248, 255));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel searchLabel = new JLabel("Search by Employee ID: ");
        JTextField searchField = new JTextField(15);
        JButton searchButton = new JButton("Search");

        searchButton.addActionListener(e -> performSearch(searchField));
        searchField.addActionListener(e -> performSearch(searchField));
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        viewEmployeesPanel.add(searchPanel, BorderLayout.NORTH);

        String[] columnNames = {"ID", "Name", "Department", "Phone Number", "Position", "Gender", "Salary", "Date"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        employeesTable = new JTable(tableModel);
        employeesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(employeesTable);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton deleteButton = new JButton("Delete Selected Employee");
        JButton editButton = new JButton("Edit Selected Employee");

        deleteButton.addActionListener(e -> {
            deleteSelectedEmployee();
            refreshViewEmployeesTable();
            refreshAttendanceTable();
        });
        editButton.addActionListener(e -> {
            editSelectedEmployee();
        });
        buttonPanel.add(deleteButton);
        buttonPanel.add(editButton);
        viewEmployeesPanel.add(scrollPane, BorderLayout.CENTER);
        viewEmployeesPanel.add(buttonPanel, BorderLayout.SOUTH);
        SwingUtilities.invokeLater(this::refreshViewEmployeesTable);

        return viewEmployeesPanel;
    }

    private void performSearch(JTextField searchField) {
        String idInput = searchField.getText().trim();
        if (!idInput.isEmpty()) {
            try {
                int id = Integer.parseInt(idInput);
                Employee emp = employeeList.searchEmployee(id);
                if (emp != null) {
                    JOptionPane.showMessageDialog(this,
                            "Employee Found:\nID: " + emp.getId() +
                                    "\nName: " + emp.getName() +
                                    "\nDepartment: " + emp.getDepartment() +
                                    "\nPhone Number: " + emp.getPhoneNumber() +
                                    "\nPosition: " + emp.getPosition() +
                                    "\nGender: " + emp.getGender() +
                                    "\nSalary: " + emp.getSalary() +
                                    "\nDate: " + emp.getDate().toString());
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Employee with ID " + id + " not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number for ID.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please enter an Employee ID to search.", "Input Required", JOptionPane.ERROR_MESSAGE);
        }
        searchField.setText("");
    }

    // Load employees in ascending order of ID for View panel table
    private void refreshViewEmployeesTable() {
        List<Employee> employees = employeeList.getAllEmployees();
        employees.sort(Comparator.comparingInt(Employee::getId));

        tableModel.setRowCount(0);
        for (Employee emp : employees) {
            tableModel.addRow(new Object[]{
                    emp.getId(),
                    emp.getName(),
                    emp.getDepartment(),
                    emp.getPhoneNumber(),
                    emp.getPosition(),
                    emp.getGender(),
                    emp.getSalary(),
                    emp.getDate().toString()
            });
        }
    }

    private void deleteSelectedEmployee() {
        int selectedRow = employeesTable.getSelectedRow();
        if (selectedRow != -1) {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this employee?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                boolean deleted = employeeList.deleteEmployee(id);
                if (deleted) {
                    // Remove attendance record as well
                    employeeAttendanceMap.remove(id);
                    JOptionPane.showMessageDialog(this, "Employee deleted successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "Employee not found!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "No employee selected!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editSelectedEmployee() {
        int selectedRow = employeesTable.getSelectedRow();
        if (selectedRow != -1) {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            String name = (String) tableModel.getValueAt(selectedRow, 1);
            String department = (String) tableModel.getValueAt(selectedRow, 2);
            String phoneNumber = (String) tableModel.getValueAt(selectedRow, 3);
            String position = (String) tableModel.getValueAt(selectedRow, 4);
            String gender = (String) tableModel.getValueAt(selectedRow, 5);
            int salary = (int) tableModel.getValueAt(selectedRow, 6);
            String dateString = (String) tableModel.getValueAt(selectedRow, 7);

            JTextField idFieldEdit = new JTextField(String.valueOf(id));
            idFieldEdit.setEditable(false);
            JTextField nameFieldEdit = new JTextField(name);
            JTextField deptFieldEdit = new JTextField(department);
            JTextField phoneFieldEdit = new JTextField(phoneNumber);
            JComboBox<String> positionComboBoxEdit = new JComboBox<>(new String[]{"IT", "Graphic Designer", "HR", "Admin", "Marketing Head"});
            positionComboBoxEdit.setSelectedItem(position);
            JComboBox<String> genderComboBoxEdit = new JComboBox<>(new String[]{"Male", "Female", "Other"});
            genderComboBoxEdit.setSelectedItem(gender);
            JTextField salaryFieldEdit = new JTextField(String.valueOf(salary));

            SpinnerDateModel editDateModel;
            try {
                LocalDate localDate = LocalDate.parse(dateString);
                Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                editDateModel = new SpinnerDateModel(date, null, null, Calendar.DAY_OF_MONTH);
            } catch (Exception ex) {
                editDateModel = new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH);
            }
            JSpinner dateSpinnerEdit = new JSpinner(editDateModel);
            JSpinner.DateEditor dateEditorEdit = new JSpinner.DateEditor(dateSpinnerEdit, "yyyy-MM-dd");
            dateSpinnerEdit.setEditor(dateEditorEdit);

            Object[] message = {
                    "Employee ID:", idFieldEdit,
                    "Name:", nameFieldEdit,
                    "Department:", deptFieldEdit,
                    "Phone Number:", phoneFieldEdit,
                    "Position:", positionComboBoxEdit,
                    "Gender:", genderComboBoxEdit,
                    "Salary:", salaryFieldEdit,
                    "Date:", dateSpinnerEdit
            };

            int option = JOptionPane.showConfirmDialog(this, message, "Edit Employee", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                // Validate all fields
                String newName = nameFieldEdit.getText().trim();
                String newDepartment = deptFieldEdit.getText().trim();
                String newPhoneNumber = phoneFieldEdit.getText().trim();
                String newPosition = (String) positionComboBoxEdit.getSelectedItem();
                String newGender = (String) genderComboBoxEdit.getSelectedItem();
                String newSalaryText = salaryFieldEdit.getText().trim();
                Date selectedEditDate = (Date) dateSpinnerEdit.getValue();
                LocalDate newLocalDate = selectedEditDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                if (newName.isEmpty() || newDepartment.isEmpty() || newPhoneNumber.isEmpty() || newPosition == null || newGender == null || newSalaryText.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "All fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!newPhoneNumber.matches("\\d{12}")) {
                    JOptionPane.showMessageDialog(this, "Phone number must be 12 digits!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!newName.matches("[a-zA-Z ]+") || !newDepartment.matches("[a-zA-Z ]+")) {
                    JOptionPane.showMessageDialog(this, "Name and Department must contain only characters!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int newSalary;
                try {
                    newSalary = Integer.parseInt(newSalaryText);
                    if (newSalary < 0) {
                        JOptionPane.showMessageDialog(this, "Salary must be a non-negative integer!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Salary must be a valid integer!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                employeeList.deleteEmployee(id);
                employeeList.addEmployee(id, newName, newDepartment, newPhoneNumber, newPosition, newGender, newSalary, newLocalDate);
                refreshViewEmployeesTable();
                refreshAttendanceTable();
                JOptionPane.showMessageDialog(this, "Employee updated successfully!");
            }
        } else {
            JOptionPane.showMessageDialog(this, "No employee selected!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ========== Employee Attendance panel ==========
    private JPanel createAttendancePanel() {
        JPanel attendancePanel = new JPanel(new BorderLayout());
        attendancePanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        attendancePanel.setBackground(new Color(240, 248, 255)); // same as View Employees color

        String[] columns = {"ID", "Name", "Department", "Salary", "Action"};
        attendanceTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // only Action button editable
            }
        };
        attendanceTable = new JTable(attendanceTableModel);
        attendanceTable.setRowHeight(35);

        attendanceTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        attendanceTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        attendanceTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        attendanceTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        attendanceTable.getColumnModel().getColumn(4).setPreferredWidth(120);

        attendanceTable.getColumn("Action").setCellRenderer(new ButtonRenderer());
        attendanceTable.getColumn("Action").setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(attendanceTable);
        attendancePanel.add(scrollPane, BorderLayout.CENTER);
        return attendancePanel;
    }

    private void refreshAttendanceTable() {
        List<Employee> employees = employeeList.getAllEmployees();
        employees.sort(Comparator.comparingInt(Employee::getId));
        attendanceTableModel.setRowCount(0);
        for (Employee emp : employees) {
            int finalSalary = calculateSalaryWithAttendance(emp);
            attendanceTableModel.addRow(new Object[]{
                    emp.getId(),
                    emp.getName(),
                    emp.getDepartment(),
                    finalSalary,
                    "View More"
            });
        }
    }

    private int calculateSalaryWithAttendance(Employee emp) {
        int originalSalary = emp.getSalary();
        boolean[] attendance = employeeAttendanceMap.get(emp.getId());

        if (attendance == null) return originalSalary;

        int absentDays = 0;
        for (boolean present : attendance) {
            if (!present) absentDays++;
        }
        double deductionPercent = absentDays * 0.05;
        if (deductionPercent > 1.0) deductionPercent = 1.0;

        return (int) Math.round(originalSalary * (1 - deductionPercent));
    }

    // Renderer for "View More" button
    private class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setText("View More");
            setForeground(Color.WHITE);
            setBackground(new Color(0, 102, 204));
            setFont(getFont().deriveFont(Font.BOLD));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            setText(value == null ? "View More" : value.toString());
            return this;
        }
    }

    // Editor for "View More" button handling click
    private class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String label;
        private boolean clicked;
        private int row;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.setForeground(Color.WHITE);
            button.setBackground(new Color(0, 102, 204));
            button.setFont(button.getFont().deriveFont(Font.BOLD));

            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            this.label = (value == null) ? "View More" : value.toString();
            button.setText(label);
            this.clicked = true;
            this.row = row;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (clicked) openAttendanceDialog(row);
            clicked = false;
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            clicked = false;
            return super.stopCellEditing();
        }

        @Override
        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }

    // Attendance dialog UI and logic
    private void openAttendanceDialog(int row) {
        int empId = (int) attendanceTableModel.getValueAt(row, 0);
        Employee emp = employeeList.searchEmployee(empId);
        if (emp == null) {
            JOptionPane.showMessageDialog(this, "Employee data not found.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        JDialog dialog = new JDialog(this, "Attendance - " + emp.getName(), true);
        dialog.setSize(450, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        JLabel infoLabel = new JLabel("Mark attendance for current month:");
        infoLabel.setBorder(new EmptyBorder(10, 10, 0, 10));
        dialog.add(infoLabel, BorderLayout.NORTH);

        JPanel daysPanel = new JPanel(new GridLayout(0, 7, 5, 5));
        daysPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        Calendar cal = Calendar.getInstance();
        int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        JCheckBox[] dayCheckboxes = new JCheckBox[daysInMonth];

        boolean[] savedAttendance = employeeAttendanceMap.get(empId);
        for (int i = 0; i < daysInMonth; i++) {
            dayCheckboxes[i] = new JCheckBox(String.valueOf(i + 1));
            dayCheckboxes[i].setSelected(savedAttendance == null || (i < savedAttendance.length && savedAttendance[i]));
            daysPanel.add(dayCheckboxes[i]);
        }
        dialog.add(daysPanel, BorderLayout.CENTER);

        JButton submitButton = new JButton("Calculate Salary Deduction");
        submitButton.setBackground(new Color(0, 102, 204));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);
        submitButton.setFont(submitButton.getFont().deriveFont(Font.BOLD));

        submitButton.addActionListener(e -> {
            boolean[] attendanceForMonth = new boolean[daysInMonth];
            int absentDays = 0;
            for (int i = 0; i < daysInMonth; i++) {
                boolean present = dayCheckboxes[i].isSelected();
                attendanceForMonth[i] = present;
                if (!present) absentDays++;
            }
            employeeAttendanceMap.put(empId, attendanceForMonth);

            double deductionPercent = absentDays * 0.05; // 5% per absent day
            if (deductionPercent > 1.0) deductionPercent = 1.0; // max 100%

            int originalSalary = emp.getSalary();
            int finalSalary = (int) Math.round(originalSalary * (1 - deductionPercent));

            attendanceTableModel.setValueAt(finalSalary, row, 3);

            JOptionPane.showMessageDialog(dialog,
                    String.format("Absent Days: %d\nSalary Deduction: %.0f%%\nFinal Salary: %d",
                            absentDays, deductionPercent * 100, finalSalary),
                    "Salary Deduction", JOptionPane.INFORMATION_MESSAGE);
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(submitButton);
        dialog.add(bottomPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame(new EmployeeList()).setVisible(true));
    }
}
