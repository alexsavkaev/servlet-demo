package ru.savkaev.servletdemo.repository.impl;

import lombok.extern.slf4j.Slf4j;
import ru.savkaev.servletdemo.model.User;
import ru.savkaev.servletdemo.repository.iUserRepository;
import ru.savkaev.servletdemo.utility.DataBaseConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collections;
import java.util.List;

/**
 * Репозиторий для работы с пользователями
 */
@Slf4j
public class UserRepository implements iUserRepository {
    /**
     * Сохранить пользователя
     * @param user пользователь
     * @return сохраненный пользователь
     */
    @Override
    public User create(User user) {
        try(DataBaseConnection connection = new DataBaseConnection()) {
            connection.connect(
                    "jdbc:postgresql://localhost:5432/postgres",
                    "postgres",
                    "12345"
            );
            String query = "INSERT INTO users (username, email, password, registration_date, expiration_date, active) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.getConnection().prepareStatement(query)) {
                statement.setString(1, user.getUsername());
                statement.setString(2, user.getEmail());
                statement.setString(3, user.getPassword());
                statement.setObject(4, user.getRegistrationDate());
                statement.setObject(5, user.getExpirationDate());
                statement.setBoolean(6, user.isActive());
                statement.executeUpdate();
                log.info("User created: {}", user);
            }
        } catch (Exception e) {
            log.error("Failed to create user", e);
        }
        return findById(user.getId());
    }

    /**
     * Изменить пользователя.
     * @param user Пользователь с новыми данными
     * @param userId id пользователя для изменения
     * @return измененный пользователь. Если что-то пошло не так, вернет null
     */

    @Override
    public User update(User user, Long userId) {
        try(DataBaseConnection connection = new DataBaseConnection()) {
            connection.connect(
                    "jdbc:postgresql://localhost:5432/postgres",
                    "postgres",
                    "12345"
            );
            String query = "UPDATE users SET username = ?, email = ?, password = ?, registration_date = ?, expiration_date = ?, active = ? WHERE id = ?";
            try (PreparedStatement statement = connection.getConnection().prepareStatement(query)) {
                statement.setString(1, user.getUsername());
                statement.setString(2, user.getEmail());
                statement.setString(3, user.getPassword());
                statement.setObject(4, user.getRegistrationDate());
                statement.setObject(5, user.getExpirationDate());
                statement.setBoolean(6, user.isActive());
                statement.setLong(7, userId);
                statement.executeUpdate();
                log.info("User updated: {}", user);
            }
        } catch (Exception e) {
            log.error("Failed to update user", e);
        }
        return null;
    }

    /**
     * Удаляет пользователя
     * @param id id пользователя
     */

    @Override
    public void delete(Long id) {
        try(DataBaseConnection connection = new DataBaseConnection()) {
            connection.connect(
                    "jdbc:postgresql://localhost:5432/postgres",
                    "postgres",
                    "12345"
            );
            String query = "DELETE FROM users WHERE id = ?";
            try (PreparedStatement statement = connection.getConnection().prepareStatement(query)) {
                statement.setLong(1, id);
                statement.executeUpdate();
                log.info("User deleted: {}", id);
            }
        } catch (Exception e) {
            log.error("Failed to delete user", e);
        }
    }

    /**
     * Поиск пользователя по id
     * @param id id
     * @return User пользователя. Если пользователь не найден - null
     */

    @Override
    public User findById(Long id) {
        try (DataBaseConnection connection = new DataBaseConnection()) {
            connection.connect(
                    "jdbc:postgresql://localhost:5432/postgres",
                    "postgres",
                    "12345"
            );
            String query = "SELECT * FROM users WHERE id = ?";
            try (PreparedStatement statement = connection.getConnection().prepareStatement(query)) {
                statement.setLong(1, id);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    return extractUserFromResultSet(resultSet);
                }
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Показать всех пользователей
     *
     * @return List<User> список всех пользователей
     */
        @Override
    public List<User> findAll() {
        try(DataBaseConnection connection = new DataBaseConnection()) {
            connection.connect(
                    "jdbc:postgresql://localhost:5432/postgres",
                    "postgres",
                    "12345"
            );
            String query = "SELECT * FROM users";
            try (PreparedStatement statement = connection.getConnection().prepareStatement(query)) {
                ResultSet resultSet = statement.executeQuery();
                List<User> users = new java.util.ArrayList<>();
                while (resultSet.next()) {
                    users.add(extractUserFromResultSet(resultSet));
                }
                return users;
            }
        } catch (Exception e) {
            log.error("Failed to find all users", e);
            return Collections.emptyList();
        }
        }

    /**
     * Извлечение пользователя из ResultSet для отображения
     * @param resultSet ResultSet с пользователями
     * @return User
     * @throws Exception если что-то пошло не так
     */
    private User extractUserFromResultSet(ResultSet resultSet) throws Exception {
        User user = new User();
        user.setId(resultSet.getLong("id"));
        user.setUsername(resultSet.getString("username"));
        user.setEmail(resultSet.getString("email"));
        user.setPassword(resultSet.getString("password"));
        user.setRegistrationDate(resultSet.getTimestamp("registration_date").toLocalDateTime());
        user.setExpirationDate(resultSet.getTimestamp("expiration_date").toLocalDateTime());
        user.setActive(resultSet.getBoolean("active"));
        return user;
    }
}
