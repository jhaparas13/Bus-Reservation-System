package com.paras.BusReservation.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/bus_reservation";

    private static final String USER = System.getenv("DB_USER");

    private static final String PASSWORD = System.getenv("DB_PASSWORD");

    public static Connection getConnection() {

        try {
            Connection con = DriverManager.getConnection(
                    URL, USER, PASSWORD
            );

            System.out.println("Database Connected Successfully!");

            return con;

        } catch (SQLException e) {

            System.out.println("Database Connection Failed!");
            e.printStackTrace();

            return null;
        }
    }
}
