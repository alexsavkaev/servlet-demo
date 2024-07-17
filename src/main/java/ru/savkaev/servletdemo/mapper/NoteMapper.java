package ru.savkaev.servletdemo.mapper;

import ru.savkaev.servletdemo.dto.NoteDTO;
import ru.savkaev.servletdemo.model.Note;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Конвертирует Note в NoteDTO и обратно
 */
public class NoteMapper {
    /**
     * Конвертирует Note в NoteDTO
     * @param note Note obj
     * @return NoteDTO obj
     */
    public static NoteDTO mapToDTO(Note note){
        NoteDTO noteDTO = new NoteDTO();
        noteDTO.setId(note.getId());
        noteDTO.setTitle(note.getTitle());
        noteDTO.setText(note.getText());
        noteDTO.setCreatedBy(note.getCreatedBy());
        return noteDTO;
    }

    /**
     * Конвертирует NoteDTO в Note
     * @param noteDTO NoteDTO
     * @return Note
     */

    public static Note mapToNote(NoteDTO noteDTO) {
        Note note = new Note();
        note.setId(noteDTO.getId());
        note.setTitle(noteDTO.getTitle());
        note.setText(noteDTO.getText());
        note.setCreatedBy(noteDTO.getCreatedBy());
        note.setUpdateDate(LocalDateTime.now());
        return note;
    }

    /**
     * Конвертирует List<Note> в List<NoteDTO>
     * @param notes List<Note>
     * @return List<NoteDTO>
     */

    public static List<NoteDTO> mapToDTO(List<Note> notes) {
        return notes.stream()
                .map(NoteMapper::mapToDTO)
                .collect(Collectors.toList());
    }
}
