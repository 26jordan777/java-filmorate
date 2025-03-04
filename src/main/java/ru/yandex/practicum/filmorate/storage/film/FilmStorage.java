package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Film addFilm(Film film);
    Film updateFilm(Film film);
    Film getFilmById(long id);
    Collection<Film> getAllFilms();
    void deleteFilm(long id);
}