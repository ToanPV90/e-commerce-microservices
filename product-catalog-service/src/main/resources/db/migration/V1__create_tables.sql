CREATE TABLE categories(
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    parent_category_id INT REFERENCES categories
);

CREATE TABLE products(
    code VARCHAR(255) NOT NULL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    price INT NOT NULL,
    amount INT NOT NULL
);

CREATE TABLE categories_products(
    category_fk INT NOT NULL REFERENCES categories,
    product_fk VARCHAR(255) NOT NULL REFERENCES products
);


