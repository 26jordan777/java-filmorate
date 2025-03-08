package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film addFilm(Film film) {
        Film addFilm = filmStorage.addFilm(film);
        log.info("Фильм добавлен: {}", addFilm);
        return addFilm;
    }

    public Film updateFilm(Film film) {
        Film updatedFilm = filmStorage.updateFilm(film);
        log.info("Фильм обновлен: {}", updatedFilm);
        return updatedFilm;
    }

    public Film getFilmById(long id) {
        Film film = filmStorage.getFilmById(id);
        if (film == null) {
            log.error("Фильм с ID {} не найден.", id);
        }
        return film;
    }

    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public void addLike(long filmId, long userId) {
        Film film = filmStorage.getFilmById(filmId);
        if (film != null) {
            film.addLike(userId);
            log.info("Пользователь с ID {} поставил лайк фильму с ID {}", userId, filmId);
        } else {
            log.error("Не удалось поставить лайк: фильм с ID {} не найден.", filmId);
        }
    }

    public void removeLike(long filmId, long userId) {
        Film film = filmStorage.getFilmById(filmId);
        if (film != null) {
            film.removeLike(userId);
            log.info("Пользователь с ID {} убрал лайк у фильма с ID {}", userId, filmId);
        } else {
            log.error("Не удалось убрать лайк: фильм с ID {} не найден.", filmId);
        }
    }

    public List<Film> getTopFilms(int count) {
        return filmStorage.getAllFilms().stream()
                .sorted(Comparator.comparingInt(f -> -f.getLikeCount()))
                .limit(count)
                .toList();
    }
}