
package com.paras.BusReservation.dao;

import com.paras.BusReservation.database.DBConnection;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AuthDAO {

    public String login(String email, String password) {

        String sql = "SELECT user_id, password, role FROM users WHERE email = ?";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) return null;

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, email);

                try (ResultSet rs = ps.executeQuery()) {

                    if (rs.next()) {

                        String storedPassword = rs.getString("password");
                        String role = rs.getString("role");

                        boolean passwordMatches;

                        // Check whether the stored password is BCrypt
                        if (storedPassword.startsWith("$2a$") ||
                                storedPassword.startsWith("$2b$") ||
                                storedPassword.startsWith("$2y$")) {

                            passwordMatches = BCrypt.checkpw(
                                    password, storedPassword
                            );

                        } else {
                            // Legacy plaintext password
                            passwordMatches = storedPassword.equals(password);

                            // Upgrade plaintext password to BCrypt
                            if (passwordMatches) {

                                String hashedPassword =
                                        BCrypt.hashpw(
                                                password,
                                                BCrypt.gensalt(12)
                                        );

                                String updateSql =
                                        "UPDATE users SET password = ? WHERE user_id = ?";

                                try (PreparedStatement updatePs =
                                             con.prepareStatement(updateSql)) {

                                    updatePs.setString(1, hashedPassword);
                                    updatePs.setInt(2, rs.getInt("user_id"));
                                    updatePs.executeUpdate();
                                }
                            }
                        }

                        if (passwordMatches) {
                            return role;
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean register(String name, String email,
                            String password, String phone) {

        String sql = "INSERT INTO users (name, email, password, phone, role) " +
                "VALUES (?, ?, ?, ?, 'CUSTOMER')";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) return false;

            String hashedPassword = BCrypt.hashpw(
                    password,
                    BCrypt.gensalt(12)
            );

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, name);
                ps.setString(2, email);
                ps.setString(3, hashedPassword);
                ps.setString(4, phone);

                return ps.executeUpdate() > 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public int getUserId(String email) {

        String sql = "SELECT user_id FROM users WHERE email = ?";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) return -1;

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, email);

                try (ResultSet rs = ps.executeQuery()) {

                    if (rs.next()) {
                        return rs.getInt("user_id");
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }
}
