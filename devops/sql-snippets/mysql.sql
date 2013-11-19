CREATE DATABASE querydsl CHARACTER SET utf8 COLLATE utf8_swedish_ci;

# For access from within the Vagrant box
CREATE USER 'querydsl'@'localhost' IDENTIFIED BY 'querydsl';
GRANT ALL PRIVILEGES ON querydsl.* TO 'querydsl'@'localhost';

# Host access
CREATE USER 'querydsl'@'%' IDENTIFIED BY 'querydsl';
GRANT ALL PRIVILEGES ON querydsl.* TO 'querydsl'@'%';
