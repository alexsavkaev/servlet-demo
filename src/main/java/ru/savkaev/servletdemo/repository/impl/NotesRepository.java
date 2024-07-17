package ru.savkaev.servletdemo.repository.impl;

import lombok.extern.slf4j.Slf4j;
import ru.savkaev.servletdemo.model.Note;
import ru.savkaev.servletdemo.repository.iNotesRepository;
import ru.savkaev.servletdemo.utility.DataBaseConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
public class NotesRepository implements iNotesRepository {

    /**
     * Сохраняет заметку
     * @param note заметка для сохранения
     * @return сохраненная заметка
     */
    @Override
    public Note create(Note note) {
        try (DataBaseConnection connection = new DataBaseConnection()) {
            connection.connect(
                    "jdbc:postgresql://localhost:5432/postgres",
                    "postgres",
                    "12345"
            );

            String query = "INSERT INTO notes (title, text, created_by, creation_date) VALUES (?, ?, ?, ?)";
            log.info("Query: {}", query);
            try (PreparedStatement statement =
                         connection.getConnection()
                                 .prepareStatement(query, Statement.RETURN_GENERATED_KEYS))
            {
                statement.setString(1, note.getTitle());
                statement.setString(2, note.getText());
                statement.setString(3, note.getCreatedBy());
                statement.setObject(4, note.getCreationDate());
                statement.executeUpdate();

                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        long id = generatedKeys.getLong(1);
                        note.setId(id);
                        log.info("Note created: {}", note);
                    }
                }
            }
        } catch (SQLException e ) {
            log.error("Failed to create note", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return findById(note.getId());
    }

    /**
     * Изменить заметку.
     * @param note Обновленая заметка
     * @param noteId id заметки
     * @return Обновленная заметка. Если что-то пошло не так, возвращается null
     */

    @Override
    public Note update(Note note, Long noteId) {
        try (DataBaseConnection connection = new DataBaseConnection()) {
            connection.connect(
                    "jdbc:postgresql://localhost:5432/postgres",
                    "postgres",
                    "12345"
            );
            String query = "UPDATE notes SET title = ?, text = ?, created_by = ?," +
                    " creation_date = ?, update_date = ? WHERE id = ? ";
            try (PreparedStatement statement = connection.getConnection().prepareStatement(query)) {
                statement.setString(1, note.getTitle());
                statement.setString(2, note.getText());
                statement.setString(3, note.getCreatedBy());
                statement.setObject(4, note.getCreationDate());
                statement.setObject(5, note.getUpdateDate());
                statement.setLong(6, noteId);
                statement.executeUpdate();
                log.info("Note updated: {}", note);
                return findById(noteId);
            }
        } catch (Exception e) {
            log.error("Failed to update note", e);
        }
        return null;
    }

    /**
     * Удалить заметку
     *
     * @param id id заметки
     */

    @Override
    public void delete(Long id) {

        try (DataBaseConnection connection = new DataBaseConnection()) {
            connection.connect(
                    "jdbc:postgresql://localhost:5432/postgres",
                    "postgres",
                    "12345"
            );
            String query = "DELETE FROM notes WHERE id = ?";
            try (PreparedStatement statement = connection.getConnection().prepareStatement(query)) {
                statement.setLong(1, id);
                statement.executeUpdate();
                log.info("Note deleted: id {}", id);
            }
        } catch (Exception e) {
            log.error("Failed to delete note", e);
        }

    }

    /**
     * Поиск заметки по id
     * @param id id
     * @return Note заметка. Если заметка не найдена - null
     */

    @Override
    public Note findById(Long id) {
        try (DataBaseConnection connection = new DataBaseConnection()) {
            connection.connect(
                    "jdbc:postgresql://localhost:5432/postgres",
                    "postgres",
                    "12345"
            );

            String query = "SELECT * FROM notes WHERE id = ?";

            try (PreparedStatement statement = connection.getConnection().prepareStatement(query)) {
                statement.setLong(1, id);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    return extractNoteFromResultSet(resultSet);
                }
            }
        } catch (Exception e) {
            log.error("Failed to find note", e);
        }

        return null;
    }

    /**
     * Поиск всех заметок
     * @return List<Note> список всех заметок. Если заметок нет - пустой список
     */

    @Override
    public List<Note> findAll() {try (DataBaseConnection connection = new DataBaseConnection()) {
        connection.connect(
                "jdbc:postgresql://localhost:5432/postgres",
                "postgres",
                "12345"
        );
        String query = "SELECT * FROM notes";
        try (PreparedStatement statement = connection.getConnection().prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            List<Note> notes = new ArrayList<>();
            while (resultSet.next()) {
                notes.add(extractNoteFromResultSet(resultSet));
            }
            return notes;
        }
    } catch (Exception e) {
        log.error("Failed to find all notes", e);
        return Collections.emptyList();
    }
    }

    /**
     * Поиск заметок по автору.
     * @param user имя автора
     * @return List<Note> список заметок искомого автора, если не найдено - пустой список
     */

    public List<Note> findByCreatedBy(String user) {
        try (DataBaseConnection connection = new DataBaseConnection()) {
            connection.connect(
                    "jdbc:postgresql://localhost:5432/postgres",
                    "postgres",
                    "12345"
            );
            String query = "SELECT * FROM notes WHERE created_by = ?";
            try (PreparedStatement statement = connection.getConnection().prepareStatement(query)) {
                statement.setString(1, user);
                ResultSet resultSet = statement.executeQuery();
                List<Note> notes = new ArrayList<>();
                while (resultSet.next()) {
                    notes.add(extractNoteFromResultSet(resultSet));
                }
                return notes;
            }
        } catch (Exception e) {
            log.error("Failed to find all notes by author", e);
            return Collections.emptyList();
        }
    }

    /**
     * Извлекает Note из ResultSet
     * @param resultSet Ответ от БД
     * @return Note
     * @throws SQLException если в ResultSet нет данных по запрашиваемому полю
     */
    private Note extractNoteFromResultSet(ResultSet resultSet) throws SQLException {
        Note note = new Note();
        note.setId(resultSet.getLong("id"));
        note.setTitle(resultSet.getString("title"));
        note.setText(resultSet.getString("text"));
        note.setCreatedBy(resultSet.getString("created_by"));
        note.setCreationDate(resultSet.getTimestamp("creation_date").toLocalDateTime());
        note.setUpdateDate(Optional
                .ofNullable(resultSet.getTimestamp("update_date"))
                .map(Timestamp::toLocalDateTime)
                .orElse(null));
        return note;
    }

    }
