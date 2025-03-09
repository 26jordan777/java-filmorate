package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Film addFilm(Film film); // Добавление фильма

    Film updateFilm(Film film); // Обновление фильма

    Film getFilmById(long id); // Получение фильма по ID

    Collection<Film> getAllFilms(); // Получение всех фильмов

    void deleteFilm(long id); // Удаление фильма по ID
}