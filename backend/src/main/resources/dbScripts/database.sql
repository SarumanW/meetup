/*Current sql-script creates database tables
with  PRIMARY KEY and UNIQUE constraints.
 */


DROP TABLE tags;
DROP TABLE likes;
DROP TABLE user_items;
DROP TABLE items;
DROP TABLE priorities;
DROP TABLE messages;
DROP TABLE chats;
DROP TABLE chat_types;
DROP TABLE user_events;
DROP TABLE roles;
DROP TABLE friends;
DROP TABLE events;
DROP TABLE folders;
DROP TABLE periodicities;
DROP TABLE event_types;
DROP TABLE users;



CREATE TABLE users (
  user_id number,
  Login varchar2(50) NOT NULL UNIQUE,
  password varchar2(50) NOT NULL,
  name varchar2(254) NOT NULL,
  surname varchar2(254) NOT NULL,
  email varchar2(100) NOT NULL UNIQUE,
  time_zone timestamp,
  image_filepath varchar2(200),
  birthday Date,
  phone varchar2(50),
  PRIMARY KEY (user_id)
);

CREATE TABLE user_items (
  user_id number,
  item_id number,
  priority_id number,
  UNIQUE (user_id, item_id)
);

CREATE TABLE priorities (
  priority_id number,
  name varchar2(50) NOT NULL,
  PRIMARY KEY (priority_id)
);

CREATE TABLE likes (
  like_id number,
  item_id number,
  user_id number,
  PRIMARY KEY (like_id),
  UNIQUE (item_id, user_id)
);

CREATE TABLE items (
  item_id number,
  name varchar2(50) NOT NULL,
  description varchar2(200),
  id_who_booked number,
  image_filepath varchar2(200),
  link varchar2(200),
  due_date timestamp,
  PRIMARY KEY (item_id)
);

CREATE TABLE tags (
  tag_id number,
  item_id number,
  name varchar2(20),
  PRIMARY KEY (tag_id)
);











CREATE TABLE friends (
  sender_id number,
  receiver_id number,
  is_Confirmed number(1) NOT NULL,
  UNIQUE (sender_id, receiver_id)
);

CREATE TABLE user_events (
  user_id number,
  event_id number,
  role_id number,
  UNIQUE (user_id, event_id)
);

CREATE TABLE roles (
  role_id number,
  name varchar2(10) NOT NULL,
  PRIMARY KEY (role_id)
);

CREATE TABLE folders (
  folder_id number,
  name varchar2(100) NOT NULL,
  user_id number,
  PRIMARY KEY (folder_id)
);

CREATE TABLE events (
  event_id number,
  name varchar2(50) NOT NULL,
  event_date timestamp,
  description varchar2(250),
  periodicity_id number,
  place varchar2(100),
  event_type_id number,
  is_draft number(1) NOT NULL,
  folder_id number,
  PRIMARY KEY (event_id)
);

CREATE TABLE messages (
  message_id number,
  sender_id number,
  text varchar2(250) NOT NULL,
  message_date Date NOT NULL,
  chat_id number,
  PRIMARY KEY (message_id)
);

CREATE TABLE periodicities (
  periodicity_id number,
  periodicity_name varchar2(20) NOT NULL,
  PRIMARY KEY (periodicity_id)
);

CREATE TABLE event_types (
  event_type_id number,
  type varchar(50) NOT NULL,
  PRIMARY KEY (event_type_id)
);

CREATE TABLE chats (
  chat_id number,
  chat_type_id number,
  event_id number,
  PRIMARY KEY (chat_id)
);

CREATE TABLE chat_types (
  chat_type_id number,
  type varchar2(10) NOT NULL,
  PRIMARY KEY (chat_type_id)
);