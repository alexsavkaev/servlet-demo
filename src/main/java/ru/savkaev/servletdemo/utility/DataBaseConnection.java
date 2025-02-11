package ru.savkaev.servletdemo.utility;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Slf4j
@Getter
/**
 * Подключение к базе данных. Импорт драйвера PostgreSQL
 */
public class DataBaseConnection implements AutoCloseable {

    private Connection connection;
    public void connect (String url, String user, String password) throws SQLException {
        try{
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);
            log.info("Database connected");
        } catch (SQLException | ClassNotFoundException e) {
            log.error("Failed to connect to database", e);
        }
    }

    /**
     * Закрывает подключение к базе для очистки ресурсов
     * @throws Exception если произошла ошибка
     */
    @Override
    public void close() throws Exception {
        if(connection != null) {
            connection.close();
            log.info("Database disconnected");
        }
    }
}
