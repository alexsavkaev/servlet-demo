

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.savkaev.servletdemo.dto.NoteDTO;
import ru.savkaev.servletdemo.mapper.NoteMapper;
import ru.savkaev.servletdemo.model.Note;
import ru.savkaev.servletdemo.repository.iNotesRepository;
import ru.savkaev.servletdemo.service.NotesService;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions. * ;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@MockitoSettings(strictness = Strictness.LENIENT)
class NotesServiceTest {

    @Mock
    private iNotesRepository notesRepository;


    @InjectMocks
    private NotesService notesService;
    private NoteDTO noteDTO;
    private Note note;
//    private LocalDateTime creationDate;
    private Long noteId;

    @BeforeEach
    void setUp() {
        noteDTO = new NoteDTO();
        note = new Note();
//        creationDate = LocalDateTime.now();
        noteId = 1L;
    }

    @Test
    void testFindByCreatedBy() {
        // Given
        String createdBy = "testUser";
        List<Note> notes = new ArrayList<>();
        when(notesRepository.findByCreatedBy(createdBy)).thenReturn(notes);

        // When
        List<NoteDTO> result = notesService.findByCreatedBy(createdBy);

        // Then
        assertEquals(result, NoteMapper.mapToDTO(notes));
    }

    @Test
    void testFindAll() {
        // Given
        List<Note> notes = new ArrayList<>();
        when(notesRepository.findAll()).thenReturn(notes);

        // When
        List<NoteDTO> result = notesService.findAll();

        // Then
        assertEquals(result, NoteMapper.mapToDTO(notes));
    }

    @Test
    void testFindById() {
        // Given
        when(notesRepository.findById(noteId)).thenReturn(note);

        // When
        NoteDTO result = notesService.findById(noteId);

        // Then
        assertEquals(result, NoteMapper.mapToDTO(note));
    }
    @Test
    void testFindByIdFailed() {
        // Given
        when(notesRepository.findById(noteId)).thenReturn(null);

        // When
        NoteDTO result = notesService.findById(noteId);

        // Then
        assertNull(result);
    }
    @Test
    void testUpdateNoteNotFound() {
        // Given
        when(notesRepository.findById(noteId)).thenReturn(null);

        // When
        NoteDTO result = notesService.update(noteId, noteDTO);

        // Then
        assertNull(result);
    }

    @Test
    void testDelete() {
        // When
        notesService.delete(noteId);

        // Then
        verify(notesRepository).delete(noteId);
    }

    @Test
    void testUpdateNote() {
        // Given
        when(notesRepository.findById(noteId)).thenReturn(note);
        when(notesRepository.update(any(Note.class), eq(noteId))).thenReturn(new Note());

        // When
        NoteDTO updatedNoteDTO = notesService.update(noteId, noteDTO);

        // Then

        assertNotNull(updatedNoteDTO);
    }

    @Test
    void testCreate() {
        // Given
        when(notesRepository.create(any())).thenReturn(note);

        // When
        NoteDTO result = notesService.create(noteDTO);

        // Then
        assertEquals(result, NoteMapper.mapToDTO(note));
    }
}
