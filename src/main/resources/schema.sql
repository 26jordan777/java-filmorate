  create table if not exists USERS (
      id integer not null primary key auto_increment,
      email varchar(255) not null,
      login varchar(255) not null,
      name varchar(255) not null,
      birthday date not null
  );

  create table if not exists FILM_GENRES (
    film_id BIGINT NOT NULL,
    genre_id BIGINT NOT NULL,
    PRIMARY KEY (film_id, genre_id),
    FOREIGN KEY (film_id) REFERENCES FILMS(id),
    FOREIGN KEY (genre_id) REFERENCES GENRES(id)
);


  create table if not exists FRIENDSHIP (
    user_id BIGINT NOT NULL,
    friend_id BIGINT NOT NULL,
    status BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (user_id, friend_id),
    FOREIGN KEY (user_id) REFERENCES USERS(id),
    FOREIGN KEY (friend_id) REFERENCES USERS(id)
);


  create table if not exists FILMS (
      film_id integer primary key auto_increment,
      name VARCHAR(255) not null,
      description VARCHAR(200),
      release_date DATE not null,
      duration BIGINT not null
  );

  create table if not exists GENRES (
      genres_id integer primary key auto_increment,
      name VARCHAR(255) not null
  );

  create table if not exists MPA (
      mpa_id integer primary key auto_increment,
      name VARCHAR(255) not null
  );
