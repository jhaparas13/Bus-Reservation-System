
package com.paras.BusReservation.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AgencyDashboard extends JFrame {

    private final int agencyId;

    private final Color NAVY = new Color(15, 39, 75);
    private final Color BLUE = new Color(25, 105, 210);
    private final Color LIGHT_BG = new Color(245, 248, 253);
    private final Color TEXT_GRAY = new Color(105, 115, 130);

    public AgencyDashboard(int agencyId) {

        this.agencyId = agencyId;

        setTitle("Bus Reservation System - Agency Dashboard");
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

        JLabel title = new JLabel("Travel Agency Dashboard");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel("Manage your buses, journeys and customer bookings");
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

        JLabel welcome = new JLabel("Welcome, Agency!");
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcome.setForeground(NAVY);
        welcome.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel description = new JLabel("What would you like to manage today?");
        description.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        description.setForeground(TEXT_GRAY);
        description.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton manageJourneyButton = createButton(
                "Manage Journeys", BLUE, Color.WHITE);

        JButton bookingsButton = createButton(
                "View Booked Tickets",
                new Color(235, 242, 252), NAVY);

        JButton logoutButton = createButton(
                "Logout",
                new Color(255, 240, 240),
                new Color(190, 55, 55));

        card.add(welcome);
        card.add(Box.createVerticalStrut(10));
        card.add(description);
        card.add(Box.createVerticalStrut(35));
        card.add(manageJourneyButton);
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
        manageJourneyButton.addActionListener(e ->
                new ManageJourneyFrame(agencyId));

        bookingsButton.addActionListener(e ->
                new AgencyBookingsFrame(agencyId));

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
