/*Current sql-script creates database tables
with  PRIMARY KEY and UNIQUE constraints.
 */

DROP TABLE tag;
DROP TABLE llike;
DROP TABLE user_item;
DROP TABLE item;
DROP TABLE priority;
DROP TABLE message;
DROP TABLE chat;
DROP TABLE chat_type;
DROP TABLE user_event;
DROP TABLE rrole;
DROP TABLE friend;
DROP TABLE event;
DROP TABLE folder;
DROP TABLE periodicity;
DROP TABLE event_type;
DROP TABLE uuser;

CREATE TABLE uuser (
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

CREATE TABLE user_item (
  user_id number NOT NULL,
  item_id number NOT NULL,
  priority_id number,
  UNIQUE (user_id, item_id)
);

CREATE TABLE priority (
  priority_id number,
  name varchar2(50) NOT NULL,
  PRIMARY KEY (priority_id)
);

CREATE TABLE llike (
  like_id number,
  item_id number,
  user_id number,
  PRIMARY KEY (like_id),
  UNIQUE (item_id, user_id)
);

CREATE TABLE item (
  item_id number,
  name varchar2(50) NOT NULL,
  description varchar2(200),
  id_who_booked number,
  image_filepath varchar2(200),
  link varchar2(200),
  due_date timestamp,
  PRIMARY KEY (item_id)
);

CREATE TABLE tag (
  tag_id number,
  item_id number,
  name varchar2(20),
  PRIMARY KEY (tag_id)
);

CREATE TABLE friend (
  sender_id number,
  receiver_id number,
  is_Confirmed number(1) NOT NULL,
  UNIQUE (sender_id, receiver_id)
);

CREATE TABLE user_event (
  user_id number NOT NULL,
  event_id number NOT NULL,
  role_id number NOT NULL,
  UNIQUE (user_id, event_id)
);

CREATE TABLE rrole (
  role_id number,
  name varchar2(10) NOT NULL,
  PRIMARY KEY (role_id)
);

CREATE TABLE folder (
  folder_id number,
  name varchar2(100) NOT NULL,
  user_id number NOT NULL,
  PRIMARY KEY (folder_id)
);

CREATE TABLE event (
  event_id number,
  name varchar2(50) NOT NULL,
  event_date timestamp,
  description varchar2(250) NOT NULL,
  periodicity_id number,
  place varchar2(100),
  event_type_id number NOT NULL,
  is_draft number(1) NOT NULL,
  folder_id number NOT NULL,
  PRIMARY KEY (event_id)
);

CREATE TABLE message (
  message_id number,
  sender_id number,
  text varchar2(250) NOT NULL,
  message_date Date NOT NULL,
  chat_id number,
  PRIMARY KEY (message_id)
);

CREATE TABLE periodicity (
  periodicity_id number,
  periodicity_name varchar2(20) NOT NULL,
  PRIMARY KEY (periodicity_id)
);

CREATE TABLE event_type (
  event_type_id number,
  type varchar(50) NOT NULL,
  PRIMARY KEY (event_type_id)
);

CREATE TABLE chat (
  chat_id number,
  chat_type_id number,
  event_id number,
  PRIMARY KEY (chat_id)
);

CREATE TABLE chat_type (
  chat_type_id number,
  type varchar2(10) NOT NULL,
  PRIMARY KEY (chat_type_id)
);