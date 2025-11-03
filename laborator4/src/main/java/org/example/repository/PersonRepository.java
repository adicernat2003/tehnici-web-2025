package org.example.repository;

import lombok.RequiredArgsConstructor;
import org.example.model.Person;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PersonRepository {
    private final JdbcTemplate jdbc;

    private static final String BASE_SELECT = "SELECT id, name, email, age FROM people";

    public List<Person> findAll() {
        return jdbc.query(BASE_SELECT, (rs, i) -> new Person(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("email"),
                (Integer) rs.getObject("age")
        ));
    }

    public Optional<Person> findById(Long id) {
        var list = jdbc.query(BASE_SELECT + " WHERE id = ?", ps -> ps.setLong(1, id), (rs, i) -> new Person(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("email"),
                (Integer) rs.getObject("age")
        ));
        return list.stream().findFirst();
    }

    public boolean existsById(Long id) {
        Integer count = jdbc.queryForObject("SELECT COUNT(*) FROM people WHERE id = ?", Integer.class, id);
        return count != null && count > 0;
    }

    public Person create(Person p) {
        String sql = "INSERT INTO people(name, email, age) VALUES(?,?,?)";
        KeyHolder kh = new GeneratedKeyHolder();
        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, p.getName());
            ps.setString(2, p.getEmail());

            if (p.getAge() == null) {
                ps.setNull(3, java.sql.Types.INTEGER);
            } else {
                ps.setInt(3, p.getAge());
            }

            return ps;
        }, kh);
        Number key = Optional.ofNullable(kh.getKey()).orElseThrow(() -> new DataIntegrityViolationException("No key generated"));
        p.setId(key.longValue());
        return p;
    }

    public void update(Person p) {
        String sql = "UPDATE people SET name=?, email=?, age=? WHERE id=?";
        jdbc.update(sql, p.getName(), p.getEmail(), p.getAge(), p.getId());
    }

    public void partialUpdate(Long id, Map<String, Object> fields) {
        List<String> sets = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        if (fields.containsKey("name")) {
            sets.add("name=?");
            params.add(fields.get("name"));
        }
        if (fields.containsKey("email")) {
            sets.add("email=?");
            params.add(fields.get("email"));
        }
        if (fields.containsKey("age")) {
            sets.add("age=?");
            params.add(fields.get("age"));
        }

        String sql = "UPDATE people SET " + String.join(", ", sets) + " WHERE id=?";
        params.add(id);
        jdbc.update(sql, params.toArray());
    }

    public void deleteById(Long id) {
        jdbc.update("DELETE FROM people WHERE id=?", id);
    }
}