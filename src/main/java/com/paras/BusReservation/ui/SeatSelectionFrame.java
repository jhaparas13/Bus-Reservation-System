
package com.paras.BusReservation.ui;

import com.paras.BusReservation.dao.BookingDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SeatSelectionFrame extends JFrame {

    private final int userId;
    private final int journeyId;
    private final int totalSeats;
    private final int availableSeats;
    private final double price;

    private final BookingDAO bookingDAO = new BookingDAO();

    private final Set<String> bookedSeats;
    private final Set<String> selectedSeats = new HashSet<>();

    private final JLabel selectedLabel = new JLabel("Selected seats: 0");
    private final JLabel totalLabel = new JLabel("Total: ₹0.00");

    private final JComboBox<String> paymentBox =
            new JComboBox<>(new String[]{"UPI", "CARD", "CASH"});

    private final Color availableColor = new Color(144, 238, 144);
    private final Color bookedColor = new Color(240, 128, 128);
    private final Color selectedColor = new Color(100, 149, 237);

    // Coordinates correspond to the prepared 1160 x 296 background image.
    // Order is front to rear.
    private static final int[][] SEAT_BOUNDS = {
            {923,46,49,39}, {923,87,49,42},
            {923,174,49,41}, {923,218,49,39},

            {841,46,49,39}, {841,87,49,42},
            {841,174,49,41}, {841,218,49,39},

            {759,46,49,39}, {759,87,49,42},
            {759,174,49,41}, {759,218,49,39},

            {676,46,49,39}, {676,87,49,42},
            {676,174,49,41}, {676,218,49,39},

            {594,46,49,41}, {594,89,49,40},
            {594,174,49,41}, {594,218,49,39},

            {510,46,49,41}, {510,89,49,40},

            {402,46,49,39}, {402,87,49,42},

            {379,174,48,41}, {379,218,48,39},

            {296,46,49,39}, {296,87,49,42},
            {296,174,49,41}, {296,218,49,39},

            {214,46,49,39}, {214,87,48,42},
            {214,174,48,42}, {214,218,48,39},

            {131,46,49,39}, {131,87,49,42},
            {131,174,49,41}, {131,218,49,39},

            {48,46,49,39}, {48,87,49,42},
            {48,131,49,41}, {48,174,49,41},
            {48,218,49,39}
    };

    private static final String[] SEAT_NUMBERS = {
            "1A","1B","1C","1D",
            "2A","2B","2C","2D",
            "3A","3B","3C","3D",
            "4A","4B","4C","4D",
            "5A","5B","5C","5D",
            "6A","6B",
            "7A","7B",
            "8A","8B",
            "9A","9B","9C","9D",
            "10A","10B","10C","10D",
            "11A","11B","11C","11D",
            "12A","12B","12C","12D","12E"
    };

    public SeatSelectionFrame(int userId, int journeyId,
                              int totalSeats, int availableSeats,
                              double price) {

        this.userId = userId;
        this.journeyId = journeyId;
        this.totalSeats = totalSeats;
        this.availableSeats = availableSeats;
        this.price = price;

        if (totalSeats < 1 || totalSeats > 43) {
            throw new IllegalArgumentException(
                    "This bus layout supports 1 to 43 seats."
            );
        }

        this.bookedSeats = bookingDAO.getBookedSeats(journeyId);

        setTitle("Select Your Seats");
        setSize(1230, 570);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new BorderLayout(10, 10));

        add(createHeader(), BorderLayout.NORTH);
        add(createBusPanel(), BorderLayout.CENTER);
        add(createBottomPanel(), BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel createHeader() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(25, 55, 95));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        JLabel title = new JLabel("CHOOSE YOUR SEATS");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 24));

        panel.add(title, BorderLayout.WEST);

        return panel;
    }

    private JPanel createBusPanel() {

        BufferedImage busImage;

        try {
            busImage = ImageIO.read(
                    getClass().getResource(
                            "/images/bus_seat_background_horizontal.png"
                    )
            );
        } catch (Exception e) {
            throw new RuntimeException(
                    "Could not load bus background image. Check resources/images.",
                    e
            );
        }

        JPanel busPanel = new JPanel(null) {

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(busImage, 0, 0, 1160, 296, null);
            }
        };

        busPanel.setPreferredSize(new Dimension(1160, 296));
        busPanel.setBackground(Color.WHITE);

        // Create clickable seats
        for (int i = 0; i < SEAT_NUMBERS.length; i++) {

            if (i >= totalSeats) {
                break;
            }

            String seatNumber = SEAT_NUMBERS[i];

            int[] bounds = SEAT_BOUNDS[i];

            JButton seatButton = new JButton(seatNumber);

            seatButton.setBounds(
                    bounds[0] + 2,
                    bounds[1] + 2,
                    bounds[2] - 4,
                    bounds[3] - 4
            );

            seatButton.setMargin(new Insets(0, 0, 0, 0));
            seatButton.setFont(new Font("Arial", Font.BOLD, 10));

            if (bookedSeats.contains(seatNumber)) {

                seatButton.setBackground(bookedColor);
                seatButton.setEnabled(false);

            } else {

                seatButton.setBackground(availableColor);

                seatButton.addActionListener(e ->
                        toggleSeat(seatNumber, seatButton)
                );
            }

            seatButton.setOpaque(true);
            seatButton.setBorderPainted(false);

            busPanel.add(seatButton);
        }

        JPanel wrapper = new JPanel(new FlowLayout(
                FlowLayout.CENTER, 0, 10
        ));

        wrapper.add(busPanel);

        return wrapper;
    }

    private JPanel createLegendItem(Color color, String text) {

        JPanel panel = new JPanel(new FlowLayout(
                FlowLayout.CENTER, 5, 0
        ));

        JLabel colorBox = new JLabel("    ");
        colorBox.setOpaque(true);
        colorBox.setBackground(color);

        panel.add(colorBox);
        panel.add(new JLabel(text));

        return panel;
    }

    private JPanel createBottomPanel() {

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 25, 15, 25));

        JPanel legend = new JPanel(new FlowLayout(
                FlowLayout.CENTER, 20, 5
        ));

        legend.add(createLegendItem(availableColor, "Available"));
        legend.add(createLegendItem(bookedColor, "Booked"));
        legend.add(createLegendItem(selectedColor, "Selected"));

        JPanel summary = new JPanel(new FlowLayout(
                FlowLayout.CENTER, 25, 8
        ));

        JButton confirmButton = new JButton("Confirm Booking");

        summary.add(selectedLabel);
        summary.add(totalLabel);
        summary.add(new JLabel("Payment Method:"));
        summary.add(paymentBox);
        summary.add(confirmButton);

        confirmButton.addActionListener(e -> confirmBooking());

        panel.add(legend, BorderLayout.NORTH);
        panel.add(summary, BorderLayout.CENTER);

        return panel;
    }

    private void toggleSeat(String seatNumber, JButton button) {

        if (selectedSeats.contains(seatNumber)) {

            selectedSeats.remove(seatNumber);
            button.setBackground(availableColor);

        } else {

            if (selectedSeats.size() >= availableSeats) {
                JOptionPane.showMessageDialog(this,
                        "Only " + availableSeats + " seats are available.");
                return;
            }

            selectedSeats.add(seatNumber);
            button.setBackground(selectedColor);
        }

        updateSummary();
    }

    private void updateSummary() {

        selectedLabel.setText(
                "Selected seats: " + selectedSeats.size()
        );

        totalLabel.setText(
                String.format("Total: ₹%.2f",
                        price * selectedSeats.size())
        );
    }

    private void confirmBooking() {

        if (selectedSeats.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please select at least one seat.");
            return;
        }

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Selected seats: " + selectedSeats +
                        "\nTotal amount: ₹" +
                        (price * selectedSeats.size()) +
                        "\nConfirm booking?",
                "Confirm Booking",
                JOptionPane.YES_NO_OPTION
        );

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        String paymentMethod =
                paymentBox.getSelectedItem().toString();

        boolean success = bookingDAO.bookJourney(
                userId,
                journeyId,
                new ArrayList<>(selectedSeats),
                paymentMethod
        );

        if (success) {

            JOptionPane.showMessageDialog(this,
                    "Booking confirmed!\nSeats: " + selectedSeats);

            dispose();

        } else {

            JOptionPane.showMessageDialog(this,
                    "Booking failed. Please refresh and try again.");
        }
    }
}
