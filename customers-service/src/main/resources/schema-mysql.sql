USE `customers-db`;
    create table if not exists customers (
                                            id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                            customer_id VARCHAR(36),
        first_name VARCHAR(100),
        last_name VARCHAR(100),
        email VARCHAR(100),
        phone_number VARCHAR(100),
        password VARCHAR(100)
    );