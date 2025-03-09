package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private long counter = 0L;

    @Override
    public Film addFilm(Film film) {
        film.setId(++counter); //Добавление в счетчик
        films.put(film.getId(), film); // Сохранение фильма в коллекцию
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            return film;
        }
        return null; //Выброс исключение если фильм не найден
    }

    @Override
    public Film getFilmById(long id) {
        return films.get(id); //Получить фильм по ID
    }

    @Override
    public Collection<Film> getAllFilms() {
        return films.values(); //Вернуть все фильмы
    }

    @Override
    public void deleteFilm(long id) {
        films.remove(id); //Удаление фильма по ID
    }
}