INSERT INTO categories(id, name)
    VALUES
    (1, 'Computers'),
    (2, 'Electronics');

INSERT INTO categories(id, name, parent_category_id)
    VALUES
    (3, 'Monitors', 1),
    (4, 'Printers', 1),
    (5, 'Camera & Photo', 2),
    (6, 'Cell phones & Accessories', 2);

SELECT setval('categories_id_seq', 7);

INSERT INTO products(code, name, description, price, amount)
    VALUES
    ('dell-27', 'Dell 27', 'Dell monitor 27 Inch', 150000, 12),
    ('dell-21', 'Dell 21', 'Dell monitor 21 Inch', 135000, 18),
    ('hp', 'HP', 'HP printer', 35000, 10),
    ('brother', 'Brother', 'Brother printer', 36500, 8),
    ('nikon', 'Nikon', 'Nikon camera', 190000, 5),
    ('canon', 'Canon', 'Canon camera', 249999, 9),
    ('samsung', 'Samsung', 'Samsung cellphone', 99999, 10),
    ('apple', 'Apple', 'Apple headphones', 15000, 17);

INSERT INTO categories_products(category_fk, product_fk)
    VALUES
    (3, 'dell-27'),
    (3, 'dell-21'),
    (4, 'hp'),
    (4, 'brother'),
    (5, 'nikon'),
    (5, 'canon'),
    (6, 'samsung'),
    (6, 'apple');