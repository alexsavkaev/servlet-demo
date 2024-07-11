package ru.savkaev.servletdemo.dto;

import lombok.Data;

@Data
public class NoteDTO {
    private Long id;
    private String title;
    private String text;
    private String createdBy;
    @Override
    public String toString() {
        return "NoteDTO{" +
                "title='" + title + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
