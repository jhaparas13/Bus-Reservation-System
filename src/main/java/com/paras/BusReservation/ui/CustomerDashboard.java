
package com.paras.BusReservation.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CustomerDashboard extends JFrame {

    private final int userId;

    private final Color NAVY = new Color(15, 39, 75);
    private final Color BLUE = new Color(25, 105, 210);
    private final Color LIGHT_BG = new Color(245, 248, 253);
    private final Color TEXT_GRAY = new Color(105, 115, 130);

    public CustomerDashboard(int userId) {

        this.userId = userId;

        setTitle("Bus Reservation System - Customer Dashboard");
        setSize(850, 600);
        setMinimumSize(new Dimension(750, 550));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(LIGHT_BG);

        // TOP HEADER
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(NAVY);
        header.setBorder(new EmptyBorder(25, 40, 25, 40));

        JPanel headingPanel = new JPanel();
        headingPanel.setLayout(new BoxLayout(headingPanel, BoxLayout.Y_AXIS));
        headingPanel.setOpaque(false);

        JLabel title = new JLabel("Customer Dashboard");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel("Manage your journeys and bookings");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(new Color(210, 220, 235));

        headingPanel.add(title);
        headingPanel.add(Box.createVerticalStrut(6));
        headingPanel.add(subtitle);

        header.add(headingPanel, BorderLayout.WEST);

        mainPanel.add(header, BorderLayout.NORTH);

        // CENTER CONTENT
        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(LIGHT_BG);
        content.setBorder(new EmptyBorder(35, 40, 35, 40));

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(new EmptyBorder(30, 35, 30, 35));

        JLabel welcome = new JLabel("Welcome!");
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcome.setForeground(NAVY);
        welcome.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel description = new JLabel("Where would you like to go today?");
        description.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        description.setForeground(TEXT_GRAY);
        description.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton searchButton = createButton(
                "Search Buses", BLUE, Color.WHITE);

        JButton bookingsButton = createButton(
                "My Bookings", new Color(235, 242, 252), NAVY);

        JButton logoutButton = createButton(
                "Logout", new Color(255, 240, 240),
                new Color(190, 55, 55));

        card.add(welcome);
        card.add(Box.createVerticalStrut(10));
        card.add(description);
        card.add(Box.createVerticalStrut(35));
        card.add(searchButton);
        card.add(Box.createVerticalStrut(16));
        card.add(bookingsButton);
        card.add(Box.createVerticalStrut(16));
        card.add(logoutButton);

        content.add(card);
        mainPanel.add(content, BorderLayout.CENTER);

        // FOOTER
        JLabel footer = new JLabel(
                "Bus Reservation System",
                JLabel.CENTER
        );
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        footer.setForeground(TEXT_GRAY);
        footer.setBorder(new EmptyBorder(0, 10, 15, 10));

        mainPanel.add(footer, BorderLayout.SOUTH);

        add(mainPanel);

        // ACTION LISTENERS
        searchButton.addActionListener(e ->
                new SearchJourneyFrame(userId));

        bookingsButton.addActionListener(e ->
                new MyBookingsFrame(userId));

        logoutButton.addActionListener(e -> {
            new LoginFrame();
            dispose();
        });

        setVisible(true);
    }

    private JButton createButton(
            String text, Color background, Color foreground) {

        JButton button = new JButton(text);

        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(300, 52));
        button.setPreferredSize(new Dimension(300, 52));
        button.setMinimumSize(new Dimension(300, 52));

        return button;
    }
}
