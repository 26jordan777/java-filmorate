package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private List<Film> films = new ArrayList<>();

    @PostMapping
    public ResponseEntity<Film> addFilm(@RequestBody Film film) {
        log.info("Добавление фильма: {}", film);
        try {
            film.validate();
            films.add(film);
            return ResponseEntity.ok(film);
        } catch (ValidationException e) {
            log.error("Ошибка валидации при добавлении фильма: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Film> updateFilm(@PathVariable int id, @RequestBody Film updatedFilm) {
        log.info("Обновление фильма с id: {}", id);
        try {
            updatedFilm.validate();
            for (int i = 0; i < films.size(); i++) {
                if (films.get(i).getId() == id) {
                    films.set(i, updatedFilm);
                    return ResponseEntity.ok(updatedFilm);
                }
            }
            log.warn("Фильм с id: {} не найден.", id);
            return ResponseEntity.notFound().build();
        } catch (ValidationException e) {
            log.error("Ошибка валидации при обновлении фильма: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<Film>> getAllFilms() {
        log.info("Получение всех фильмов.");
        return ResponseEntity.ok(films);
    }
}