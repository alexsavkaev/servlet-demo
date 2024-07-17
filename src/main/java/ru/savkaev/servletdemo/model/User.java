package ru.savkaev.servletdemo.model;

import lombok.Data;

import java.time.LocalDateTime;
@Data

public class User {
    private Long id;
    private String username;
    private String email;
    private String password;
    private LocalDateTime registrationDate;
    private LocalDateTime expirationDate;
    private boolean active;


}
