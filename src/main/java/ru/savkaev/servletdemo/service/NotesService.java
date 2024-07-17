package ru.savkaev.servletdemo.service;

import lombok.extern.slf4j.Slf4j;
import ru.savkaev.servletdemo.dto.NoteDTO;
import ru.savkaev.servletdemo.mapper.NoteMapper;
import ru.savkaev.servletdemo.model.Note;
import ru.savkaev.servletdemo.repository.iNotesRepository;
import ru.savkaev.servletdemo.repository.impl.NotesRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Сервис для работы с заметками. Конвертирует Note в NoteDTO и обратно с помощью NoteMapper
 */
@Slf4j
public class NotesService {
    private final iNotesRepository notesRepository;
    public NotesService(iNotesRepository notesRepository) {
        this.notesRepository = notesRepository;
    }

    /**
     * Поиск заметок по создателю
     * @param createdBy имя создателя
     * @return List<NoteDTO> список заметок, созданных данным пользователем
     */

    public List<NoteDTO> findByCreatedBy(String createdBy) {
        return NoteMapper.mapToDTO(notesRepository.findByCreatedBy(createdBy));
    }

    /**
     * Поиск всех заметок
     * @return List<NoteDTO> список всех заметок
     */

    public List<NoteDTO> findAll() {
        return NoteMapper.mapToDTO(notesRepository.findAll());
    }

    /**
     * Поиск заметки по id
     * @param l id
     * @return NoteDTO заметка
     */

    public NoteDTO findById(long l) {
        Note note = notesRepository.findById(l);
        return note != null ? NoteMapper.mapToDTO(note) : null;
    }

    /**
     * Удаление заметки
     * @param id id заметки
     */

    public void delete(long id) {
        notesRepository.delete(id);
    }

    /**
     * Изменить заметку
     * @param noteId id заметки для изменения
     * @param note заметка с новыми данными
     * @return NoteDTO измененная заметка
     */

    public NoteDTO update(Long noteId, NoteDTO note) {
        Note oldNote = notesRepository.findById(noteId);
        if(oldNote != null)
        {

            Note updatedNote = NoteMapper.mapToNote(note);
            updatedNote.setCreationDate(oldNote.getCreationDate());
            updatedNote.setUpdateDate(LocalDateTime.now());
            return NoteMapper.mapToDTO(notesRepository.update(updatedNote, noteId));

        }
        log.error("Note with id: {} not found", noteId);
        return null;
    }

    /**
     * Написать заметку
     * @param note заметка
     * @return NoteDTO созданная заметка
     */

    public NoteDTO create(NoteDTO note) {

        Note newNote = NoteMapper.mapToNote(note);
        newNote.setCreationDate(LocalDateTime.now());
        return NoteMapper.mapToDTO(notesRepository.create(newNote));
    }
}
