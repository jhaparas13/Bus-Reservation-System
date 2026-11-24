
package com.paras.BusReservation.ui;

import com.paras.BusReservation.dao.JourneyDAO;
import com.paras.BusReservation.model.Journey;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;
import java.util.List;

public class SearchJourneyFrame extends JFrame {

    private int userId;

    private JComboBox<String> sourceBox;
    private JComboBox<String> destinationBox;
    private JDateChooser dateChooser;

    private JTable table;
    private DefaultTableModel model;

    private JourneyDAO dao = new JourneyDAO();

    private final Color NAVY = new Color(15, 39, 75);
    private final Color BLUE = new Color(25, 105, 210);
    private final Color GREEN = new Color(35, 145, 95);
    private final Color LIGHT_BLUE = new Color(235, 242, 252);
    private final Color LIGHT_BG = new Color(245, 248, 253);
    private final Color BORDER = new Color(215, 223, 235);
    private final Color TEXT = new Color(45, 55, 70);

    public SearchJourneyFrame(int userId) {

        this.userId = userId;

        setTitle("Bus Reservation System - Search Journeys");
        setSize(1200, 750);
        setMinimumSize(new Dimension(1000, 650));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // MAIN PANEL
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(LIGHT_BG);
        mainPanel.setBorder(new EmptyBorder(15, 18, 18, 18));

        // ---------------- HEADER ----------------

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(NAVY);
        header.setBorder(new EmptyBorder(18, 22, 18, 22));

        JLabel heading = new JLabel("Search Bus Journeys");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 23));
        heading.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel(
                "Find your route, choose your journey date and book your seat"
        );
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(new Color(210, 220, 235));

        JPanel headingPanel = new JPanel();
        headingPanel.setLayout(new BoxLayout(headingPanel, BoxLayout.Y_AXIS));
        headingPanel.setOpaque(false);
        headingPanel.add(heading);
        headingPanel.add(Box.createVerticalStrut(5));
        headingPanel.add(subtitle);

        header.add(headingPanel, BorderLayout.WEST);

        // ---------------- SEARCH FORM ----------------

        JPanel searchPanel = new JPanel(new GridLayout(2, 3, 15, 10));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(BORDER),
                        "Search Journeys"
                ),
                new EmptyBorder(12, 15, 15, 15)
        ));

        sourceBox = new JComboBox<>();
        destinationBox = new JComboBox<>();

        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("yyyy-MM-dd");
        dateChooser.setPreferredSize(new Dimension(180, 36));
        dateChooser.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        styleComboBox(sourceBox);
        styleComboBox(destinationBox);

        searchPanel.add(createLabel("Source"));
        searchPanel.add(createLabel("Destination"));
        searchPanel.add(createLabel("Journey Date"));

        searchPanel.add(sourceBox);
        searchPanel.add(destinationBox);
        searchPanel.add(dateChooser);

        // ---------------- BUTTONS ----------------

        JButton searchButton = new JButton("Search Journeys");
        JButton refreshButton = new JButton("Show All Journeys");
        JButton bookButton = new JButton("Select Seats");

        styleButton(searchButton, BLUE, Color.WHITE);
        styleButton(refreshButton, LIGHT_BLUE, NAVY);
        styleButton(bookButton, GREEN, Color.WHITE);

        searchButton.setPreferredSize(new Dimension(155, 38));
        refreshButton.setPreferredSize(new Dimension(175, 38));
        bookButton.setPreferredSize(new Dimension(145, 38));

        JPanel buttonPanel = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 10, 8)
        );
        buttonPanel.setBackground(LIGHT_BG);

        buttonPanel.add(searchButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(bookButton);

        JPanel topContent = new JPanel(new BorderLayout(0, 12));
        topContent.setBackground(LIGHT_BG);
        topContent.add(searchPanel, BorderLayout.CENTER);
        topContent.add(buttonPanel, BorderLayout.SOUTH);

        JPanel topPanel = new JPanel(new BorderLayout(0, 12));
        topPanel.setBackground(LIGHT_BG);
        topPanel.add(header, BorderLayout.NORTH);
        topPanel.add(topContent, BorderLayout.CENTER);

        // ---------------- JOURNEY TABLE ----------------

        String[] columns = {
                "ID", "Bus Name", "Bus Number", "Source",
                "Destination", "Date", "Departure", "Arrival",
                "Available Seats", "Total Seats", "Price"
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
        table.setGridColor(new Color(232, 237, 245));
        table.setShowVerticalLines(false);
        table.setSelectionBackground(new Color(220, 235, 255));
        table.setSelectionForeground(TEXT);
        table.setAutoCreateRowSorter(true);

        table.getTableHeader().setFont(
                new Font("Segoe UI", Font.BOLD, 13)
        );
        table.getTableHeader().setBackground(NAVY);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));
        table.getTableHeader().setReorderingAllowed(false);

        // Zebra row styling
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {

            @Override
            public Component getTableCellRendererComponent(
                    JTable table,
                    Object value,
                    boolean isSelected,
                    boolean hasFocus,
                    int row,
                    int column) {

                Component component = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column
                );

                if (isSelected) {
                    component.setBackground(new Color(220, 235, 255));
                } else {
                    component.setBackground(
                            row % 2 == 0
                                    ? Color.WHITE
                                    : new Color(247, 249, 252)
                    );
                }

                component.setForeground(TEXT);
                setBorder(new EmptyBorder(0, 8, 0, 8));

                return component;
            }
        };

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel()
                    .getColumn(i)
                    .setCellRenderer(renderer);
        }

        int[] widths = {
                50, 125, 110, 115, 115,
                100, 95, 95, 115, 95, 85
        };

        for (int i = 0; i < widths.length; i++) {
            table.getColumnModel()
                    .getColumn(i)
                    .setPreferredWidth(widths[i]);
        }

        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(BORDER),
                        "Available Journeys"
                ),
                new EmptyBorder(5, 5, 5, 5)
        ));
        scrollPane.getViewport().setBackground(Color.WHITE);

        // ---------------- ADD TO FRAME ----------------

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);

        // ---------------- ACTION LISTENERS ----------------

        loadLocations();

        searchButton.addActionListener(e -> searchJourneys());
        refreshButton.addActionListener(e -> loadAllJourneys());
        bookButton.addActionListener(e -> bookSelectedJourney());

        loadAllJourneys();

        setVisible(true);
    }

    // ---------------- STYLING METHODS ----------------

    private JLabel createLabel(String text) {

        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(TEXT);

        return label;
    }

    private void styleComboBox(JComboBox<String> comboBox) {

        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        comboBox.setBackground(Color.WHITE);
        comboBox.setPreferredSize(new Dimension(180, 36));
    }

    private void styleButton(
            JButton button,
            Color background,
            Color foreground) {

        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(foreground);
        button.setBackground(background);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(140, 38));
    }

    // ---------------- LOAD LOCATIONS ----------------

    private void loadLocations() {

        sourceBox.removeAllItems();
        destinationBox.removeAllItems();

        sourceBox.addItem("Select Source");
        destinationBox.addItem("Select Destination");

        List<String> locations = dao.getAllLocations();

        for (String location : locations) {
            sourceBox.addItem(location);
            destinationBox.addItem(location);
        }
    }

    // ---------------- LOAD ALL JOURNEYS ----------------

    private void loadAllJourneys() {

        model.setRowCount(0);

        for (Journey j : dao.getAllAvailableJourneys()) {
            addJourneyToTable(j);
        }
    }

    // ---------------- SEARCH JOURNEYS ----------------

    private void searchJourneys() {

        String source = (String) sourceBox.getSelectedItem();
        String destination = (String) destinationBox.getSelectedItem();

        Date selectedDate = dateChooser.getDate();

        if (source == null || source.equals("Select Source") ||
                destination == null ||
                destination.equals("Select Destination")) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select source and destination."
            );
            return;
        }

        if (source.equalsIgnoreCase(destination)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Source and destination cannot be the same."
            );
            return;
        }

        if (selectedDate == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a journey date."
            );
            return;
        }

        java.sql.Date sqlDate =
                new java.sql.Date(selectedDate.getTime());

        model.setRowCount(0);

        List<Journey> results =
                dao.searchJourneys(source, destination, sqlDate);

        for (Journey j : results) {
            addJourneyToTable(j);
        }

        if (results.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "No journeys found for the selected route and date."
            );
        }
    }

    // ---------------- ADD JOURNEY TO TABLE ----------------

    private void addJourneyToTable(Journey j) {

        model.addRow(new Object[]{
                j.getJourneyId(),
                j.getBusName(),
                j.getBusNumber(),
                j.getSource(),
                j.getDestination(),
                j.getJourneyDate(),
                j.getDepartureTime(),
                j.getArrivalTime(),
                j.getAvailableSeats(),
                j.getTotalSeats(),
                j.getPrice()
        });
    }

    // ---------------- BOOK SELECTED JOURNEY ----------------

    private void bookSelectedJourney() {

        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a journey first."
            );
            return;
        }

        int row = table.convertRowIndexToModel(selectedRow);

        int journeyId = Integer.parseInt(
                model.getValueAt(row, 0).toString()
        );

        int availableSeats = Integer.parseInt(
                model.getValueAt(row, 8).toString()
        );

        int totalSeats = Integer.parseInt(
                model.getValueAt(row, 9).toString()
        );

        double price = Double.parseDouble(
                model.getValueAt(row, 10).toString()
        );

        if (availableSeats <= 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "No seats are currently available."
            );
            return;
        }

        SeatSelectionFrame seatFrame = new SeatSelectionFrame(
                userId,
                journeyId,
                totalSeats,
                availableSeats,
                price
        );

        seatFrame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosed(WindowEvent e) {
                loadAllJourneys();
            }
        });
    }
}
