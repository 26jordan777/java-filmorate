package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Collection;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private Map<Long, Film> films = new HashMap<>();
    private long counter = 0L;

    @PostMapping
    public ResponseEntity<Film> addFilm(@Valid @RequestBody Film film) {
        log.info("Добавление фильма: {}", film);
        film.setId(++counter);
        films.put(film.getId(), film);
        return ResponseEntity.ok(film);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Film> updateFilm(@PathVariable long id, @Valid @RequestBody Film updatedFilm) {
        log.info("Обновление фильма с id: {}", id);
        if (films.containsKey(id)) {
            updatedFilm.setId(id);
            films.put(id, updatedFilm);
            return ResponseEntity.ok(updatedFilm);
        } else {
            log.warn("Фильм с id: {} не найден.", id);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<Collection<Film>> getAllFilms() {
        log.info("Получение всех фильмов.");
        return ResponseEntity.ok(films.values());
    }
}