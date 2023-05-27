package com.main.racerstore;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBHelper {
    private static final String URL = "jdbc:sqlserver://RacerStore:1433;databaseName=RacerStore";
    private static final String USER = "";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        // Agregar breakpoint en la línea siguiente
        return conn;
    }
}

