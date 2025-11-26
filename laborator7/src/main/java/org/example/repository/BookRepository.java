package org.example.repository;

import lombok.RequiredArgsConstructor;
import org.example.model.Book;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BookRepository {

    private static final BookRowMapper BOOK_ROW_MAPPER = new BookRowMapper();

    private final JdbcTemplate jdbc;
    private final NamedParameterJdbcTemplate namedJdbc;

    // 1) query(...) with custom RowMapper
    public List<Book> findAll() {
        return jdbc.query("SELECT * FROM books ORDER BY id", BOOK_ROW_MAPPER);
    }

    // 2) queryForObject(...) with BeanPropertyRowMapper (property <-> column naming)
    public Optional<Book> findById(Long id) {
        try {
            var mapper = new BeanPropertyRowMapper<>(Book.class);
            return Optional.ofNullable(jdbc.queryForObject(
                    "SELECT id, title, author, price_eur AS priceEur, created_at AS createdAt FROM books WHERE id = ?",
                    mapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    // 3) queryForList(...) for a single column
    public List<String> findDistinctTitles() {
        return jdbc.queryForList("SELECT DISTINCT title FROM books ORDER BY title", String.class);
    }

    // 4) queryForObject(...) for scalar
    public long count() {
        return jdbc.queryForObject("SELECT COUNT(*) FROM books", Long.class);
    }

    // 5) update(...) with KeyHolder for auto-generated id
    public Book create(String title, String author, BigDecimal priceEur) {
        KeyHolder kh = new GeneratedKeyHolder();
        jdbc.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO books (title, author, price_eur) VALUES (?, ?, ?)",
                    new String[]{"id"}
            );
            ps.setString(1, title);
            ps.setString(2, author);
            ps.setBigDecimal(3, priceEur);
            return ps;
        }, kh);
        Long newId = Objects.requireNonNull(kh.getKey()).longValue();
        return findById(newId).orElseThrow();
    }

    // 6) update(...) for changes
    public int updatePrice(Long id, BigDecimal newPrice) {
        return jdbc.update("UPDATE books SET price_eur = ? WHERE id = ?", newPrice, id);
    }

    // 7) delete
    public int deleteById(Long id) {
        return jdbc.update("DELETE FROM books WHERE id = ?", id);
    }

    // 8) batchUpdate(...) inserting many rows
    public void batchInsert(List<Book> books) {
        jdbc.batchUpdate(
                "INSERT INTO books (title, author, price_eur) VALUES (?, ?, ?)",
                books,
                100,
                (ps, b) -> {
                    ps.setString(1, b.getTitle());
                    ps.setString(2, b.getAuthor());
                    ps.setBigDecimal(3, b.getPriceEur());
                }
        );
    }

    // 9) NamedParameterJdbcTemplate with dynamic filters
    public List<Book> searchByAuthorContains(String authorFragment) {
        String sql = "SELECT * FROM books WHERE author ILIKE :q ORDER BY id";
        var params = new MapSqlParameterSource().addValue("q", "%" + authorFragment + "%");
        return namedJdbc.query(sql, params, BOOK_ROW_MAPPER);
    }

    // 10) query(...) using ResultSetExtractor (aggregate example)
    public Map<String, Long> countByAuthor() {
        String sql = "SELECT author, COUNT(*) AS c FROM books GROUP BY author";
        return jdbc.query(sql, rs -> {
            Map<String, Long> map = new LinkedHashMap<>();
            while (rs.next()) {
                map.put(rs.getString("author"), rs.getLong("c"));
            }
            return map;
        });
    }
}