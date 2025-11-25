package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    private Long id;
    private String title;
    private String author;
    private BigDecimal priceEur;
    private OffsetDateTime createdAt;
}