
package com.paras.BusReservation.ui;

import com.paras.BusReservation.dao.AuthDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class LoginFrame extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;

    private final Color NAVY = new Color(15, 39, 75);
    private final Color BLUE = new Color(25, 105, 210);
    private final Color LIGHT_BG = new Color(245, 248, 253);

    public LoginFrame() {

        setTitle("Bus Reservation System");
        setSize(1050, 700);
        setMinimumSize(new Dimension(850, 550));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new GridLayout(1, 2));

        // LEFT SIDE - BUS IMAGE
        HeroPanel heroPanel = new HeroPanel();
        mainPanel.add(heroPanel);

        // RIGHT SIDE - LOGIN FORM
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(LIGHT_BG);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(new EmptyBorder(25, 45, 25, 45));
        card.setPreferredSize(new Dimension(390, 550));

        // HEADER
        JLabel busIcon = new JLabel("🚌");
        busIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 42));
        busIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel heading = new JLabel("Welcome Back");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 30));
        heading.setForeground(NAVY);
        heading.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Log in to your account");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        subtitle.setForeground(Color.GRAY);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // INPUT FIELDS
        emailField = new JTextField();
        passwordField = new JPasswordField();

        styleField(emailField, "Email Address");
        styleField(passwordField, "Password");

        // LABELS
        JLabel emailLabel = new JLabel("Email Address");
        JLabel passwordLabel = new JLabel("Password");

        styleLabel(emailLabel);
        styleLabel(passwordLabel);

        // BUTTONS
        JButton loginButton = new JButton("Login");
        styleButton(loginButton, BLUE, Color.WHITE);

        JButton registerButton = new JButton("Create New Account");
        styleButton(registerButton, Color.WHITE, BLUE);

        registerButton.setBorder(
                BorderFactory.createLineBorder(BLUE, 1)
        );

        // ADD COMPONENTS
        card.add(busIcon);
        card.add(Box.createVerticalStrut(12));

        card.add(heading);
        card.add(Box.createVerticalStrut(8));

        card.add(subtitle);
        card.add(Box.createVerticalStrut(30));

        card.add(emailLabel);
        card.add(Box.createVerticalStrut(7));
        card.add(emailField);

        card.add(Box.createVerticalStrut(18));

        card.add(passwordLabel);
        card.add(Box.createVerticalStrut(7));
        card.add(passwordField);

        card.add(Box.createVerticalStrut(25));

        card.add(loginButton);
        card.add(Box.createVerticalStrut(15));

        card.add(registerButton);

        rightPanel.add(card);
        mainPanel.add(rightPanel);

        add(mainPanel);

        // ACTION LISTENERS
        loginButton.addActionListener(e -> login());

        registerButton.addActionListener(e -> {
            new RegisterFrame();
            dispose();
        });

        getRootPane().setDefaultButton(loginButton);

        setVisible(true);
    }

    // LABEL STYLING
    private void styleLabel(JLabel label) {

        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(NAVY);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setMaximumSize(new Dimension(300, 22));
    }

    // INPUT FIELD STYLING
    private void styleField(JTextField field, String placeholder) {

        field.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        Dimension fieldSize = new Dimension(300, 48);

        field.setPreferredSize(fieldSize);
        field.setMaximumSize(fieldSize);
        field.setMinimumSize(fieldSize);

        field.setAlignmentX(Component.CENTER_ALIGNMENT);

        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(210, 218, 230)),
                new EmptyBorder(10, 12, 10, 12)
        ));

        field.setToolTipText(placeholder);
    }

    // BUTTON STYLING
    private void styleButton(JButton button,
                             Color background,
                             Color foreground) {

        button.setFont(new Font("Segoe UI", Font.BOLD, 15));
        button.setForeground(foreground);
        button.setBackground(background);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        Dimension buttonSize = new Dimension(300, 48);

        button.setPreferredSize(buttonSize);
        button.setMaximumSize(buttonSize);
        button.setMinimumSize(buttonSize);

        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBorder(BorderFactory.createEmptyBorder(
                10, 15, 10, 15
        ));
    }

    // LOGIN LOGIC
    private void login() {

        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {

            JOptionPane.showMessageDialog(this,
                    "Please enter email and password.");

            return;
        }

        AuthDAO dao = new AuthDAO();

        String role = dao.login(email, password);

        if ("AGENCY".equals(role)) {

            int agencyId = dao.getUserId(email);

            new AgencyDashboard(agencyId);
            dispose();

        } else if ("CUSTOMER".equals(role)) {

            int customerId = dao.getUserId(email);

            JOptionPane.showMessageDialog(this,
                    "Customer Login Successful!");

            new CustomerDashboard(customerId);
            dispose();

        } else {

            JOptionPane.showMessageDialog(this,
                    "Invalid email or password.");
        }
    }

    // LEFT IMAGE PANEL
    private class HeroPanel extends JPanel {

        private BufferedImage busImage;

        HeroPanel() {

            try {

                busImage = ImageIO.read(
                        getClass().getResource("/images/login_bus.png")
                );

            } catch (Exception e) {

                System.out.println("Login bus image not found.");
            }

            setBackground(NAVY);
        }

        @Override
        protected void paintComponent(Graphics g) {

            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();

            if (busImage != null) {

                int panelWidth = getWidth();
                int panelHeight = getHeight();

                double scale = Math.max(
                        (double) panelWidth / busImage.getWidth(),
                        (double) panelHeight / busImage.getHeight()
                );

                int width = (int) (busImage.getWidth() * scale);
                int height = (int) (busImage.getHeight() * scale);

                int x = (panelWidth - width) / 2;
                int y = (panelHeight - height) / 2;

                g2.drawImage(
                        busImage, x, y, width, height, this
                );

                // Subtle dark overlay for text readability
                g2.setColor(new Color(0, 0, 0, 0));
                g2.fillRect(0, 0, panelWidth, panelHeight);

            } else {

                GradientPaint gradient = new GradientPaint(
                        0, 0, NAVY,
                        getWidth(), getHeight(), BLUE
                );

                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }

            // BANNER TEXT
//            g2.setColor(Color.WHITE);
//
//            g2.setFont(new Font("Segoe UI", Font.BOLD, 34));
//            g2.drawString("Bus Reservation", 45, 100);
//            g2.drawString("System", 45, 145);
//
//            g2.setFont(new Font("Segoe UI", Font.ITALIC, 19));
//            g2.drawString("Your Journey, Our Priority.", 48, 185);
//
//            g2.dispose();
        }
    }
}
