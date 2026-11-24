
package com.paras.BusReservation.ui;

import com.paras.BusReservation.dao.JourneyDAO;
import com.paras.BusReservation.model.Journey;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.sql.Time;

public class ManageJourneyFrame extends JFrame {

    private int agencyId;

    private JTextField busNameField, busNumberField;
    private JTextField sourceField, destinationField;
    private JTextField dateField, departureField, arrivalField;
    private JTextField seatsField, priceField;

    private JTable table;
    private DefaultTableModel model;

    private JourneyDAO dao = new JourneyDAO();

    private final Color NAVY = new Color(15, 39, 75);
    private final Color BLUE = new Color(25, 105, 210);
    private final Color LIGHT_BG = new Color(245, 248, 253);
    private final Color BORDER = new Color(215, 223, 235);
    private final Color TEXT = new Color(45, 55, 70);

    public ManageJourneyFrame(int agencyId) {

        this.agencyId = agencyId;

        setTitle("Bus Reservation System - Manage Journeys");
        setSize(1150, 720);
        setMinimumSize(new Dimension(950, 650));
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

        JLabel heading = new JLabel("Manage Journeys");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 25));
        heading.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel(
                "Add, update and manage your bus journeys"
        );
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(new Color(210, 220, 235));

        headingPanel.add(heading);
        headingPanel.add(Box.createVerticalStrut(4));
        headingPanel.add(subtitle);

        header.add(headingPanel, BorderLayout.WEST);

        // ---------------- FORM ----------------

        JPanel form = new JPanel(new GridLayout(5, 4, 15, 12));
        form.setBackground(Color.WHITE);

        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(BORDER),
                        "Journey Details",
                        TitledBorder.LEFT,
                        TitledBorder.TOP,
                        new Font("Segoe UI", Font.BOLD, 14),
                        NAVY
                ),
                new EmptyBorder(15, 15, 15, 15)
        ));

        busNameField = new JTextField();
        busNumberField = new JTextField();
        sourceField = new JTextField();
        destinationField = new JTextField();
        dateField = new JTextField();
        departureField = new JTextField();
        arrivalField = new JTextField();
        seatsField = new JTextField();
        priceField = new JTextField();

        addFormField(form, "Bus Name:", busNameField);
        addFormField(form, "Bus Number:", busNumberField);
        addFormField(form, "Source:", sourceField);
        addFormField(form, "Destination:", destinationField);
        addFormField(form, "Journey Date:", dateField);
        addFormField(form, "Departure:", departureField);
        addFormField(form, "Arrival:", arrivalField);
        addFormField(form, "Total Seats:", seatsField);
        addFormField(form, "Price (₹):", priceField);

        form.add(new JLabel(""));
        form.add(new JLabel(""));

        // ---------------- BUTTONS ----------------

        JButton addButton = new JButton("Add Journey");
        JButton updateButton = new JButton("Update Journey");
        JButton deleteButton = new JButton("Delete Journey");
        JButton clearButton = new JButton("Clear");
        JButton refreshButton = new JButton("Refresh");

        styleButton(addButton, BLUE, Color.WHITE);
        styleButton(updateButton, new Color(30, 145, 105), Color.WHITE);
        styleButton(deleteButton, new Color(195, 60, 60), Color.WHITE);
        styleButton(clearButton, new Color(235, 242, 252), NAVY);
        styleButton(refreshButton, new Color(235, 242, 252), NAVY);

        JPanel buttonPanel = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 10, 8)
        );
        buttonPanel.setBackground(LIGHT_BG);

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(refreshButton);

        // TOP PANEL
        JPanel topPanel = new JPanel(new BorderLayout(8, 8));
        topPanel.setBackground(LIGHT_BG);

        topPanel.add(header, BorderLayout.NORTH);
        topPanel.add(form, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        // ---------------- TABLE ----------------

        String[] columns = {
                "ID", "Bus", "Number", "Source", "Destination",
                "Date", "Departure", "Arrival",
                "Total Seats", "Available", "Price"
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
                45, 120, 90, 100, 100,
                95, 90, 90, 85, 85, 75
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
                        "Available Journeys",
                        TitledBorder.LEFT,
                        TitledBorder.TOP,
                        new Font("Segoe UI", Font.BOLD, 14),
                        NAVY
                ),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        // ---------------- ADD TO FRAME ----------------

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);

        // ---------------- ACTION LISTENERS ----------------

        addButton.addActionListener(e -> addJourney());
        updateButton.addActionListener(e -> updateJourney());
        deleteButton.addActionListener(e -> deleteJourney());
        clearButton.addActionListener(e -> clearFields());
        refreshButton.addActionListener(e -> loadJourneys());

        table.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting() &&
                    table.getSelectedRow() != -1) {
                fillFields();
            }
        });

        loadJourneys();

        setVisible(true);
    }

    // ---------------- FORM FIELD STYLING ----------------

    private void addFormField(
            JPanel panel, String label, JTextField field) {

        JLabel jLabel = new JLabel(label);
        jLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        jLabel.setForeground(TEXT);

        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setPreferredSize(new Dimension(180, 32));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(5, 8, 5, 8)
        ));

        panel.add(jLabel);
        panel.add(field);
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
        button.setPreferredSize(new Dimension(125, 36));
    }

    // ---------------- GET JOURNEY FROM FIELDS ----------------

    private Journey getJourneyFromFields() {

        Journey j = new Journey();

        j.setAgencyId(agencyId);
        j.setBusName(busNameField.getText().trim());
        j.setBusNumber(busNumberField.getText().trim());
        j.setSource(sourceField.getText().trim());
        j.setDestination(destinationField.getText().trim());
        j.setJourneyDate(Date.valueOf(dateField.getText().trim()));
        j.setDepartureTime(Time.valueOf(departureField.getText().trim()));
        j.setArrivalTime(Time.valueOf(arrivalField.getText().trim()));
        j.setTotalSeats(Integer.parseInt(seatsField.getText().trim()));
        j.setPrice(Double.parseDouble(priceField.getText().trim()));

        return j;
    }

    // ---------------- ADD JOURNEY ----------------

    private void addJourney() {

        try {

            Journey j = getJourneyFromFields();

            if (j.getBusName().isEmpty() ||
                    j.getSource().isEmpty() ||
                    j.getDestination().isEmpty()) {

                JOptionPane.showMessageDialog(
                        this, "Fill all required fields."
                );
                return;
            }

            if (j.getTotalSeats() <= 0 || j.getPrice() < 0) {

                JOptionPane.showMessageDialog(
                        this, "Invalid seats or price."
                );
                return;
            }

            if (dao.addJourney(j)) {

                JOptionPane.showMessageDialog(this, "Journey Added!");
                clearFields();
                loadJourneys();
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid input. Check date, time, seats and price."
            );
        }
    }

    // ---------------- LOAD JOURNEYS ----------------

    private void loadJourneys() {

        model.setRowCount(0);

        for (Journey j : dao.getJourneys(agencyId)) {

            model.addRow(new Object[]{
                    j.getJourneyId(),
                    j.getBusName(),
                    j.getBusNumber(),
                    j.getSource(),
                    j.getDestination(),
                    j.getJourneyDate(),
                    j.getDepartureTime(),
                    j.getArrivalTime(),
                    j.getTotalSeats(),
                    j.getAvailableSeats(),
                    j.getPrice()
            });
        }
    }

    // ---------------- FILL FIELDS ----------------

    private void fillFields() {

        int row = table.getSelectedRow();

        busNameField.setText(model.getValueAt(row, 1).toString());
        busNumberField.setText(model.getValueAt(row, 2).toString());
        sourceField.setText(model.getValueAt(row, 3).toString());
        destinationField.setText(model.getValueAt(row, 4).toString());
        dateField.setText(model.getValueAt(row, 5).toString());
        departureField.setText(model.getValueAt(row, 6).toString());
        arrivalField.setText(model.getValueAt(row, 7).toString());
        seatsField.setText(model.getValueAt(row, 8).toString());
        priceField.setText(model.getValueAt(row, 10).toString());
    }

    // ---------------- UPDATE JOURNEY ----------------

    private void updateJourney() {

        int row = table.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this, "Select a journey first."
            );
            return;
        }

        try {

            Journey j = getJourneyFromFields();

            int journeyId = Integer.parseInt(
                    model.getValueAt(row, 0).toString()
            );

            int availableSeats = Integer.parseInt(
                    model.getValueAt(row, 9).toString()
            );

            int originalTotal = Integer.parseInt(
                    model.getValueAt(row, 8).toString()
            );

            int bookedSeats = originalTotal - availableSeats;

            if (j.getTotalSeats() < bookedSeats) {

                JOptionPane.showMessageDialog(
                        this,
                        "Cannot reduce seats below booked seats."
                );
                return;
            }

            j.setJourneyId(journeyId);
            j.setAvailableSeats(j.getTotalSeats() - bookedSeats);

            if (dao.updateJourney(j)) {

                JOptionPane.showMessageDialog(this, "Journey Updated!");
                clearFields();
                loadJourneys();
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(this, "Invalid input.");
        }
    }

    // ---------------- DELETE JOURNEY ----------------

    private void deleteJourney() {

        int row = table.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this, "Select a journey first."
            );
            return;
        }

        int journeyId = Integer.parseInt(
                model.getValueAt(row, 0).toString()
        );

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this journey?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {

            if (dao.deleteJourney(journeyId, agencyId)) {

                JOptionPane.showMessageDialog(this, "Journey Deleted!");
                clearFields();
                loadJourneys();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Cannot delete journey. It may have existing bookings."
                );
            }
        }
    }

    // ---------------- CLEAR FIELDS ----------------

    private void clearFields() {

        busNameField.setText("");
        busNumberField.setText("");
        sourceField.setText("");
        destinationField.setText("");
        dateField.setText("");
        departureField.setText("");
        arrivalField.setText("");
        seatsField.setText("");
        priceField.setText("");

        table.clearSelection();
    }
}
