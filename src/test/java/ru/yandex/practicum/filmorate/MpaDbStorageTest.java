package ru.yandex.practicum.filmorate;

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

    @Autowired
    private MpaDbStorage mpaDbStorage;

    @Test
    public void testAddMpa() {
        Mpa mpa = new Mpa("PG");
        Mpa addedMpa = mpaDbStorage.addMpa(mpa);
        assertThat(addedMpa).isNotNull();
        assertThat(addedMpa.getId()).isGreaterThan(0);
    }

    @Test
    public void testGetMpaById() {
        Mpa mpa = new Mpa("PG");
        Mpa addedMpa = mpaDbStorage.addMpa(mpa);

        Mpa foundMpa = mpaDbStorage.getMpaById(addedMpa.getId());
        assertThat(foundMpa).isNotNull();
        assertThat(foundMpa.getId()).isEqualTo(addedMpa.getId());
    }

    @Test
    public void testGetAllMpa() {

        Mpa mpa1 = new Mpa("G");
        Mpa mpa2 = new Mpa("PG");
        mpaDbStorage.addMpa(mpa1);
        mpaDbStorage.addMpa(mpa2);

        List<Mpa> mpaList = mpaDbStorage.getAllMpa();
        assertThat(mpaList).isNotEmpty();
    }
}