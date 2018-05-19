--Current sql-script creates database indexes

CREATE INDEX periodical_email_index ON uuser (periodical_email);
CREATE INDEX id_who_booked_index ON user_item (id_who_booked);
CREATE INDEX ta_name_index ON tag (name);