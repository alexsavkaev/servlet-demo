package ru.savkaev.servletdemo.service;

import lombok.extern.slf4j.Slf4j;
import ru.savkaev.servletdemo.dto.NoteDTO;
import ru.savkaev.servletdemo.mapper.NoteMapper;
import ru.savkaev.servletdemo.model.Note;
import ru.savkaev.servletdemo.repository.iNotesRepository;
import ru.savkaev.servletdemo.repository.impl.NotesRepository;

import java.time.LocalDateTime;
import java.util.List;
@Slf4j
public class NotesService {
    private final iNotesRepository notesRepository = new NotesRepository();

    public List<NoteDTO> findAll() {
        return NoteMapper.mapToDTO(notesRepository.findAll());
    }

    public NoteDTO findById(long l) {
        return NoteMapper.mapToDTO(notesRepository.findById(l));
    }

    public void delete(long id) {
        notesRepository.delete(id);
    }

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

    public NoteDTO create(NoteDTO note) {

        Note newNote = NoteMapper.mapToNote(note);
        newNote.setCreationDate(LocalDateTime.now());
        return NoteMapper.mapToDTO(notesRepository.create(newNote));
    }
}
