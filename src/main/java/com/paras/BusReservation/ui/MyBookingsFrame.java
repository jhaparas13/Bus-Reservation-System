
package com.paras.BusReservation.ui;

import com.paras.BusReservation.dao.BookingDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MyBookingsFrame extends JFrame {

    private int userId;

    private JTable table;
    private DefaultTableModel model;

    private BookingDAO dao = new BookingDAO();

    private final Color NAVY = new Color(15, 39, 75);
    private final Color BLUE = new Color(25, 105, 210);
    private final Color LIGHT_BG = new Color(245, 248, 253);
    private final Color BORDER = new Color(215, 223, 235);
    private final Color TEXT = new Color(45, 55, 70);

    public MyBookingsFrame(int userId) {

        this.userId = userId;

        setTitle("Bus Reservation System - My Bookings");
        setSize(1150, 620);
        setMinimumSize(new Dimension(950, 500));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // MAIN PANEL
        JPanel mainPanel = new JPanel(new BorderLayout(12, 12));
        mainPanel.setBackground(LIGHT_BG);
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // ---------------- HEADER ----------------

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(NAVY);
        header.setBorder(new EmptyBorder(16, 22, 16, 22));

        JPanel headingPanel = new JPanel();
        headingPanel.setLayout(new BoxLayout(headingPanel, BoxLayout.Y_AXIS));
        headingPanel.setOpaque(false);

        JLabel heading = new JLabel("My Bookings");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 25));
        heading.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel(
                "View your reservations and manage your bookings"
        );
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(new Color(210, 220, 235));

        headingPanel.add(heading);
        headingPanel.add(Box.createVerticalStrut(4));
        headingPanel.add(subtitle);

        header.add(headingPanel, BorderLayout.WEST);

        // ---------------- TABLE ----------------

        String[] columns = {
                "Booking ID", "Bus", "Source", "Destination",
                "Journey Date", "Seats", "Amount",
                "Payment Method", "Payment Status", "Booking Status"
        };

        model = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);

        table.setRowHeight(32);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setForeground(TEXT);
        table.setBackground(Color.WHITE);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFillsViewportHeight(true);
        table.setGridColor(new Color(230, 235, 242));
        table.setShowVerticalLines(false);
        table.setSelectionBackground(new Color(220, 235, 255));
        table.setSelectionForeground(NAVY);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        // TABLE HEADER
        table.getTableHeader().setFont(
                new Font("Segoe UI", Font.BOLD, 13)
        );
        table.getTableHeader().setBackground(NAVY);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setPreferredSize(new Dimension(0, 38));
        table.getTableHeader().setReorderingAllowed(false);

        // ALTERNATING ROW COLORS
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {

            @Override
            public Component getTableCellRendererComponent(
                    JTable table,
                    Object value,
                    boolean isSelected,
                    boolean hasFocus,
                    int row,
                    int column) {

                Component c = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column
                );

                if (isSelected) {
                    c.setBackground(new Color(220, 235, 255));
                    c.setForeground(NAVY);
                } else {
                    c.setBackground(
                            row % 2 == 0
                                    ? Color.WHITE
                                    : new Color(246, 249, 253)
                    );
                    c.setForeground(TEXT);
                }

                setBorder(new EmptyBorder(0, 8, 0, 8));

                return c;
            }
        };

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel()
                    .getColumn(i)
                    .setCellRenderer(renderer);
        }

        // COLUMN WIDTHS
        int[] widths = {
                85, 130, 115, 115, 110,
                70, 95, 125, 125, 125
        };

        for (int i = 0; i < widths.length; i++) {
            table.getColumnModel()
                    .getColumn(i)
                    .setPreferredWidth(widths[i]);
        }

        JScrollPane scrollPane = new JScrollPane(table);

        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(BORDER),
                        "Booking History",
                        TitledBorder.LEFT,
                        TitledBorder.TOP,
                        new Font("Segoe UI", Font.BOLD, 14),
                        NAVY
                ),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        // ---------------- BUTTONS ----------------

        JButton cancelButton = new JButton("Cancel Booking");
        JButton refreshButton = new JButton("Refresh");

        styleButton(
                cancelButton,
                new Color(195, 60, 60),
                Color.WHITE
        );

        styleButton(
                refreshButton,
                new Color(235, 242, 252),
                NAVY
        );

        JPanel buttonPanel = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 10, 8)
        );
        buttonPanel.setBackground(LIGHT_BG);

        buttonPanel.add(cancelButton);
        buttonPanel.add(refreshButton);

        // ---------------- ADD TO FRAME ----------------

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(LIGHT_BG);
        topPanel.add(header, BorderLayout.CENTER);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // ---------------- ACTION LISTENERS ----------------

        cancelButton.addActionListener(e -> cancelBooking());
        refreshButton.addActionListener(e -> loadBookings());

        loadBookings();

        setVisible(true);
    }

    // ---------------- BUTTON STYLING ----------------

    private void styleButton(
            JButton button, Color background, Color foreground) {

        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(foreground);
        button.setBackground(background);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(145, 36));
    }

    // ---------------- LOAD BOOKINGS ----------------

    private void loadBookings() {

        model.setRowCount(0);

        for (Object[] booking : dao.getCustomerBookings(userId)) {
            model.addRow(booking);
        }
    }

    // ---------------- CANCEL BOOKING ----------------

    private void cancelBooking() {

        int row = table.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a booking first."
            );
            return;
        }

        int modelRow = table.convertRowIndexToModel(row);

        String status = model.getValueAt(modelRow, 9).toString();

        if (!"CONFIRMED".equals(status)) {

            JOptionPane.showMessageDialog(
                    this,
                    "This booking is already cancelled."
            );
            return;
        }

        int bookingId = Integer.parseInt(
                model.getValueAt(modelRow, 0).toString()
        );

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to cancel this booking?",
                "Confirm Cancellation",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        if (dao.cancelBooking(userId, bookingId)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Booking cancelled successfully.\nSeats have been restored."
            );

            loadBookings();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Cancellation failed. Please refresh and try again."
            );

            loadBookings();
        }
    }
}
