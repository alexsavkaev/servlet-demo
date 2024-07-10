package ru.savkaev.servletdemo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Note {
    private Long id;
    private String title;
    private String text;
    private String createdBy;
    private LocalDateTime creationDate;
    private LocalDateTime updateDate;
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        return "Note{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", creationDate=" + creationDate.format(formatter) +
                '}';
    }
}
