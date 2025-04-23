   CREATE TABLE IF NOT EXISTS films (
       id BIGINT AUTO_INCREMENT PRIMARY KEY,
       name VARCHAR(255) NOT NULL,
       description VARCHAR(200),
       release_date DATE NOT NULL,
       duration BIGINT NOT NULL
   );

   CREATE TABLE IF NOT EXISTS users (
       id BIGINT AUTO_INCREMENT PRIMARY KEY,
       email VARCHAR(255) NOT NULL,
       login VARCHAR(255) NOT NULL,
       name VARCHAR(255),
       birthday DATE NOT NULL
   );