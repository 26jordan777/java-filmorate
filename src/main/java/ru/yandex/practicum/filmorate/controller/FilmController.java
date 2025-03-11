package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Film> create(@Valid @RequestBody Film film) throws ValidationException {
        log.info("Получен запрос на создание фильма: {}", film);
        Film createdFilm = filmService.addFilm(film);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdFilm);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Film> read(@PathVariable long id) throws ValidationException {
        log.info("Получен запрос на получение фильма с идентификатором: {}", id);
        Film film = filmService.getFilmById(id);
        return ResponseEntity.ok(film);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Film> update(@PathVariable long id, @Valid @RequestBody Film updatedFilm) throws ValidationException {
        log.info("Обновление фильма с ID: {}", id);
        updatedFilm.setId(id);
        Film film = filmService.updateFilm(updatedFilm);
        return ResponseEntity.ok(film);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        log.info("Удаление фильма с ID: {}", id);
        filmService.deleteFilm(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Collection<Film>> findAll() {
        log.info("Получение списка всех фильмов");
        Collection<Film> films = filmService.getAllFilms();
        return ResponseEntity.ok(films);
    }

    @PutMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void addLike(@PathVariable long id, @PathVariable long userId) {
        log.info("Пользователь с ID {} поставил лайк фильму с ID {}", userId, id);
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void removeLike(@PathVariable long id, @PathVariable long userId) {
        log.info("Пользователь с ID {} убрал лайк у фильма с ID {}", userId, id);
        filmService.removeLike(id, userId);
    }
}