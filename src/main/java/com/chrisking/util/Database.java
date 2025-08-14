package com.chrisking.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class Database {
    private static final Properties PROPS = new Properties();

    static {
        try (InputStream in = Database.class.getClassLoader()
                .getResourceAsStream("db.properties")) {
            if (in == null) {
                throw new IllegalStateException(
                    "db.properties not found (expected at src/main/resources/db.properties)"
                );
            }
            PROPS.load(in);
        } catch (IOException e) {
            throw new ExceptionInInitializerError("Failed to load db.properties: " + e.getMessage());
        }
    }

    private Database() { /* no instances */ }

    public static Connection getConnection() throws SQLException {
        String url  = PROPS.getProperty("DB_URL");
        String user = PROPS.getProperty("DB_USER");
        String pass = PROPS.getProperty("DB_PASSWORD");
        if (url == null || user == null || pass == null) {
            throw new IllegalStateException("Missing DB_URL / DB_USER / DB_PASSWORD in db.properties");
        }
        return DriverManager.getConnection(url, user, pass);
    }
}
