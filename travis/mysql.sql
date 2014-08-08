create database querydsl;
create database querydsl2;
create user 'querydsl'@'localhost' identified by 'querydsl';
grant all privileges on querydsl.* to 'querydsl'@'localhost';
grant all privileges on querydsl2.* to 'querydsl'@'localhost';

