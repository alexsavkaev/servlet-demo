package ru.savkaev.servletdemo.utility;

import lombok.AllArgsConstructor;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@AllArgsConstructor
public class DataBaseInit {
    private String url;
    private String username;
    private String password;

    public void initializeDatabase() throws SQLException {
        try (DataBaseConnection connection = new DataBaseConnection())
        {
            connection.connect(url, username, password);
            String tableName = "notes";
            String createTableQuery = "CREATE TABLE IF NOT EXISTS " +
                    tableName +
                    " (id SERIAL PRIMARY KEY, title VARCHAR(255), text TEXT," +
                    " created_by VARCHAR(255), creation_date DATE, update_date DATE)";

            try(PreparedStatement statement = connection.getConnection().prepareStatement(createTableQuery))
            {
                statement.execute();
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
}

