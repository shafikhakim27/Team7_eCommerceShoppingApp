CREATE DATABASE IF NOT EXISTS ecommerce_db;

Select * from users;

-- Drop tables if they exist to avoid conflicts
DROP TABLE IF EXISTS users;

DROP TABLE IF EXISTS products;

DROP TABLE IF EXISTS carts;

DROP TABLE IF EXISTS cart_items;

DROP TABLE IF EXISTS customers;

DROP TABLE IF EXISTS orders;

DROP TABLE IF EXISTS order_items;

DROP TABLE IF EXISTS reviews;

DROP TABLE IF EXISTS categories;

DROP TABLE IF EXISTS product_categories;

DROP TABLE IF EXISTS suppliers;

DROP TABLE IF EXISTS inventory;

DROP TABLE IF EXISTS payments;

DROP TABLE IF EXISTS shipments;

DROP TABLE IF EXISTS delivery_addresses;

-- Create table for users
CREATE TABLE users (
id SERIAL PRIMARY KEY,
username VARCHAR(50) NOT NULL UNIQUE,
email VARCHAR(100) NOT NULL UNIQUE,
password VARCHAR(50) NOT NULL default'',
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert 5 sample users, Singapore context
INSERT INTO users (username, email, password) VALUES
('alice', 'alice@example.com', 'alice123'),
('bob', 'bob@example.com', 'bob123'),
('charlie', 'charlie@example.com', 'charlie123'),
('david', 'david@example.com', 'david123'),
('eve', 'eve@example.com', 'eve123');

-- Create table for products
CREATE TABLE products (
id SERIAL PRIMARY KEY,
name VARCHAR(100) NOT NULL,
description TEXT,
price DECIMAL(10, 2) NOT NULL,
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert 5 sample products, round up to 10 cents
INSERT INTO products (name, description, price) VALUES
('Laptop', 'A high-performance laptop', 1000.00),
('Smartphone', 'A latest model smartphone', 700.00),
('Headphones', 'Noise-cancelling headphones', 200.00),
('Smartwatch', 'A smartwatch with various features', 300.00),
('Tablet', 'A lightweight tablet', 400.00);

-- Create table for carts
CREATE TABLE carts (
id SERIAL PRIMARY KEY,
user_id INT REFERENCES users(id),
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert 5 sample carts
INSERT INTO carts (user_id) VALUES
(1),
(2),
(3),
(4),
(5);

-- Create table for cart_items to hold products in carts
CREATE TABLE cart_items (
cart_id INT REFERENCES carts(id),
product_id INT REFERENCES products(id),
quantity INT NOT NULL,
PRIMARY KEY (cart_id, product_id)
);

-- Insert sample data into cart_items
INSERT INTO cart_items (cart_id, product_id, quantity) VALUES
(1, 1, 1),
(1, 2, 2),
(2, 3, 1),
(3, 4, 1),
(4, 5, 3);

-- Create table for customers
CREATE TABLE customers (
id SERIAL PRIMARY KEY,
user_id INT REFERENCES users(id),
first_name VARCHAR(50) NOT NULL,
last_name VARCHAR(50) NOT NULL,
phone VARCHAR(15),
address TEXT
);

-- Insert 5 sample customers, Singapore format
INSERT INTO customers (user_id, first_name, last_name, phone, address) VALUES
(1, 'Alice', 'Tan', '+65 9123 4567', '123 Orchard Road, Singapore'),
(2, 'Bob', 'Lim', '+65 9234 5678', '456 Clementi Ave, Singapore'),
(3, 'Charlie', 'Wong', '+65 9345 6789', '789 Bukit Timah Rd, Singapore'),
(4, 'David', 'Lee', '+65 9456 7890', '321 Serangoon Rd, Singapore'),
(5, 'Eve', 'Ng', '+65 9567 8901', '654 Jurong East St, Singapore');

-- Create table for orders
CREATE TABLE orders (
id SERIAL PRIMARY KEY,
user_id INT REFERENCES users(id),
product_id INT REFERENCES products(id),
quantity INT NOT NULL,
order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert 5 sample orders
INSERT INTO orders (user_id, product_id, quantity) VALUES
(1, 1, 1),
(2, 2, 2),
(3, 3, 1),
(4, 4, 1),
(5, 5, 3);

-- Create table for order_items to hold products in orders
CREATE TABLE order_items (
order_id INT REFERENCES orders(id),
product_id INT REFERENCES products(id),
quantity INT NOT NULL,
PRIMARY KEY (order_id, product_id)
);

-- Insert sample data into order_items
INSERT INTO order_items (order_id, product_id, quantity) VALUES
(1, 1, 1),
(2, 2, 2),
(3, 3, 1),
(4, 4, 1),
(5, 5, 3);

-- line break to separate primary from optional sections
-- Optional sections below

-- Create table for reviews
CREATE TABLE reviews (
id SERIAL PRIMARY KEY,
product_id INT REFERENCES products(id),
user_id INT REFERENCES users(id),
rating INT CHECK (rating >= 1 AND rating <= 5),
comment TEXT,
review_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert 5 sample reviews
INSERT INTO reviews (product_id, user_id, rating, comment) VALUES
(1, 1, 5, 'Excellent laptop!'),
(2, 2, 4, 'Great smartphone with good features.'),
(3, 3, 3, 'Average headphones.'),
(4, 4, 5, 'Loving my new smartwatch!'),
(5, 5, 4, 'Good tablet for the price.');

-- Create table for categories
CREATE TABLE categories (
id SERIAL PRIMARY KEY,
name VARCHAR(50) NOT NULL UNIQUE,
description TEXT
);

-- Insert 5 sample categories
INSERT INTO categories (name, description) VALUES
('Electronics', 'Devices and gadgets'),
('Books', 'Various genres of books'),
('Clothing', 'Apparel and accessories'),
('Home & Kitchen', 'Household items and kitchenware'),
('Sports', 'Sporting goods and outdoor equipment');

-- Create table for product_categories to establish many-to-many relationship
CREATE TABLE product_categories (
product_id INT REFERENCES products(id),
category_id INT REFERENCES categories(id),
PRIMARY KEY (product_id, category_id)
);

-- Insert sample data into product_categories
INSERT INTO product_categories (product_id, category_id) VALUES
(1, 1),
(2, 1),
(3, 1),
(4, 1),
(5, 1);

-- Create table for suppliers
CREATE TABLE suppliers (
id SERIAL PRIMARY KEY,
name VARCHAR(100) NOT NULL,
contact_name VARCHAR(100),
phone VARCHAR(15),
address TEXT
);

-- Insert 5 sample suppliers, Singapore format
INSERT INTO suppliers (name, contact_name, phone, address) VALUES
('Tech Supplies Pte Ltd', 'John Doe', '+65 9123 4567', '10 Anson Rd, Singapore'),
('Gadget World', 'Jane Smith', '+65 9234 5678', '20 Raffles Quay, Singapore'),
('ElectroMart', 'Mike Johnson', '+65 9345 6789', '30 Marina Bay Sands, Singapore'),
('Device Hub', 'Emily Davis', '+65 9456 7890', '40 Suntec City, Singapore'),
('Accessory Plus', 'Chris Lee', '+65 9567 8901', '50 VivoCity, Singapore');

-- Create table for inventory
CREATE TABLE inventory (
id SERIAL PRIMARY KEY,
product_id INT REFERENCES products(id),
supplier_id INT REFERENCES suppliers(id),
stock_quantity INT NOT NULL,
last_restocked TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert 5 sample inventory records
INSERT INTO inventory (product_id, supplier_id, stock_quantity) VALUES
(1, 1, 50),
(2, 2, 100),
(3, 3, 75),
(4, 4, 60),
(5, 5, 80);

-- Create table for payments
CREATE TABLE payments (
id SERIAL PRIMARY KEY,
order_id INT REFERENCES orders(id),
amount DECIMAL(10, 2) NOT NULL,
payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert 5 sample payments, Singapore format, round up to 5 cents
INSERT INTO payments (order_id, amount) VALUES
(1, 1000.00),
(2, 1400.00),
(3, 200.00),
(4, 300.00),
(5, 1200.00);

-- Create table for shipments
CREATE TABLE shipments (
id SERIAL PRIMARY KEY,
order_id INT REFERENCES orders(id),
shipment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
delivery_date TIMESTAMP,
status VARCHAR(50) NOT NULL
);

-- Corrected INSERT statement using DATE_ADD
INSERT INTO shipments (order_id, delivery_date, status) VALUES 
(1, DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 3 DAY), 'Shipped'),
(2, DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 5 DAY), 'Processing'),
(3, DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 2 DAY), 'Delivered'),
(4, DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 4 DAY), 'Shipped'),
(5, DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 6 DAY), 'Processing');


-- Create table for delivery_addresses
CREATE TABLE delivery_addresses (
id SERIAL PRIMARY KEY,
order_id INT REFERENCES orders(id),
recipient_name VARCHAR(100) NOT NULL,
address VARCHAR(100) NOT NULL,
city VARCHAR(50) NOT NULL,
state VARCHAR(50) NOT NULL,
postal_code VARCHAR(10) NOT NULL,
country VARCHAR(50) NOT NULL
);

-- Insert 5 sample delivery addresses, Singapore format
INSERT INTO delivery_addresses (order_id, recipient_name, address, city, state, postal_code, country) VALUES
(1, 'Alice Tan', '123 Orchard Road', 'Singapore', 'Central', '238823', 'Singapore'),
(2, 'Bob Lim', '456 Clementi Ave', 'Singapore', 'West', '129789', 'Singapore'),
(3, 'Charlie Wong', '789 Bukit Timah Rd', 'Singapore', 'Central', '259760', 'Singapore'),
(4, 'David Lee', '321 Serangoon Rd', 'Singapore', 'North-East', '218123', 'Singapore'),
(5, 'Eve Ng', '654 Jurong East St', 'Singapore', 'West', '609601', 'Singapore');

-- End of SQL script

/* Indexes for performance optimization */
-- Create an index on the username column for faster lookups
CREATE INDEX idx_username ON users(username);

-- Create an index on the product name column for faster lookups
CREATE INDEX idx_product_name ON products(name);

-- Create an index on the user_id column in orders for faster lookups
CREATE INDEX idx_order_user_id ON orders(user_id);

-- Create an index on the product_id column in orders for faster lookups
CREATE INDEX idx_order_product_id ON orders(product_id);




