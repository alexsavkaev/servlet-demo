package ru.savkaev.servletdemo.servlet;

import java.io.*;
import java.sql.SQLException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import lombok.extern.slf4j.Slf4j;
import ru.savkaev.servletdemo.dto.NoteDTO;
import ru.savkaev.servletdemo.repository.iNotesRepository;
import ru.savkaev.servletdemo.repository.impl.NotesRepository;
import ru.savkaev.servletdemo.service.NotesService;
import ru.savkaev.servletdemo.utility.DataBaseInit;

/**
 * Сервлет для работы с заметками
 */
@Slf4j
@WebServlet(name = "helloServlet", value = "/notes")
public class NotesServlet extends HttpServlet {
    private final NotesService notesService = new NotesService(new NotesRepository() {
    });

    /**
     * Запускает сервлет
     */
    public void init() {
        DataBaseInit dataBaseInit = new DataBaseInit(
                "jdbc:postgresql://localhost:5432/postgres",
                "postgres",
                "12345"
        );
        try {
            dataBaseInit.initializeDatabase();
        } catch (SQLException e) {
            log.error("Failed to initialize database", e);
        }

        log.info("Servlet initialized");
    }

    /**
     * Обрабатывает GET запросы. Выводит все заметки, заметку по id или заметки по создателю
     * @param request an {@link HttpServletRequest} объект, содержащий параметры запроса к сервлету
     *
     * @param response an {@link HttpServletResponse} объект, содержащий параметры ответа от сервлета
     *
     * @throws IOException если произошла ошибка ввода-вывода
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        String createdBy = request.getParameter("createdBy");
        ObjectMapper objectMapper = new ObjectMapper();
        if (id != null) {
            log.info("Get note with id: {}", request.getParameter("id"));
            NoteDTO note = notesService.findById(Long.parseLong(id));
            writeResponse(note, response, objectMapper);

        } else if(createdBy != null) {
            log.info("Get notes created by: {}", createdBy);
            List<NoteDTO> notes = notesService.findByCreatedBy(createdBy);
            writeResponse(notes, response, objectMapper);
        } else {
            log.info("Get all notes");
            List<NoteDTO> notes = notesService.findAll();
            writeResponse(notes, response, objectMapper);
        }
    }

    /**
     * Обрабатывает PUT запросы. Обновляет заметку по id в параметрах запроса, если такая заметка существует.
     * Извлекает обновленные данные из тела запроса.
     * @param request an {@link HttpServletRequest} объект, содержащий параметры запроса к сервлету
     *
     * @param response an {@link HttpServletResponse} объект, содержащий параметры ответа от сервлета
     *
     * @throws IOException если произошла ошибка ввода-вывода
     */
    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        NoteDTO note = objectMapper.readValue(request.getReader(), NoteDTO.class);

        Long noteId = Long.parseLong(request.getParameter("id"));

        log.info("Update note with id: {}", noteId);

        NoteDTO updatedNote = notesService.update(noteId, note);
        writeResponse(updatedNote, response, objectMapper);
    }

    /**
     * Обрабатывает POST запросы. Создает новую заметку. Извлекает данные из тела запроса.
     * @param request an {@link HttpServletRequest} объект, содержащий параметры запроса к сервлету
     *
     * @param response an {@link HttpServletResponse} объект, содержащий параметры ответа от сервлета
     *
     * @throws IOException  если произошла ошибка ввода-вывода
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("Creating note.");
        ObjectMapper objectMapper = new ObjectMapper();
        NoteDTO note = objectMapper.readValue(request.getReader(), NoteDTO.class);
        log.info("Created DTO: {}", note);
        try {
            NoteDTO createdNote = notesService.create(note);
            writeResponse(createdNote, response, objectMapper);
        } catch (Exception e) {
            log.error("Failed to create note", e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            writeResponse("Failed to create note", response, objectMapper);
        }
    }

    /**
     * Обрабатывает DELETE запросы. Удаляет заметку по id.
     * @param request an {@link HttpServletRequest} объект, содержащий параметры запроса к сервлету
     *
     * @param response an {@link HttpServletResponse} объект, содержащий параметры ответа от сервлета
     *
     * @throws IOException если произошла ошибка ввода-вывода
     */
    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        long id = Long.parseLong(request.getParameter("id"));
        notesService.delete(id);
    }

    /**
     * Вызывается при завершении работы сервлета
     *
     */
    @Override
    public void destroy() {
        log.info("Servlet destroyed");
    }

    /**
     * Записывает объект в поток вывода сервлета
     * @param object объект для записи
     * @param response ответ сервлета
     * @param objectMapper объект для сериализации
     * @throws IOException если произошла ошибка ввода-вывода
     */
    public void writeResponse(Object object, HttpServletResponse response, ObjectMapper objectMapper) throws IOException
    {
        String json = objectMapper.writeValueAsString(object);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try (PrintWriter writer = response.getWriter()) {
            writer.write(json);
        }
    }
}