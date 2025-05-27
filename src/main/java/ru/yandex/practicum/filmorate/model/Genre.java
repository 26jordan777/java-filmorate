package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Genre {
    private long id;
    private String name;

    public Genre(String name) {
        this.name = name;
    }

    public Genre(){

    }

}