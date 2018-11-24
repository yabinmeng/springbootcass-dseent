CREATE KEYSPACE IF NOT EXISTS springbootcass WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '1'}  AND durable_writes = true;

CREATE TABLE IF NOT EXISTS springbootcass.bookrating (
    book_id UUID,
    author_code text,
    rating_year int,
    rating_score float,
    rating_desc text,
    PRIMARY KEY ((book_id, author_code), rating_year)
);