package ru.yandex.practicum.filmorate.controller;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Collection;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    private final Map<Long, Film> films = new HashMap<>();

    @PostMapping
    public ResponseEntity<Film> addFilm(@Valid @RequestBody Film film) throws ValidationException {
        log.info("Добавление фильма: {}", film);
        validate(film);
        film.setId(++counter);
        films.put(film.getId(), film);
        return ResponseEntity.ok(film);
    }

    @PutMapping()
    public ResponseEntity<Film> updateFilm(@Valid @RequestBody Film updatedFilm) throws ValidationException {
        log.info("Обновление фильма с id: {}", updatedFilm.getId());
        if (!films.containsKey(updatedFilm.getId())) {
            throw new ValidationException("Not found key: " + updatedFilm.getId());
        }
        validate(updatedFilm);
        films.put(updatedFilm.getId(), updatedFilm);
        return ResponseEntity.ok(updatedFilm);
    }

    @GetMapping()
    public ResponseEntity<Film> getFilmById(@PathVariable long id) throws ValidationException {
        log.info("Получение фильма с ID: {}", id);
        Film film = films.get(id);
        if (film == null) {
            log.error("Фильм с ID {} не найден.", id);
            throw new ValidationException("Фильм с ID " + id + " не найден.");
        }
        return ResponseEntity.ok(film);
    }

    @GetMapping
    public ResponseEntity<Collection<Film>> getAllFilms() {
        log.info("Получение всех фильмов.");
        return ResponseEntity.ok(films.values());
    }

    @DeleteMapping()
    public ResponseEntity<Void> deleteFilm(@PathVariable long id) {
        log.info("Удаление фильма с ID: {}", id);
        films.remove(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping()
    public ResponseEntity<Void> addLike(@PathVariable long id, @PathVariable long userId) throws ValidationException {
        Film film = films.get(id);
        if (film != null) {
            film.addLike(userId);
            log.info("Пользователь с ID {} поставил лайк фильму с ID {}", userId, id);
            return ResponseEntity.ok().build();
        } else {
            log.error("Не удалось поставить лайк: фильм с ID {} не найден.", id);
            throw new ValidationException("Фильм с ID " + id + " не найден.");
        }
    }

    @DeleteMapping()
    public ResponseEntity<Void> removeLike(@PathVariable long id, @PathVariable long userId) throws ValidationException {
        Film film = films.get(id);
        if (film != null) {
            film.removeLike(userId);
            log.info("Пользователь с ID {} убрал лайк у фильма с ID {}", userId, id);
            return ResponseEntity.ok().build();
        } else {
            log.error("Не удалось убрать лайк: фильм с ID {} не найден.", id);
            throw new ValidationException("Фильм с ID " + id + " не найден.");
        }
    }

    private long counter = 0L;

    private void validate(Film film) throws ValidationException {
        log.debug("Валидация фильма: {}", this);

        if (film.getName() == null || film.getName().isEmpty()) {
            log.error("Ошибка валидации: {}", "Название фильма не может быть пустым.");
            throw new ValidationException("Название фильма не может быть пустым.");
        }

        if (film.getDescription() != null && film.getDescription().length() > 200) {
            log.error("Ошибка валидации: {}", "Максимальная длина описания — 200 символов.");
            throw new ValidationException("Максимальная длина описания — 200 символов.");
        }

        if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Ошибка валидации: {}", "Дата релиза не может быть раньше 28 декабря 1895 года.");
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года.");
        }

        if (film.getDuration() <= 0) {
            log.error("Ошибка валидации: {}", "Продолжительность фильма должна быть положительным числом.");
            throw new ValidationException("Продолжительность фильма должна быть положительным числом.");
        }

        log.info("Фильм {} успешно прошел валидацию.", this);
    }

}