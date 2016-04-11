# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table city (
  name                      varchar(255) not null,
  constraint pk_city primary key (name))
;

create table ingredient (
  name                      varchar(255) not null,
  alternative_name          varchar(255),
  image_path                varchar(255),
  constraint pk_ingredient primary key (name))
;

create table menu_item (
  id                        bigserial not null,
  name                      varchar(255),
  image_path                varchar(255),
  category_name             varchar(255),
  restaurant_id             bigint,
  constraint pk_menu_item primary key (id))
;

create table menu_item_category (
  name                      varchar(255) not null,
  type                      varchar(1),
  image_path                varchar(255),
  constraint ck_menu_item_category_type check (type in ('S','R')),
  constraint pk_menu_item_category primary key (name))
;

create table picture (
  name                      varchar(255) not null,
  content_type              varchar(255),
  data                      bytea,
  constraint pk_picture primary key (name))
;

create table restaurant (
  id                        bigserial not null,
  name                      varchar(255),
  logo_path                 varchar(255),
  image_path                varchar(255),
  latitude                  float,
  longitude                 float,
  address                   varchar(255),
  google_id                 varchar(255),
  phone                     varchar(255),
  city_name                 varchar(255),
  rating                    integer,
  constraint pk_restaurant primary key (id))
;

create table review (
  id                        bigserial not null,
  author                    varchar(255),
  author_image_path         varchar(255),
  text                      varchar(255),
  restaurant_id             bigint,
  constraint pk_review primary key (id))
;

create table suggestion (
  id                        bigserial not null,
  text                      varchar(255),
  constraint pk_suggestion primary key (id))
;


create table ingredient_menu_item (
  ingredient_name                varchar(255) not null,
  menu_item_id                   bigint not null,
  constraint pk_ingredient_menu_item primary key (ingredient_name, menu_item_id))
;

create table menu_item_ingredient (
  menu_item_id                   bigint not null,
  ingredient_name                varchar(255) not null,
  constraint pk_menu_item_ingredient primary key (menu_item_id, ingredient_name))
;
alter table menu_item add constraint fk_menu_item_category_1 foreign key (category_name) references menu_item_category (name);
create index ix_menu_item_category_1 on menu_item (category_name);
alter table menu_item add constraint fk_menu_item_restaurant_2 foreign key (restaurant_id) references restaurant (id);
create index ix_menu_item_restaurant_2 on menu_item (restaurant_id);
alter table restaurant add constraint fk_restaurant_city_3 foreign key (city_name) references city (name);
create index ix_restaurant_city_3 on restaurant (city_name);
alter table review add constraint fk_review_restaurant_4 foreign key (restaurant_id) references restaurant (id);
create index ix_review_restaurant_4 on review (restaurant_id);



alter table ingredient_menu_item add constraint fk_ingredient_menu_item_ingre_01 foreign key (ingredient_name) references ingredient (name);

alter table ingredient_menu_item add constraint fk_ingredient_menu_item_menu__02 foreign key (menu_item_id) references menu_item (id);

alter table menu_item_ingredient add constraint fk_menu_item_ingredient_menu__01 foreign key (menu_item_id) references menu_item (id);

alter table menu_item_ingredient add constraint fk_menu_item_ingredient_ingre_02 foreign key (ingredient_name) references ingredient (name);

# --- !Downs

drop table if exists city cascade;

drop table if exists ingredient cascade;

drop table if exists ingredient_menu_item cascade;

drop table if exists menu_item cascade;

drop table if exists menu_item_ingredient cascade;

drop table if exists menu_item_category cascade;

drop table if exists picture cascade;

drop table if exists restaurant cascade;

drop table if exists review cascade;

drop table if exists suggestion cascade;

