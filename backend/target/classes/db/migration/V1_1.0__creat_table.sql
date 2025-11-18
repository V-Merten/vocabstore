CREATE TABLE IF NOT EXISTS users (
    id serial PRIMARY KEY,
    username varchar(50) NOT NULL UNIQUE,
    password varchar(100) NOT NULL,
    email varchar(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS word_groups (
    id SERIAL PRIMARY KEY,
    group_name VARCHAR(100) NOT NULL,
    user_id INTEGER NOT NULL,
    CONSTRAINT fk_word_groups_user
        FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS words (
    id SERIAL PRIMARY KEY,
    foreign_word VARCHAR(100) NOT NULL,
    translated_word VARCHAR(100) NOT NULL,

    user_id INTEGER NOT NULL,
    group_id INTEGER,

    CONSTRAINT fk_words_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_words_group FOREIGN KEY (group_id) REFERENCES word_groups(id)
);