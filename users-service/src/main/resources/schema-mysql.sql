USE `users-db`;
    create table if not exists users (
                                            id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                            user_id VARCHAR(36),
        first_name VARCHAR(100),
        last_name VARCHAR(100),
        email VARCHAR(100) UNIQUE ,
        phone_number VARCHAR(100),
        password VARCHAR(100)
                                         );


create table if not exists reset_password_token (
                                         id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                         token VARCHAR(36),
                                         user_id VARCHAR(36) UNIQUE,
                                         expiry_date DATETIME
);