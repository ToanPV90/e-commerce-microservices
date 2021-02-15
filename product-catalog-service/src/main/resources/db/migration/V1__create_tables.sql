CREATE TABLE IF NOT EXISTS categories(
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    parent_category_id INT REFERENCES categories
);

CREATE TABLE IF NOT EXISTS products(
    code VARCHAR(255) NOT NULL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    price INT NOT NULL,
    amount INT NOT NULL,
    category_fk INT REFERENCES categories
);
