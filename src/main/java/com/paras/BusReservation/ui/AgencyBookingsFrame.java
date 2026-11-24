
package com.paras.BusReservation.ui;

import com.paras.BusReservation.dao.BookingDAO;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AgencyBookingsFrame extends JFrame {

    private final int agencyId;
    private final BookingDAO dao = new BookingDAO();

    private final DefaultTableModel confirmedModel;
    private final DefaultTableModel cancelledModel;

    private JLabel totalLabel;
    private JLabel confirmedLabel;
    private JLabel cancelledLabel;

    private JTable confirmedTable;
    private JTable cancelledTable;

    private final String[] columns = {
            "Booking ID", "Customer", "Email", "Bus",
            "Route", "Journey Date", "Seats", "Amount",
            "Payment", "Payment Status", "Booked On"
    };

    public AgencyBookingsFrame(int agencyId) {

        this.agencyId = agencyId;

        setTitle("Agency - Booked Tickets");
        setSize(1250, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        confirmedModel = createModel();
        cancelledModel = createModel();

        confirmedTable = createTable(confirmedModel);
        cancelledTable = createTable(cancelledModel);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(20, 25, 20, 25)
        );
        mainPanel.setBackground(new Color(245, 247, 250));

        // Heading
        JLabel heading = new JLabel("Booking Management");
        heading.setFont(new Font("Arial", Font.BOLD, 25));
        heading.setForeground(new Color(35, 45, 60));

        JLabel subtitle = new JLabel(
                "View and manage bookings for your journeys"
        );
        subtitle.setFont(new Font("Arial", Font.PLAIN, 13));
        subtitle.setForeground(Color.GRAY);

        JPanel headingPanel = new JPanel(new BorderLayout());
        headingPanel.setOpaque(false);
        headingPanel.add(heading, BorderLayout.NORTH);
        headingPanel.add(subtitle, BorderLayout.SOUTH);

        // Summary cards
        totalLabel = new JLabel("0");
        confirmedLabel = new JLabel("0");
        cancelledLabel = new JLabel("0");

        JPanel summaryPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        summaryPanel.setOpaque(false);

        summaryPanel.add(createStatCard(
                "Total Bookings", totalLabel,
                new Color(48, 63, 85)
        ));

        summaryPanel.add(createStatCard(
                "Confirmed Bookings", confirmedLabel,
                new Color(30, 130, 90)
        ));

        summaryPanel.add(createStatCard(
                "Cancelled Bookings", cancelledLabel,
                new Color(190, 75, 75)
        ));

        JPanel topPanel = new JPanel(new BorderLayout(0, 20));
        topPanel.setOpaque(false);
        topPanel.add(headingPanel, BorderLayout.NORTH);
        topPanel.add(summaryPanel, BorderLayout.CENTER);

        // Booking sections
        JPanel confirmedPanel = createTablePanel(
                "Confirmed Bookings",
                confirmedTable
        );

        JPanel cancelledPanel = createTablePanel(
                "Cancelled Bookings",
                cancelledTable
        );

        JPanel tablesPanel = new JPanel();
        tablesPanel.setLayout(new BoxLayout(tablesPanel, BoxLayout.Y_AXIS));
        tablesPanel.setOpaque(false);

        tablesPanel.add(confirmedPanel);
        tablesPanel.add(Box.createVerticalStrut(20));
        tablesPanel.add(cancelledPanel);

        JScrollPane mainScroll = new JScrollPane(tablesPanel);
        mainScroll.setBorder(null);
        mainScroll.getViewport().setBackground(
                new Color(245, 247, 250)
        );

        // Bottom buttons
        JButton refreshButton = new JButton("Refresh");
        JButton closeButton = new JButton("Close");

        refreshButton.addActionListener(e -> loadBookings());
        closeButton.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 10, 5)
        );
        buttonPanel.setOpaque(false);
        buttonPanel.add(refreshButton);
        buttonPanel.add(closeButton);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.add(buttonPanel, BorderLayout.EAST);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(mainScroll, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);

        loadBookings();

        setVisible(true);
    }

    private DefaultTableModel createModel() {

        return new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private JTable createTable(DefaultTableModel model) {

        JTable table = new JTable(model);

        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 12)
        );
        table.getTableHeader().setBackground(
                new Color(230, 235, 242)
        );
        table.getTableHeader().setForeground(
                new Color(45, 55, 70)
        );

        table.setSelectionBackground(new Color(210, 225, 245));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        int[] widths = {
                85, 130, 180, 130, 160, 110,
                65, 90, 100, 120, 150
        };

        for (int i = 0; i < widths.length; i++) {
            table.getColumnModel().getColumn(i)
                    .setPreferredWidth(widths[i]);
        }

        return table;
    }

    private JPanel createStatCard(
            String title, JLabel value, Color accent) {

        JPanel card = new JPanel(new BorderLayout(5, 8));

        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(225, 230, 237)),
                BorderFactory.createEmptyBorder(15, 18, 15, 18)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        titleLabel.setForeground(Color.GRAY);

        value.setFont(new Font("Arial", Font.BOLD, 27));
        value.setForeground(accent);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(value, BorderLayout.CENTER);

        return card;
    }

    private JPanel createTablePanel(String title, JTable table) {

        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(225, 230, 237)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 17));
        titleLabel.setForeground(new Color(45, 55, 70));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(1100, 180));

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        return panel;
    }

    private void loadBookings() {

        confirmedModel.setRowCount(0);
        cancelledModel.setRowCount(0);

        List<Object[]> bookings = dao.getAgencyBookings(agencyId);

        int confirmedCount = 0;
        int cancelledCount = 0;

        for (Object[] booking : bookings) {

            String status = booking[13].toString();

            // Map DAO result to visible table columns
            Object[] row = {
                    booking[0], booking[1], booking[2],
                    booking[4], booking[6] + " - " + booking[7],
                    booking[8], booking[9], booking[10],
                    booking[11], booking[12], booking[14]
            };

            if ("CONFIRMED".equalsIgnoreCase(status)) {
                confirmedModel.addRow(row);
                confirmedCount++;
            } else if ("CANCELLED".equalsIgnoreCase(status)) {
                cancelledModel.addRow(row);
                cancelledCount++;
            }
        }

        totalLabel.setText(String.valueOf(bookings.size()));
        confirmedLabel.setText(String.valueOf(confirmedCount));
        cancelledLabel.setText(String.valueOf(cancelledCount));
    }
}
