create database shopapp;
use shopapp;
create table roles(
 id int primary key,
 name varchar(20) not null
);
create table users(
id int primary key auto_increment,
fullname varchar(100) default '',
phone_number varchar(20) not null,
address varchar(200) default '',
password varchar(100) not null default '',
created_at DATETIME,
updated_at DATETIME,
is_active TINYINT(1) DEFAULT 1,
date_of_birth DATE,
facebook_account_id INT DEFAULT 0,
google_account_id int default 0,
roles_id int,
foreign key (roles_id) references roles(id)
);
create table tokens(
 id int primary key auto_increment,
 token varchar(255) unique not null,
 token_type varchar(50) not null,
 expiration_date datetime,
 revoked tinyint(1) not null,
 expired tinyint(1) not null,
 user_id int,
 foreign key (user_id) references users(id)
);
create table social_accounts(
	id int primary key auto_increment,
    provider varchar(20) not null comment 'Name house of social network',
    email varchar(150) not null comment 'email account',
    name varchar(100) not null comment 'usename',
    user_id int,
    foreign key (user_id) references users(id)
);
create table categories(
	id int primary key auto_increment,
    name varchar(100) not null default '' comment 'name category'
);
create table products(
	id int primary key auto_increment,
    name varchar(350) comment 'name product',
    price float not null check(price >=0),
    thumbnail varchar(300) default '',
    description longtext,
    created_at datetime,
    updated_at datetime,
    category_id int,
    foreign key (category_id) references categories(id)
);
create table product_images(
  id int primary key auto_increment,
  product_id int,
  foreign key (product_id) references products(id),
  constraint fk_product_images_product_id foreign key (product_id) references products(id) on delete cascade,
  url_image varchar(300)
);
create table orders(
 id int primary key auto_increment,
 user_id int,
 foreign key (user_id) references users(id),
 fullname varchar(100) default '',
 email varchar(100) default '',
 phone_number varchar(20) not null,
 tracking_number varchar(100) default'',
 address varchar(200) not null,
 shipping_address varchar(200) default'',
 shipping_method varchar(100) default '',
 note varchar(100) default '',
 order_date datetime default current_timestamp,
 shipping_date date,
 status enum('PENDING','PROCESSING','SHIPPED','DELIVERED','CANCELLED') comment 'status bill',
 payment_menthod varchar(100),
 total_money float check(total_money >=0),
 active tinyint(1)
);
create table order_detailes(
  id int primary key auto_increment,
  order_id int,
  foreign key (order_id) references orders(id),
  product_id int,
  foreign key (product_id) references products(id),
  price float check(price>=0),
  number_of_products int check(number_of_products >0),
  total_money float check(total_money >=0),
  color varchar(20) default ''
);