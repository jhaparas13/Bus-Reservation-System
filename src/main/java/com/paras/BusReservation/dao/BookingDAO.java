
package com.paras.BusReservation.dao;

import com.paras.BusReservation.database.DBConnection;

import java.time.LocalDate;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BookingDAO {

    public List<Object[]> getCustomerBookings(int userId) {

        List<Object[]> bookings = new ArrayList<>();

        String sql = "SELECT b.booking_id, j.bus_name, j.source, " +
                "j.destination, j.journey_date, b.seats_booked, " +
                "b.total_amount, b.payment_method, b.payment_status, b.status " +
                "FROM bookings b " +
                "JOIN journeys j ON b.journey_id = j.journey_id " +
                "WHERE b.user_id = ? " +
                "ORDER BY b.booking_date DESC";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) return bookings;

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, userId);

                try (ResultSet rs = ps.executeQuery()) {

                    while (rs.next()) {

                        bookings.add(new Object[]{
                                rs.getInt("booking_id"),
                                rs.getString("bus_name"),
                                rs.getString("source"),
                                rs.getString("destination"),
                                rs.getDate("journey_date"),
                                rs.getInt("seats_booked"),
                                rs.getBigDecimal("total_amount"),
                                rs.getString("payment_method"),
                                rs.getString("payment_status"),
                                rs.getString("status")
                        });
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bookings;
    }



    public boolean bookJourney(int userId, int journeyId,
                               List<String> selectedSeats,
                               String paymentMethod) {

        if (selectedSeats == null || selectedSeats.isEmpty()) {
            return false;
        }

        // Normalize seat numbers and reject duplicates
        List<String> seats = new ArrayList<>();

        for (String seat : selectedSeats) {
            if (seat == null || seat.trim().isEmpty()) {
                return false;
            }

            seats.add(seat.trim().toUpperCase());
        }

        if (new HashSet<>(seats).size() != seats.size()) {
            return false;
        }

        String journeySql =
                "SELECT total_seats, available_seats, price, journey_date " +
                        "FROM journeys WHERE journey_id = ? FOR UPDATE";

        String bookingSql =
                "INSERT INTO bookings " +
                        "(user_id, journey_id, seats_booked, total_amount, " +
                        "status, payment_method, payment_status) " +
                        "VALUES (?, ?, ?, ?, 'CONFIRMED', ?, 'PAID')";

        String seatSql =
                "INSERT INTO booking_seats " +
                        "(booking_id, journey_id, seat_number) VALUES (?, ?, ?)";

        String updateSql =
                "UPDATE journeys SET available_seats = available_seats - ? " +
                        "WHERE journey_id = ? AND available_seats >= ?";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) return false;

            con.setAutoCommit(false);

            try {
                int totalSeats;
                int availableSeats;
                double price;

                // Lock journey and retrieve details
                try (PreparedStatement ps = con.prepareStatement(journeySql)) {

                    ps.setInt(1, journeyId);

                    try (ResultSet rs = ps.executeQuery()) {

                        if (!rs.next()) {
                            con.rollback();
                            return false;
                        }

                        totalSeats = rs.getInt("total_seats");
                        availableSeats = rs.getInt("available_seats");
                        price = rs.getDouble("price");

                        java.sql.Date journeyDate =
                                rs.getDate("journey_date");

                        if (journeyDate == null ||
                                journeyDate.toLocalDate().isBefore(LocalDate.now())) {
                            con.rollback();
                            return false;
                        }
                    }
                }

                // Validate seat numbers against bus capacity
                // Validate seat numbers against the actual bus layout
                int[] seatsPerRow = {
                        4, 4, 4, 4, 4,
                        2, 2, 2,
                        4, 4, 4, 5
                };

                Set<String> validSeats = new HashSet<>();

                int seatCounter = 0;

                for (int row = 1; row <= seatsPerRow.length; row++) {

                    for (int col = 0; col < seatsPerRow[row - 1]; col++) {

                        seatCounter++;

                        if (seatCounter <= totalSeats) {
                            validSeats.add(
                                    row + String.valueOf((char) ('A' + col))
                            );
                        }
                    }
                }

                if (totalSeats <= 0 || totalSeats > 43 ||
                        !validSeats.containsAll(seats) ||
                        seats.size() > availableSeats) {

                    con.rollback();
                    return false;
                }

                double totalAmount = price * seats.size();

                // Create booking
                int bookingId;

                try (PreparedStatement ps = con.prepareStatement(
                        bookingSql, java.sql.Statement.RETURN_GENERATED_KEYS)) {

                    ps.setInt(1, userId);
                    ps.setInt(2, journeyId);
                    ps.setInt(3, seats.size());
                    ps.setDouble(4, totalAmount);
                    ps.setString(5, paymentMethod);

                    ps.executeUpdate();

                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (!rs.next()) {
                            con.rollback();
                            return false;
                        }

                        bookingId = rs.getInt(1);
                    }
                }

                // Insert individual seat allocations
                try (PreparedStatement ps = con.prepareStatement(seatSql)) {

                    for (String seat : seats) {
                        ps.setInt(1, bookingId);
                        ps.setInt(2, journeyId);
                        ps.setString(3, seat);
                        ps.addBatch();
                    }

                    ps.executeBatch();
                }

                // Update available seats
                try (PreparedStatement ps = con.prepareStatement(updateSql)) {

                    ps.setInt(1, seats.size());
                    ps.setInt(2, journeyId);
                    ps.setInt(3, seats.size());

                    if (ps.executeUpdate() != 1) {
                        con.rollback();
                        return false;
                    }
                }

                con.commit();
                return true;

            } catch (Exception e) {
                con.rollback();
                e.printStackTrace();
                return false;

            } finally {
                con.setAutoCommit(true);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean cancelBooking(int userId, int bookingId) {

        String selectSql =
                "SELECT journey_id, seats_booked, status " +
                        "FROM bookings " +
                        "WHERE booking_id = ? AND user_id = ? FOR UPDATE";

        String updateBookingSql =
                "UPDATE bookings SET status = 'CANCELLED' " +
                        "WHERE booking_id = ? AND user_id = ? " +
                        "AND status = 'CONFIRMED'";

        String restoreSeatsSql =
                "UPDATE journeys SET available_seats = " +
                        "LEAST(total_seats, available_seats + ?) " +
                        "WHERE journey_id = ?";

        String deleteSeatsSql =
                "DELETE FROM booking_seats WHERE booking_id = ?";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) return false;

            con.setAutoCommit(false);

            try {

                int journeyId;
                int seatsBooked;

                // Lock the booking row
                try (PreparedStatement ps = con.prepareStatement(selectSql)) {

                    ps.setInt(1, bookingId);
                    ps.setInt(2, userId);

                    try (ResultSet rs = ps.executeQuery()) {

                        if (!rs.next() ||
                                !"CONFIRMED".equals(rs.getString("status"))) {

                            con.rollback();
                            return false;
                        }

                        journeyId = rs.getInt("journey_id");
                        seatsBooked = rs.getInt("seats_booked");
                    }
                }

                // Mark booking as cancelled
                try (PreparedStatement ps =
                             con.prepareStatement(updateBookingSql)) {

                    ps.setInt(1, bookingId);
                    ps.setInt(2, userId);

                    if (ps.executeUpdate() != 1) {
                        con.rollback();
                        return false;
                    }
                }

                // Restore available seats
                try (PreparedStatement ps =
                             con.prepareStatement(restoreSeatsSql)) {

                    ps.setInt(1, seatsBooked);
                    ps.setInt(2, journeyId);

                    if (ps.executeUpdate() != 1) {
                        con.rollback();
                        return false;
                    }
                }
                try (PreparedStatement ps = con.prepareStatement(deleteSeatsSql)) {
                    ps.setInt(1, bookingId);
                    ps.executeUpdate();
                }

                con.commit();
                return true;

            } catch (Exception e) {

                con.rollback();
                e.printStackTrace();
                return false;

            } finally {
                con.setAutoCommit(true);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public List<Object[]> getAgencyBookings(int agencyId) {

        List<Object[]> bookings = new ArrayList<>();

        String sql = "SELECT b.booking_id, u.name, u.email, u.phone, " +
                "j.bus_name, j.bus_number, j.source, j.destination, " +
                "j.journey_date, b.seats_booked, b.total_amount, " +
                "b.payment_method, b.payment_status, b.status, b.booking_date " +
                "FROM bookings b " +
                "JOIN journeys j ON b.journey_id = j.journey_id " +
                "JOIN users u ON b.user_id = u.user_id " +
                "WHERE j.agency_id = ? " +
                "ORDER BY " +
                "CASE WHEN b.status = 'CONFIRMED' THEN 1 ELSE 2 END, " +
                "j.journey_date ASC, b.booking_date DESC";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) return bookings;

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, agencyId);

                try (ResultSet rs = ps.executeQuery()) {

                    while (rs.next()) {
                        bookings.add(new Object[]{
                                rs.getInt("booking_id"),
                                rs.getString("name"),
                                rs.getString("email"),
                                rs.getString("phone"),
                                rs.getString("bus_name"),
                                rs.getString("bus_number"),
                                rs.getString("source"),
                                rs.getString("destination"),
                                rs.getDate("journey_date"),
                                rs.getInt("seats_booked"),
                                rs.getBigDecimal("total_amount"),
                                rs.getString("payment_method"),
                                rs.getString("payment_status"),
                                rs.getString("status"),
                                rs.getTimestamp("booking_date")
                        });
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bookings;
    }
    public Set<String> getBookedSeats(int journeyId) {

        Set<String> bookedSeats = new HashSet<>();

        String sql = "SELECT bs.seat_number " +
                "FROM booking_seats bs " +
                "JOIN bookings b ON bs.booking_id = b.booking_id " +
                "WHERE bs.journey_id = ? " +
                "AND b.status = 'CONFIRMED'";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) return bookedSeats;

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, journeyId);

                try (ResultSet rs = ps.executeQuery()) {

                    while (rs.next()) {
                        bookedSeats.add(rs.getString("seat_number"));
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bookedSeats;
    }

}
