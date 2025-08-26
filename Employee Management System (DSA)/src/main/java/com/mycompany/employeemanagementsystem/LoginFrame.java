package com.mycompany.employeemanagementsystem;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final EmployeeList employeeList;
    private final Image backgroundImage;

    public LoginFrame(EmployeeList employeeList) {
        this.employeeList = employeeList;
        backgroundImage = new ImageIcon("C:\\Users\\HP RAFAY\\Documents\\NetBeansProjects\\EmployeeManagementSystems\\src\\main\\java\\com\\mycompany\\employeemanagementsystem\\Image\\Login_Background.jpg").getImage();

        setTitle("Employee Management System - Login");
        setSize(850, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new GridBagLayout());
        JPanel loginCard = new JPanel();
        loginCard.setLayout(new BoxLayout(loginCard, BoxLayout.Y_AXIS));
        loginCard.setPreferredSize(new Dimension(500, 350));
        loginCard.setBackground(new Color(255, 255, 255, 230));
        loginCard.setBorder(new CompoundBorder(
                new LineBorder(Color.LIGHT_GRAY, 1, true),
                new EmptyBorder(30, 40, 30, 40)
        ));

        JLabel titleLabel = new JLabel("Employee Management System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(new Color(40, 40, 40));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(new EmptyBorder(0, 0, 25, 0));
        loginCard.add(titleLabel);

        JPanel userPanel = new JPanel(new BorderLayout(10, 10));
        userPanel.setOpaque(false);
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        usernameField = new JTextField();
        styleField(usernameField);
        userPanel.add(userLabel, BorderLayout.WEST);
        userPanel.add(usernameField, BorderLayout.CENTER);
        loginCard.add(userPanel);
        loginCard.add(Box.createVerticalStrut(20));

        JPanel passPanel = new JPanel(new BorderLayout(10, 10));
        passPanel.setOpaque(false);
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        passwordField = new JPasswordField();
        styleField(passwordField);
        passPanel.add(passLabel, BorderLayout.WEST);
        passPanel.add(passwordField, BorderLayout.CENTER);
        loginCard.add(passPanel);
        loginCard.add(Box.createVerticalStrut(30));

        JButton loginButton = new JButton("Login");
        styleButton(loginButton);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(loginButton);
        loginCard.add(buttonPanel);

        backgroundPanel.add(loginCard);
        add(backgroundPanel);

        loginButton.addActionListener(e -> performLogin());
        usernameField.addActionListener(e -> performLogin());
        passwordField.addActionListener(e -> performLogin());
    }

    private void styleField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 17));
        field.setPreferredSize(new Dimension(300, 40));
        field.setBorder(new CompoundBorder(
                new LineBorder(Color.GRAY, 1, true),
                new EmptyBorder(10, 14, 10, 14)
        ));
        field.setBackground(Color.WHITE);
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBackground(new Color(0, 120, 215));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(140, 40));
        button.setBorder(new LineBorder(new Color(0, 90, 180), 2, true));
    }

    private void performLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Fields can't be empty!", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (!username.equals("admin") || !password.equals("1234")) {
            JOptionPane.showMessageDialog(this, "Incorrect Username or Password!", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Login Successful!");
            new MainFrame(employeeList).setVisible(true);
            this.dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame(new EmployeeList()).setVisible(true));
    }
}