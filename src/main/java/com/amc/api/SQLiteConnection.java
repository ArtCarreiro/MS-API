package com.amc.api;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteConnection {

    public static Connection connect() {
        String url = "jdbc:sqlite:database.db";

        try {
            return DriverManager.getConnection(url);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar ao SQLite", e);
        }
    }
}
