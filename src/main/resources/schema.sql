  create table if not exists USERS (
      user_id integer not null primary key auto_increment,
      email varchar(255) not null,
      login varchar(255) not null,
      name varchar(255) not null,
      birthday date not null
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
