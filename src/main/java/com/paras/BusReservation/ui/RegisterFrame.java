
package com.paras.BusReservation.ui;

import com.paras.BusReservation.dao.AuthDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class RegisterFrame extends JFrame {

    private JTextField nameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JPasswordField passwordField;

    private final Color NAVY = new Color(15, 39, 75);
    private final Color BLUE = new Color(25, 105, 210);
    private final Color LIGHT_BG = new Color(245, 248, 253);
    private final Color TEXT = new Color(45, 55, 70);
    private final Color BORDER = new Color(215, 223, 235);

    public RegisterFrame() {

        setTitle("Bus Reservation System - Registration");
        setSize(520, 720);
        setMinimumSize(new Dimension(480, 680));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(LIGHT_BG);

        // ---------------- HEADER ----------------

        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(NAVY);
        header.setBorder(new EmptyBorder(25, 35, 25, 35));

        JLabel icon = new JLabel("🚌");
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 35));
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel heading = new JLabel("Create Account");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 26));
        heading.setForeground(Color.WHITE);
        heading.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Register to start your journey with us");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(new Color(210, 220, 235));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        header.add(icon);
        header.add(Box.createVerticalStrut(8));
        header.add(heading);
        header.add(Box.createVerticalStrut(6));
        header.add(subtitle);

        // ---------------- FORM CARD ----------------

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(new EmptyBorder(20, 40, 25, 40));

        nameField = createTextField();
        emailField = createTextField();
        phoneField = createTextField();

        passwordField = new JPasswordField();
        styleField(passwordField);

        // Add all four fields first
        addField(card, 0, "Full Name", nameField);
        addField(card, 1, "Email Address", emailField);
        addField(card, 2, "Phone Number", phoneField);
        addField(card, 3, "Password", passwordField);

        // ---------------- BUTTONS ----------------

        JButton registerButton = new JButton("Create Account");
        JButton backButton = new JButton("Back to Login");

        styleButton(registerButton, BLUE, Color.WHITE);
        styleButton(backButton, new Color(235, 242, 252), NAVY);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 0, 0);

        // Register button - after all fields
        gbc.gridy = 8;
        gbc.insets = new Insets(22, 0, 8, 0);
        card.add(registerButton, gbc);

        // Back button - below Register
        gbc.gridy = 9;
        gbc.insets = new Insets(5, 0, 5, 0);
        card.add(backButton, gbc);

        // ---------------- FOOTER ----------------

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setBackground(LIGHT_BG);
        footer.setBorder(new EmptyBorder(8, 0, 12, 0));

        JLabel footerText = new JLabel("Bus Reservation System");
        footerText.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        footerText.setForeground(new Color(125, 135, 150));

        footer.add(footerText);

        // ---------------- LAYOUT ----------------

        mainPanel.add(header, BorderLayout.NORTH);
        mainPanel.add(card, BorderLayout.CENTER);
        mainPanel.add(footer, BorderLayout.SOUTH);

        add(mainPanel);

        // ---------------- ACTION LISTENERS ----------------

        registerButton.addActionListener(e -> register());

        backButton.addActionListener(e -> {
            new LoginFrame();
            dispose();
        });

        setVisible(true);
    }

    // ---------------- FIELD CREATION ----------------

    private JTextField createTextField() {

        JTextField field = new JTextField();
        styleField(field);
        return field;
    }

    private void styleField(JTextField field) {

        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setForeground(TEXT);
        field.setBackground(Color.WHITE);
        field.setPreferredSize(new Dimension(300, 40));

        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(5, 10, 5, 10)
        ));
    }

    private void addField(
            JPanel panel,
            int row,
            String labelText,
            JTextField field) {

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(TEXT);

        gbc.gridy = row * 2;
        gbc.insets = new Insets(7, 0, 2, 0);
        panel.add(label, gbc);

        gbc.gridy = row * 2 + 1;
        gbc.insets = new Insets(2, 0, 7, 0);
        panel.add(field, gbc);
    }

    // ---------------- BUTTON STYLING ----------------

    private void styleButton(
            JButton button,
            Color background,
            Color foreground) {

        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(foreground);
        button.setBackground(background);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(300, 42));
    }

    // ---------------- REGISTRATION LOGIC ----------------

    private void register() {

        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (name.isEmpty() || email.isEmpty() ||
                phone.isEmpty() || password.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "All fields are required."
            );
            return;
        }

        if (!email.contains("@") || !email.contains(".")) {

            JOptionPane.showMessageDialog(
                    this,
                    "Enter a valid email address."
            );
            return;
        }

        if (password.length() < 6) {

            JOptionPane.showMessageDialog(
                    this,
                    "Password must contain at least 6 characters."
            );
            return;
        }

        AuthDAO dao = new AuthDAO();

        boolean success = dao.register(
                name, email, password, phone
        );

        if (success) {

            JOptionPane.showMessageDialog(
                    this,
                    "Registration Successful! Please Login."
            );

            new LoginFrame();
            dispose();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Registration Failed! Email may already exist."
            );
        }
    }
}
