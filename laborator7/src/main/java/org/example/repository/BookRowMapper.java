package org.example.repository;

import org.example.model.Book;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;

public class BookRowMapper implements RowMapper<Book> {
    @Override
    public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Book.builder()
                .id(rs.getLong("id"))
                .title(rs.getString("title"))
                .author(rs.getString("author"))
                .priceEur(rs.getBigDecimal("price_eur"))
                .createdAt(rs.getObject("created_at", OffsetDateTime.class))
                .build();
    }
}