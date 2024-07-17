package ru.savkaev.servletdemo.repository;

import ru.savkaev.servletdemo.model.User;

import java.util.List;

public interface iUserRepository {

    User create(User user);

    User update(User user, Long userId);

    void delete(Long id);

    User findById(Long id);

    List<User> findAll();
}
