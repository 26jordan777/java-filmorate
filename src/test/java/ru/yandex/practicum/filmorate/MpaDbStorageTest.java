package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@Import(MpaDbStorage.class)
public class MpaDbStorageTest {

    @BeforeEach
    public void setUp() {
        mpaDbStorage.addMpa(new Mpa("G"));
        mpaDbStorage.addMpa(new Mpa("PG"));
    }

    @Autowired
    private MpaDbStorage mpaDbStorage;

    @Test
    public void testGetAllMpa() {
        List<Mpa> mpas = mpaDbStorage.getAllMpa();
        assertThat(mpas).isNotEmpty();
    }

    @Test
    public void testGetMpaById() {
        Mpa mpa = mpaDbStorage.getMpaById(1);
        assertThat(mpa).isNotNull();
        assertThat(mpa.getId()).isEqualTo(1);
    }
}