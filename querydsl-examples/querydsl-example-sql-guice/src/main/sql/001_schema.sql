create schema if not exists PUBLIC;

create table if not exists LOCATION
(
    ID        BIGINT auto_increment primary key,
    LATITUDE  DOUBLE,
    LONGITUDE DOUBLE,
);

create table if not exists USER
(
    ID       BIGINT auto_increment primary key,
    USERNAME VARCHAR(255)
);

create table if not exists TWEET
(
    ID          BIGINT auto_increment primary key,
    CONTENT     VARCHAR(255),
    LOCATION_ID BIGINT,
    POSTER_ID   BIGINT not null,
);

create table if not exists TWEET_USER
(
    TWEET_ID    BIGINT not null,
    MENTIONS_ID BIGINT not null,
);

