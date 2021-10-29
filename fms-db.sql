drop table if exists Users;
drop table if exists Persons;
drop table if exists Events;
drop table if exists Tokens;

create table Users (
	username varchar(255) not null primary key,
	password varchar(255) not null,
	email varchar(255) not null,
	first_name varchar(255) not null,
	last_name varchar(255) not null,
	--- TODO: Figure out how to constrain this to just "f" and "m" here
	gender varchar(1) not null,
	person_id varchar(255) not null
);

create table Persons (
	id varchar(255) not null primary key,
	username varchar(255) not null,
	first_name varchar(255) not null,
	last_name varchar(255) not null,
	gender varchar(1) not null,
	father_id varchar(255),
	mother_id varchar(255),
	spouse_id varchar(255)
);

create table Events (
	id varchar(255) not null primary key,
	username varchar(255) not null,
	person_id varchar(255) not null,
	latitude real not null,
	longitude real not null,
	country varchar(511) not null,
	city varchar(511) not null,
    year integer not null,
	event_type varchar(255) not null
);

create table Tokens (
	token varchar(255) not null primary key,
	username varchar(255) not null,
	foreign key(username) references users(username)
);
