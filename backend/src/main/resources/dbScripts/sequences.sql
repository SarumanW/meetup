/*Current sql-script creates sequences and triggers
for pre-auto-incrementing the field with PRIMARY KEY constraint
in each table.
 */



DROP SEQUENCE tags_seq;
DROP TRIGGER tags_tr;

DROP SEQUENCE items_seq;
DROP TRIGGER items_tr;

DROP SEQUENCE priorities_seq;
DROP TRIGGER priorities_tr;

DROP SEQUENCE likes_seq;
DROP TRIGGER likes_tr;

DROP SEQUENCE users_seq;
DROP TRIGGER users_tr;

DROP SEQUENCE roles_seq;
DROP TRIGGER roles_tr;

DROP SEQUENCE folders_seq;
DROP TRIGGER folders_tr;

DROP SEQUENCE events_seq;
DROP TRIGGER events_tr;

DROP SEQUENCE periodicities_seq;
DROP TRIGGER periodicities_tr;

DROP SEQUENCE e_types_seq;
DROP TRIGGER e_types_tr;

DROP SEQUENCE chats_seq;
DROP TRIGGER chats_tr;

DROP SEQUENCE c_types_seq;
DROP TRIGGER c_types_tr;

DROP SEQUENCE messages_seq;
DROP TRIGGER messages_tr;




CREATE SEQUENCE tags_seq
 START WITH     1
 INCREMENT BY   1
 NOCACHE
 NOCYCLE;
CREATE OR REPLACE TRIGGER tags_tr
BEFORE INSERT ON tags
FOR EACH ROW
BEGIN
  SELECT tags_seq.NEXTVAL
  INTO   :new.tag_id
  FROM   dual;
END;
/

CREATE SEQUENCE items_seq
 START WITH     1
 INCREMENT BY   1
 NOCACHE
 NOCYCLE;
CREATE OR REPLACE TRIGGER items_tr
BEFORE INSERT ON items
FOR EACH ROW
BEGIN
  SELECT items_seq.NEXTVAL
  INTO   :new.item_id
  FROM   dual;
END;
/

CREATE SEQUENCE priorities_seq
 START WITH     1
 INCREMENT BY   1
 NOCACHE
 NOCYCLE;
CREATE OR REPLACE TRIGGER priorities_tr
BEFORE INSERT ON priorities
FOR EACH ROW
BEGIN
  SELECT priorities_seq.NEXTVAL
  INTO   :new.priority_id
  FROM   dual;
END;
/

CREATE SEQUENCE likes_seq
 START WITH     1
 INCREMENT BY   1
 NOCACHE
 NOCYCLE;
CREATE OR REPLACE TRIGGER likes_tr
BEFORE INSERT ON likes
FOR EACH ROW
BEGIN
  SELECT likes_seq.NEXTVAL
  INTO   :new.like_id
  FROM   dual;
END;
/

CREATE SEQUENCE users_seq
 START WITH     1
 INCREMENT BY   1
 NOCACHE
 NOCYCLE;
CREATE OR REPLACE TRIGGER users_tr
BEFORE INSERT ON users
FOR EACH ROW
BEGIN
  SELECT users_seq.NEXTVAL
  INTO   :new.user_id
  FROM   dual;
END;
/

CREATE SEQUENCE roles_seq
 START WITH     1
 INCREMENT BY   1
 NOCACHE
 NOCYCLE;
CREATE OR REPLACE TRIGGER roles_tr
BEFORE INSERT ON roles
FOR EACH ROW
BEGIN
  SELECT roles_seq.NEXTVAL
  INTO   :new.role_id
  FROM   dual;
END;
/

CREATE SEQUENCE folders_seq
 START WITH     1
 INCREMENT BY   1
 NOCACHE
 NOCYCLE;
CREATE OR REPLACE TRIGGER folders_tr
BEFORE INSERT ON folders
FOR EACH ROW
BEGIN
  SELECT folders_seq.NEXTVAL
  INTO   :new.folder_id
  FROM   dual;
END;
/

CREATE SEQUENCE events_seq
 START WITH     1
 INCREMENT BY   1
 NOCACHE
 NOCYCLE;
CREATE OR REPLACE TRIGGER events_tr
BEFORE INSERT ON events
FOR EACH ROW
BEGIN
  SELECT events_seq.NEXTVAL
  INTO   :new.event_id
  FROM   dual;
END;
/

CREATE SEQUENCE periodicities_seq
 START WITH     1
 INCREMENT BY   1
 NOCACHE
 NOCYCLE;
CREATE OR REPLACE TRIGGER periodicities_tr
BEFORE INSERT ON periodicities
FOR EACH ROW
BEGIN
  SELECT periodicities_seq.NEXTVAL
  INTO   :new.periodicity_id
  FROM   dual;
END;
/

CREATE SEQUENCE e_types_seq
 START WITH     1
 INCREMENT BY   1
 NOCACHE
 NOCYCLE;
CREATE OR REPLACE TRIGGER e_types_tr
BEFORE INSERT ON event_types
FOR EACH ROW
BEGIN
  SELECT e_types_seq.NEXTVAL
  INTO   :new.event_type_id
  FROM   dual;
END;
/

CREATE SEQUENCE chats_seq
 START WITH     1
 INCREMENT BY   1
 NOCACHE
 NOCYCLE;
CREATE OR REPLACE TRIGGER chats_tr
BEFORE INSERT ON chats
FOR EACH ROW
BEGIN
  SELECT chats_seq.NEXTVAL
  INTO   :new.chat_id
  FROM   dual;
END;
/

CREATE SEQUENCE c_types_seq
 START WITH     1
 INCREMENT BY   1
 NOCACHE
 NOCYCLE;
CREATE OR REPLACE TRIGGER c_types_tr
BEFORE INSERT ON chat_types
FOR EACH ROW
BEGIN
  SELECT c_types_seq.NEXTVAL
  INTO   :new.chat_type_id
  FROM   dual;
END;
/

CREATE SEQUENCE messages_seq
 START WITH     1
 INCREMENT BY   1
 NOCACHE
 NOCYCLE;
CREATE OR REPLACE TRIGGER messages_tr
BEFORE INSERT ON messages
FOR EACH ROW
BEGIN
  SELECT messages_seq.NEXTVAL
  INTO   :new.message_id
  FROM   dual;
END;
/