package ru.savkaev.servletdemo.service;

import lombok.extern.slf4j.Slf4j;
import ru.savkaev.servletdemo.dto.UserDTO;
import ru.savkaev.servletdemo.mapper.UserMapper;
import ru.savkaev.servletdemo.model.User;
import ru.savkaev.servletdemo.repository.iUserRepository;
import ru.savkaev.servletdemo.repository.impl.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Сервис для работы с пользователями. Конвертирует User в UserDTO и обратно с помощью UserMapper
 */
@Slf4j
public class UserService {
    private final iUserRepository userRepository;


    public UserService(iUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Поиск всех пользоватлей
     * @return List<UserDTO> список всех пользоватлей
     */
    public List<UserDTO> findAll() {
        return UserMapper.mapToDTO(userRepository.findAll());
    }

    /**
     * Поиск пользователя по id
     * @param id id пользователя
     * @return UserDTO пользователя
     */
    public UserDTO findById(Long id) {
        return UserMapper.mapToDTO(userRepository.findById(id));
    }

    /**
     * Удаление пользователя
     * @param id id пользователя
     */
    public void delete(Long id) {
        userRepository.delete(id);
    }

    /**
     * Изменить пользователя
     * @param userId id пользователя для изменения
     * @param user новые данные
     * @return UserDTO измененного пользователя
     */
    public UserDTO update(Long userId, UserDTO user) {
        User oldUser = userRepository.findById(userId);
        if(oldUser != null)
        {
            User updatedUser = UserMapper.mapToUser(user);
            updatedUser.setId(oldUser.getId());
            updatedUser.setRegistrationDate(oldUser.getRegistrationDate());
            updatedUser.setExpirationDate(oldUser.getExpirationDate());
            return UserMapper.mapToDTO(userRepository.update(updatedUser, userId));
        }
        log.error("User with id: {} not found", userId);
        return null;
    }

    /**
     * Сохранить пользователя
     * @param user данные
     * @return UserDTO сохраненного пользователя
     */
    public UserDTO create(UserDTO user) {
        User newUser = UserMapper.mapToUser(user);
        newUser.setRegistrationDate(LocalDateTime.now());
        return UserMapper.mapToDTO(userRepository.create(newUser));
    }

}
