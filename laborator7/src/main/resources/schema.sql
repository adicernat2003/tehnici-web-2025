CREATE TABLE IF NOT EXISTS books
(
    id         BIGSERIAL PRIMARY KEY,
    title      TEXT           NOT NULL,
    author     TEXT           NOT NULL,
    price_eur  NUMERIC(10, 2) NOT NULL,
    created_at TIMESTAMPTZ    NOT NULL DEFAULT NOW()
);


CREATE INDEX IF NOT EXISTS idx_books_author ON books (author);