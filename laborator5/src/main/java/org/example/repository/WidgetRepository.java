package org.example.repository;

import lombok.RequiredArgsConstructor;
import org.example.model.Widget;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class WidgetRepository {

    private final JdbcTemplate jdbc;

    private static final RowMapper<Widget> MAPPER = (rs, rowNum) ->
            Widget.builder()
                    .id(rs.getLong("id"))
                    .name(rs.getString("name"))
                    .description(rs.getString("description"))
                    .build();

    public Optional<Widget> findById(Long id) {
        return jdbc.query("SELECT id, name, description FROM widgets WHERE id = ?", MAPPER, id).stream().findFirst();
    }

    public List<Widget> findAll(Integer page, Integer size) {
        int offset = page * size;
        return jdbc.query("SELECT id, name, description FROM widgets ORDER BY id LIMIT ? OFFSET ?", MAPPER, size, offset);
    }

    public void save(Widget widget) {
        String sql = "INSERT INTO widgets(name, description) VALUES(?, ?)";
        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, widget.getName());
            ps.setString(2, widget.getDescription());
            return ps;
        });
    }

    public boolean update(Long id, String name, String description) {
        int rows = jdbc.update("UPDATE widgets SET name = ?, description = ? WHERE id = ?", name, description, id);
        return rows > 0;
    }

    public boolean delete(Long id) {
        int rows = jdbc.update("DELETE FROM widgets WHERE id = ?", id);
        return rows == 1;
    }
}