

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.savkaev.servletdemo.dto.NoteDTO;
import ru.savkaev.servletdemo.dto.UserDTO;
import ru.savkaev.servletdemo.mapper.NoteMapper;
import ru.savkaev.servletdemo.mapper.UserMapper;
import ru.savkaev.servletdemo.model.Note;
import ru.savkaev.servletdemo.model.User;
import ru.savkaev.servletdemo.repository.iNotesRepository;
import ru.savkaev.servletdemo.repository.iUserRepository;
import ru.savkaev.servletdemo.service.NotesService;
import ru.savkaev.servletdemo.service.UserService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions. * ;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@MockitoSettings(strictness = Strictness.LENIENT)

public class UserServiceTest {
    @Mock
    private iUserRepository userRepository;

    @InjectMocks
    private UserService userService;
    private UserDTO userDTO;
    private User user;
    private Long userId;


    @BeforeEach
    void setUp() {
        userDTO = new UserDTO();
        user = new User();
        userId = 1L;
    }

    @Test
    void testFindAll() {
        // Given
        List<User> users = new ArrayList<>();
        when(userRepository.findAll()).thenReturn(users);

        // When
        List<UserDTO> result = userService.findAll();

        // Then
        assertEquals(result, UserMapper.mapToDTO(users));
    }

    @Test
    void testFindById() {
        // Given
        when(userRepository.findById(userId)).thenReturn(user);

        // When
        UserDTO result = userService.findById(userId);

        // Then
        assertEquals(result, UserMapper.mapToDTO(user));
    }
    @Test
    void testDelete() {
        // Given
        when(userRepository.findById(userId)).thenReturn(user);

        // When
        userService.delete(userId);

        // Then
        verify(userRepository).delete(userId);
    }

    @Test
    void testUpdate() {
        // Given
        when(userRepository.findById(userId)).thenReturn(user);
        when(userRepository.update(any(User.class), eq(userId))).thenReturn(new User());


        // When
        UserDTO result = userService.update(userId, userDTO);

        // Then
        assertEquals(result, UserMapper.mapToDTO(user));
    }
    @Test
    void testUpdateFailed() {
        // Given
        when(userRepository.findById(userId)).thenReturn(null);

        // When
        UserDTO result = userService.update(userId, userDTO);

        // Then
        assertNull(result);
    }
    @Test
    void testCreate() {

        // Given
        when(userRepository.create(any(User.class))).thenReturn(user);
        // When
        UserDTO result = userService.create(userDTO);

        // Then
        assertEquals(result, UserMapper.mapToDTO(user));
    }


}
