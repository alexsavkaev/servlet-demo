package ru.savkaev.servletdemo.dto;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class UserDTO {

    private String name;
    private String email;
    private Long id;

}
