package ru.savkaev.servletdemo.mapper;

import ru.savkaev.servletdemo.dto.UserDTO;
import ru.savkaev.servletdemo.model.User;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Конвертирует User в UserDTO и обратно
 */
public class UserMapper {

    /**
     * Конвертирует User в UserDTO
     * @param user User obj
     * @return UserDTO obj
     */
    public static UserDTO mapToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setName(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setId(user.getId());
        return userDTO;
    }

    /**
     * Конвертирует UserDTO в User
     * @param userDTO UserDTO
     * @return User
     */

    public static User mapToUser(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setId(userDTO.getId());
        return user;
    }

    /**
     * Конвертирует List<User> в List<UserDTO>
     * @param users List<User>
     * @return List<UserDTO>
     */
    public static List<UserDTO> mapToDTO(List<User> users) {
        return users.stream()
                .map(UserMapper::mapToDTO)
                .collect(Collectors.toList());
    }
}
