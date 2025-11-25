INSERT INTO books (title, author, price_eur)
VALUES ('Effective Java', 'Joshua Bloch', 45.00),
       ('Clean Code', 'Robert C. Martin', 39.99),
       ('Spring in Action', 'Craig Walls', 42.50)
ON CONFLICT DO NOTHING;