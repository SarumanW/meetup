/*Current sql-script creates sequences and triggers
for pre-auto-incrementing the field with PRIMARY KEY constraint
in each table.
 */



DROP SEQUENCE tag_seq;
DROP TRIGGER tag_tr;

DROP SEQUENCE item_seq;
DROP TRIGGER item_tr;

DROP SEQUENCE priority_seq;
DROP TRIGGER priority_tr;

DROP SEQUENCE like_seq;
DROP TRIGGER like_tr;

DROP SEQUENCE user_seq;
DROP TRIGGER user_tr;

DROP SEQUENCE role_seq;
DROP TRIGGER role_tr;

DROP SEQUENCE folder_seq;
DROP TRIGGER folder_tr;

DROP SEQUENCE event_seq;
DROP TRIGGER event_tr;

DROP SEQUENCE periodicity_seq;
DROP TRIGGER periodicity_tr;

DROP SEQUENCE e_type_seq;
DROP TRIGGER e_type_tr;

DROP SEQUENCE chat_seq;
DROP TRIGGER chat_tr;

DROP SEQUENCE c_type_seq;
DROP TRIGGER c_type_tr;

DROP SEQUENCE message_seq;
DROP TRIGGER message_tr;

DROP SEQUENCE item_comment_seq;
DROP TRIGGER item_comment_tr;


CREATE SEQUENCE item_comment_seq
 START WITH     1
 INCREMENT BY   1
 NOCACHE
 NOCYCLE;
CREATE OR REPLACE TRIGGER item_comment_tr
BEFORE INSERT ON item_comment
FOR EACH ROW
BEGIN
  SELECT item_comment_seq.NEXTVAL
  INTO   :new.comment_id
  FROM   dual;
END;
/


CREATE SEQUENCE tag_seq
 START WITH     1
 INCREMENT BY   1
 NOCACHE
 NOCYCLE;
CREATE OR REPLACE TRIGGER tag_tr
BEFORE INSERT ON tag
FOR EACH ROW
BEGIN
  SELECT tag_seq.NEXTVAL
  INTO   :new.tag_id
  FROM   dual;
END;
/

CREATE SEQUENCE item_seq
 START WITH     1
 INCREMENT BY   1
 NOCACHE
 NOCYCLE;
CREATE OR REPLACE TRIGGER item_tr
BEFORE INSERT ON item
FOR EACH ROW
BEGIN
  SELECT item_seq.NEXTVAL
  INTO   :new.item_id
  FROM   dual;
END;
/

CREATE SEQUENCE priority_seq
 START WITH     1
 INCREMENT BY   1
 NOCACHE
 NOCYCLE;
CREATE OR REPLACE TRIGGER priority_tr
BEFORE INSERT ON priority
FOR EACH ROW
BEGIN
  SELECT priority_seq.NEXTVAL
  INTO   :new.priority_id
  FROM   dual;
END;
/

CREATE SEQUENCE like_seq
 START WITH     1
 INCREMENT BY   1
 NOCACHE
 NOCYCLE;
CREATE OR REPLACE TRIGGER like_tr
BEFORE INSERT ON llike
FOR EACH ROW
BEGIN
  SELECT like_seq.NEXTVAL
  INTO   :new.like_id
  FROM   dual;
END;
/

CREATE SEQUENCE user_seq
 START WITH     1
 INCREMENT BY   1
 NOCACHE
 NOCYCLE;
CREATE OR REPLACE TRIGGER user_tr
BEFORE INSERT ON uuser
FOR EACH ROW
BEGIN
  SELECT user_seq.NEXTVAL
  INTO   :new.user_id
  FROM   dual;
END;
/

CREATE SEQUENCE role_seq
 START WITH     1
 INCREMENT BY   1
 NOCACHE
 NOCYCLE;
CREATE OR REPLACE TRIGGER role_tr
BEFORE INSERT ON rrole
FOR EACH ROW
BEGIN
  SELECT role_seq.NEXTVAL
  INTO   :new.role_id
  FROM   dual;
END;
/

CREATE SEQUENCE folder_seq
 START WITH     1
 INCREMENT BY   1
 NOCACHE
 NOCYCLE;
CREATE OR REPLACE TRIGGER folder_tr
BEFORE INSERT ON folder
FOR EACH ROW
BEGIN
  SELECT folder_seq.NEXTVAL
  INTO   :new.folder_id
  FROM   dual;
END;
/

CREATE SEQUENCE event_seq
 START WITH     1
 INCREMENT BY   1
 NOCACHE
 NOCYCLE;
CREATE OR REPLACE TRIGGER event_tr
BEFORE INSERT ON event
FOR EACH ROW
BEGIN
  SELECT event_seq.NEXTVAL
  INTO   :new.event_id
  FROM   dual;
END;
/

CREATE SEQUENCE periodicity_seq
 START WITH     1
 INCREMENT BY   1
 NOCACHE
 NOCYCLE;
CREATE OR REPLACE TRIGGER periodicity_tr
BEFORE INSERT ON periodicity
FOR EACH ROW
BEGIN
  SELECT periodicity_seq.NEXTVAL
  INTO   :new.periodicity_id
  FROM   dual;
END;
/

CREATE SEQUENCE e_type_seq
 START WITH     1
 INCREMENT BY   1
 NOCACHE
 NOCYCLE;
CREATE OR REPLACE TRIGGER e_type_tr
BEFORE INSERT ON event_type
FOR EACH ROW
BEGIN
  SELECT e_type_seq.NEXTVAL
  INTO   :new.event_type_id
  FROM   dual;
END;
/

CREATE SEQUENCE chat_seq
 START WITH     1
 INCREMENT BY   1
 NOCACHE
 NOCYCLE;
CREATE OR REPLACE TRIGGER chat_tr
BEFORE INSERT ON chat
FOR EACH ROW
BEGIN
  SELECT chat_seq.NEXTVAL
  INTO   :new.chat_id
  FROM   dual;
END;
/

CREATE SEQUENCE c_type_seq
 START WITH     1
 INCREMENT BY   1
 NOCACHE
 NOCYCLE;
CREATE OR REPLACE TRIGGER c_type_tr
BEFORE INSERT ON chat_type
FOR EACH ROW
BEGIN
  SELECT c_type_seq.NEXTVAL
  INTO   :new.chat_type_id
  FROM   dual;
END;
/

CREATE SEQUENCE message_seq
 START WITH     1
 INCREMENT BY   1
 NOCACHE
 NOCYCLE;
CREATE OR REPLACE TRIGGER message_tr
BEFORE INSERT ON message
FOR EACH ROW
BEGIN
  SELECT message_seq.NEXTVAL
  INTO   :new.message_id
  FROM   dual;
END;
/