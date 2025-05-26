package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Mpa> getAllMpa() {
        String sql = "SELECT * FROM mpa";
        return jdbcTemplate.query(sql, this::mapRowToMpa);
    }

    @Override
    public Mpa getMpaById(long id) {
        String sql = "SELECT * FROM mpa WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, this::mapRowToMpa, id);
    }

    private Mpa mapRowToMpa(ResultSet rs, int rowNum) throws SQLException {
        Mpa mpa = new Mpa();
        mpa.setId(rs.getLong("id"));
        mpa.setName(rs.getString("name"));
        return mpa;
    }

    public void addMpa(Mpa mpa) {
        String sql = "INSERT INTO MPA (name) VALUES (?)";
        jdbcTemplate.update(sql, mpa.getName());

        String idSql = "SELECT LAST_INSERT_ID()";
        Long id = jdbcTemplate.queryForObject(idSql, Long.class);

        mpa.setId(id);
    }
}