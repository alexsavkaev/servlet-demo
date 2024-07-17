package ru.savkaev.servletdemo.repository;

import ru.savkaev.servletdemo.model.Note;

import java.util.List;

public interface iNotesRepository {
    Note create(Note note);

    Note update(Note note, Long noteId);

    void delete(Long id);

    Note findById(Long id);

    List<Note> findAll();

    List<Note> findByCreatedBy(String createdBy);
}
