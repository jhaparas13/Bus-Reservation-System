
package com.paras.BusReservation.dao;

import com.paras.BusReservation.database.DBConnection;
import com.paras.BusReservation.model.Journey;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JourneyDAO {

    // ADD JOURNEY
    public boolean addJourney(Journey j) {

        String sql = "INSERT INTO journeys " +
                "(agency_id, bus_name, bus_number, source, destination, " +
                "journey_date, departure_time, arrival_time, " +
                "total_seats, available_seats, price) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (con == null) return false;

            ps.setInt(1, j.getAgencyId());
            ps.setString(2, j.getBusName());
            ps.setString(3, j.getBusNumber());
            ps.setString(4, j.getSource());
            ps.setString(5, j.getDestination());
            ps.setDate(6, j.getJourneyDate());
            ps.setTime(7, j.getDepartureTime());
            ps.setTime(8, j.getArrivalTime());
            ps.setInt(9, j.getTotalSeats());
            ps.setInt(10, j.getTotalSeats());
            ps.setDouble(11, j.getPrice());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // VIEW ALL JOURNEYS OF AN AGENCY
    public List<Journey> getJourneys(int agencyId) {

        List<Journey> list = new ArrayList<>();

        String sql = "SELECT * FROM journeys WHERE agency_id = ?";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) return list;

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, agencyId);

                try (ResultSet rs = ps.executeQuery()) {

                    while (rs.next()) {

                        Journey j = new Journey();

                        j.setJourneyId(rs.getInt("journey_id"));
                        j.setAgencyId(rs.getInt("agency_id"));
                        j.setBusName(rs.getString("bus_name"));
                        j.setBusNumber(rs.getString("bus_number"));
                        j.setSource(rs.getString("source"));
                        j.setDestination(rs.getString("destination"));
                        j.setJourneyDate(rs.getDate("journey_date"));
                        j.setDepartureTime(rs.getTime("departure_time"));
                        j.setArrivalTime(rs.getTime("arrival_time"));
                        j.setTotalSeats(rs.getInt("total_seats"));
                        j.setAvailableSeats(rs.getInt("available_seats"));
                        j.setPrice(rs.getDouble("price"));

                        list.add(j);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // UPDATE JOURNEY
    public boolean updateJourney(Journey j) {

        String sql = "UPDATE journeys SET bus_name=?, bus_number=?, " +
                "source=?, destination=?, journey_date=?, " +
                "departure_time=?, arrival_time=?, total_seats=?, " +
                "available_seats=?, price=? " +
                "WHERE journey_id=? AND agency_id=?";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) return false;

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, j.getBusName());
                ps.setString(2, j.getBusNumber());
                ps.setString(3, j.getSource());
                ps.setString(4, j.getDestination());
                ps.setDate(5, j.getJourneyDate());
                ps.setTime(6, j.getDepartureTime());
                ps.setTime(7, j.getArrivalTime());
                ps.setInt(8, j.getTotalSeats());
                ps.setInt(9, j.getAvailableSeats());
                ps.setDouble(10, j.getPrice());
                ps.setInt(11, j.getJourneyId());
                ps.setInt(12, j.getAgencyId());

                return ps.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // DELETE JOURNEY
    public boolean deleteJourney(int journeyId, int agencyId) {

        String sql = "DELETE FROM journeys WHERE journey_id=? AND agency_id=?";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) return false;

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, journeyId);
                ps.setInt(2, agencyId);

                return ps.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Journey> getAllAvailableJourneys() {

        List<Journey> journeys = new ArrayList<>();

        String sql = "SELECT * FROM journeys " +
                "WHERE available_seats > 0 AND journey_date >= CURDATE() " +
                "ORDER BY journey_date, departure_time";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) return journeys;

            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    journeys.add(mapJourney(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return journeys;
    }


    public List<Journey> searchJourneys(String source, String destination, Date date) {

        List<Journey> journeys = new ArrayList<>();

        String sql = "SELECT * FROM journeys " +
                "WHERE LOWER(source) = LOWER(?) " +
                "AND LOWER(destination) = LOWER(?) " +
                "AND journey_date = ? " +
                "AND available_seats > 0 " +
                "ORDER BY departure_time";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) return journeys;

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, source);
                ps.setString(2, destination);
                ps.setDate(3, date);

                try (ResultSet rs = ps.executeQuery()) {

                    while (rs.next()) {
                        journeys.add(mapJourney(rs));
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return journeys;
    }


    private Journey mapJourney(ResultSet rs) throws SQLException {

        Journey j = new Journey();

        j.setJourneyId(rs.getInt("journey_id"));
        j.setAgencyId(rs.getInt("agency_id"));
        j.setBusName(rs.getString("bus_name"));
        j.setBusNumber(rs.getString("bus_number"));
        j.setSource(rs.getString("source"));
        j.setDestination(rs.getString("destination"));
        j.setJourneyDate(rs.getDate("journey_date"));
        j.setDepartureTime(rs.getTime("departure_time"));
        j.setArrivalTime(rs.getTime("arrival_time"));
        j.setTotalSeats(rs.getInt("total_seats"));
        j.setAvailableSeats(rs.getInt("available_seats"));
        j.setPrice(rs.getDouble("price"));

        return j;
    }

    public List<String> getAllLocations() {

        List<String> locations = new ArrayList<>();

        String sql = "SELECT source AS location FROM journeys " +
                "UNION " +
                "SELECT destination AS location FROM journeys " +
                "ORDER BY location";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) return locations;

            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    locations.add(rs.getString("location"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return locations;
    }


}
