package ru.savkaev.servletdemo.utility;

import lombok.AllArgsConstructor;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Инициализация базы данных. Создание таблиц notes и users. Заполнение таблицы users тестовыми данными
 */
@AllArgsConstructor
public class DataBaseInit {
    private String url;
    private String username;
    private String password;

    public void initializeDatabase() throws SQLException {
        try (DataBaseConnection connection = new DataBaseConnection()) {
            connection.connect(url, username, password);

            String tableName = "users";
            String createTableQuery = "CREATE TABLE IF NOT EXISTS " +
                    tableName +
                    "(" +
                    "id SERIAL PRIMARY KEY," +
                    " username VARCHAR(255) UNIQUE," +
                    "email VARCHAR(255)," +
                    "password VARCHAR(255), " +
                    "registration_date DATE, " +
                    "expiration_date DATE, " +
                    "active BOOLEAN" +
                    ")";

            try (PreparedStatement statement = connection.getConnection().prepareStatement(createTableQuery)) {
                statement.execute();
            }
            String insertUsersQuery = "INSERT INTO users " +
                    "(username, email, password, registration_date, expiration_date, active) " +
                    "VALUES (?, ?, ?, ?, ?, ?)" +
                    " ON CONFLICT DO NOTHING";
            try(PreparedStatement insertStatement = connection.getConnection().prepareStatement(insertUsersQuery)) {
                insertStatement.setString(1, "testUser");
                insertStatement.setString(2, "testEmail");
                insertStatement.setString(3, "testPassword");
                insertStatement.setObject(4, Timestamp.valueOf(LocalDateTime.now()));
                insertStatement.setObject(5, Timestamp.valueOf(LocalDateTime.now().plusDays(100)));
                insertStatement.setBoolean(6, true);
                insertStatement.addBatch();

                insertStatement.setString(1, "testUser2");
                insertStatement.setString(2, "testEmail2");
                insertStatement.setString(3, "testPassword2");
                insertStatement.setObject(4, Timestamp.valueOf(LocalDateTime.now()));
                insertStatement.setObject(5, Timestamp.valueOf(LocalDateTime.now().plusDays(200)));
                insertStatement.setBoolean(6, true);
                insertStatement.addBatch();

                insertStatement.setString(1, "testUser3");
                insertStatement.setString(2, "testEmail3");
                insertStatement.setString(3, "testPassword3");
                insertStatement.setObject(4, Timestamp.valueOf(LocalDateTime.now()));
                insertStatement.setObject(5, Timestamp.valueOf(LocalDateTime.now().plusDays(300)));
                insertStatement.setBoolean(6, true);
                insertStatement.addBatch();

                insertStatement.executeBatch();

            }


            tableName = "notes";
            createTableQuery = "CREATE TABLE IF NOT EXISTS " +
                    tableName +
                    "(" +
                    "id SERIAL PRIMARY KEY," +
                    "title VARCHAR(255), " +
                    "text TEXT," +
                    "created_by VARCHAR(255)," +
                    "creation_date DATE, " +
                    "update_date DATE," +
                    "FOREIGN KEY (created_by) REFERENCES users(username)" +
                    ")";

            try (PreparedStatement statement = connection.getConnection().prepareStatement(createTableQuery)) {
                statement.execute();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

