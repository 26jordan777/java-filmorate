package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Mpa {
    private long id;
    private String name;

    public Mpa(String name) {
        this.name = name;
    }

    public Mpa() {

    }
}