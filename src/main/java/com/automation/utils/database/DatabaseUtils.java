package com.automation.utils.database;

import com.automation.utils.config.ConfigReader;
import com.automation.utils.logging.Log;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generic JDBC database utility for executing test queries and validating database state.
 */
public final class DatabaseUtils {

    private DatabaseUtils() {}

    private static Connection getConnection() throws SQLException {
        String url = ConfigReader.get("db.url", "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        String username = ConfigReader.get("db.username", "sa");
        String password = ConfigReader.get("db.password", "");
        return DriverManager.getConnection(url, username, password);
    }

    public static List<Map<String, Object>> executeQuery(String query) {
        List<Map<String, Object>> results = new ArrayList<>();
        Log.info("Executing DB Query: %s", query);

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(metaData.getColumnLabel(i), rs.getObject(i));
                }
                results.add(row);
            }
        } catch (SQLException e) {
            Log.error("Error executing query: " + e.getMessage(), e);
        }
        return results;
    }

    public static int executeUpdate(String sql) {
        Log.info("Executing DB Update: %s", sql);
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            return stmt.executeUpdate(sql);
        } catch (SQLException e) {
            Log.error("Error executing SQL update: " + e.getMessage(), e);
            return -1;
        }
    }
}
