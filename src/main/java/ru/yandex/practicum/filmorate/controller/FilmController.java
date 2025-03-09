package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import java.time.LocalDate;
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

    @PostMapping
    public ResponseEntity<Film> addFilm(@Valid @RequestBody Film film) throws ValidationException {
        log.info("Добавление фильма: {}", film);
        validate(film);
        return ResponseEntity.ok(filmService.addFilm(film));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Film> updateFilm(@PathVariable long id, @Valid @RequestBody Film updatedFilm) throws ValidationException {
        log.info("Обновление фильма с ID: {}", id);
        updatedFilm.setId(id);
        validate(updatedFilm);
        Film film = filmService.updateFilm(updatedFilm);
        if (film == null) {
            log.error("Фильм с ID {} не найден для обновления.", id);
            throw new ValidationException("Фильм с ID " + id + " не найден.");
        }
        return ResponseEntity.ok(film);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Film> getFilmById(@PathVariable long id) throws ValidationException {
        log.info("Получение фильма с ID: {}", id);
        Film film = filmService.getFilmById(id);
        if (film == null) {
            log.error("Фильм с ID {} не найден.", id);
            throw new ValidationException("Фильм с ID " + id + " не найден.");
        }
        return ResponseEntity.ok(film);
    }

    @GetMapping
    public ResponseEntity<Collection<Film>> getAllFilms() {
        log.info("Получение всех фильмов.");
        return ResponseEntity.ok(filmService.getAllFilms());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFilm(@PathVariable long id) {
        log.info("Удаление фильма с ID: {}", id);
        filmService.deleteFilm(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/like/{userId}")
    public ResponseEntity<Void> addLike(@PathVariable long id, @PathVariable long userId) throws ValidationException {
        Film film = filmService.getFilmById(id);
        if (film != null) {
            film.addLike(userId);
            log.info("Пользователь с ID {} поставил лайк фильму с ID {}", userId, id);
            return ResponseEntity.ok().build();
        } else {
            log.error("Не удалось поставить лайк: фильм с ID {} не найден.", id);
            throw new ValidationException("Фильм с ID " + id + " не найден.");
        }
    }

    @DeleteMapping("/{id}/like/{userId}")
    public ResponseEntity<Void> removeLike(@PathVariable long id, @PathVariable long userId) throws ValidationException {
        Film film = filmService.getFilmById(id);
        if (film != null) {
            film.removeLike(userId);
            log.info("Пользователь с ID {} убрал лайк у фильма с ID {}", userId, id);
            return ResponseEntity.ok().build();
        } else {
            log.error("Не удалось убрать лайк: фильм с ID {} не найден.", id);
            throw new ValidationException("Фильм с ID " + id + " не найден.");
        }
    }

    private void validate(Film film) throws ValidationException {
        log.debug("Валидация фильма: {}", film);

        if (film.getName() == null || film.getName().isEmpty()) {
            log.error("Ошибка валидации: Название фильма не может быть пустым.");
            throw new ValidationException("Название фильма не может быть пустым.");
        }

        if (film.getDescription() != null && film.getDescription().length() > 200) {
            log.error("Ошибка валидации: Максимальная длина описания — 200 символов.");
            throw new ValidationException("Максимальная длина описания — 200 символов.");
        }

        if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Ошибка валидации: Дата релиза не может быть раньше 28 декабря 1895 года.");
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года.");
        }

        if (film.getDuration() <= 0) {
            log.error("Ошибка валидации: Продолжительность фильма должна быть положительным числом.");
            throw new ValidationException("Продолжительность фильма должна быть положительным числом.");
        }

        log.info("Фильм {} успешно прошел валидацию.", film);
    }
}