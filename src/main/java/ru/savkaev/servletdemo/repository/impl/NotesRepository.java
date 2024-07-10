package ru.savkaev.servletdemo.repository.impl;

import lombok.extern.slf4j.Slf4j;
import ru.savkaev.servletdemo.model.Note;
import ru.savkaev.servletdemo.repository.iNotesRepository;
import ru.savkaev.servletdemo.utility.DataBaseConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Slf4j
public class NotesRepository implements iNotesRepository {

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
        return note;
    }

    @Override
    public Note update(Note note, Long noteId) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public Note findById(Long id) {
        return null;
    }

    @Override
    public List<Note> findAll() {
        return List.of();
    }
}
