package org.example.repository;

import lombok.RequiredArgsConstructor;
import org.example.exception.NotFoundException;
import org.example.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final JdbcTemplate jdbc;

    private static final RowMapper<User> MAPPER = (rs, i) -> {
        User u = new User();
        u.setId(rs.getLong("id"));
        u.setFirstName(rs.getString("first_name"));
        u.setLastName(rs.getString("last_name"));
        u.setEmail(rs.getString("email"));
        Date bd = rs.getDate("birth_date");
        u.setBirthDate(bd != null ? bd.toLocalDate() : null);
        return u;
    };

    public boolean existsByEmail(String email) {
        Boolean exists = jdbc.queryForObject(
                "SELECT EXISTS (SELECT 1 FROM users WHERE email = ?)",
                Boolean.class,
                email
        );
        return Boolean.TRUE.equals(exists);
    }

    public void insert(User u) {
        String sql = "INSERT INTO users(first_name,last_name,email,birth_date) VALUES (?,?,?,?)";
        jdbc.update(sql, u.getFirstName(), u.getLastName(), u.getEmail(), u.getBirthDate());
    }

    public void update(User u) {
        jdbc.update("""
                        UPDATE users SET first_name=?, last_name=?, email=?, birth_date=?
                        WHERE id=?""",
                u.getFirstName(), u.getLastName(), u.getEmail(), u.getBirthDate(), u.getId());
    }

    public User findById(long id) {
        List<User> list = jdbc.query("SELECT * FROM users WHERE id = ?", MAPPER, id);
        return list.stream().findFirst()
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    public List<User> findAll() {
        return jdbc.query("SELECT * FROM users ORDER BY id", MAPPER);
    }

    public void delete(long id) {
        List<User> list = jdbc.query("SELECT * FROM users WHERE id = ?", MAPPER, id);
        if (list.isEmpty()) {
            throw new NotFoundException("User not found");
        }
        jdbc.update("DELETE FROM users WHERE id = ?", id);
    }
}