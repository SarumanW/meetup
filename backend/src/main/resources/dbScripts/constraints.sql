/*This sql-script creates FOREIGN KEY constraints for the database*/



ALTER TABLE tag DROP CONSTRAINT tag_fk_item;
ALTER TABLE item DROP CONSTRAINT item_fk_user;
ALTER TABLE user_item DROP CONSTRAINT u_item_fk_user;
ALTER TABLE user_item DROP CONSTRAINT u_item_fk_item;
ALTER TABLE user_item DROP CONSTRAINT u_item_fk_prior;
ALTER TABLE llike DROP CONSTRAINT like_fk_item;
ALTER TABLE llike DROP CONSTRAINT like_fk_user;
ALTER TABLE friend DROP CONSTRAINT friend_fk_user_s;
ALTER TABLE friend DROP CONSTRAINT friend_fk_user_r;
ALTER TABLE user_event DROP CONSTRAINT u_event_fk_user;
ALTER TABLE user_event DROP CONSTRAINT u_event_fk_event;
ALTER TABLE user_event DROP CONSTRAINT u_event_fk_role;
ALTER TABLE folder DROP CONSTRAINT folder_fk_user;
ALTER TABLE event DROP CONSTRAINT event_fk_period;
ALTER TABLE event DROP CONSTRAINT event_fk_e_type;
ALTER TABLE event DROP CONSTRAINT event_fk_folder;
ALTER TABLE message DROP CONSTRAINT message_fk_user;
ALTER TABLE message DROP CONSTRAINT message_fk_chat;
ALTER TABLE chat DROP CONSTRAINT chat_fk_c_type;
ALTER TABLE chat DROP CONSTRAINT chat_fk_event;
ALTER TABLE user_item DROP CONSTRAINT u_item_fk_booker;
ALTER TABLE tag_item DROP CONSTRAINT tag_item_fk_tag;
ALTER TABLE tag_item DROP CONSTRAINT tag_item_fk_item;
ALTER TABLE item_comment DROP CONSTRAINT item_comment_user_id_fk;
ALTER TABLE item_comment DROP CONSTRAINT item_comment_item_id_fk;

ALTER TABLE user_item DROP CONSTRAINT u_item_fk_booker;
ALTER TABLE tag_item DROP CONSTRAINT tag_item_fk_tag;
ALTER TABLE tag_item DROP CONSTRAINT tag_item_fk_item;
ALTER TABLE uuser DROP CONSTRAINT uuser_fk_event;




ALTER TABLE item_comment ADD CONSTRAINT item_comment_user_id_fk FOREIGN KEY (author_id) REFERENCES uuser(user_id);
ALTER TABLE item_comment ADD CONSTRAINT item_comment_item_id_fk FOREIGN KEY (item_id) REFERENCES item(item_id) ON DELETE CASCADE;

ALTER TABLE user_item ADD CONSTRAINT u_item_fk_booker FOREIGN KEY(id_who_booked) REFERENCES uuser(user_id);

ALTER TABLE user_item ADD CONSTRAINT u_item_fk_user FOREIGN KEY(user_id) REFERENCES uuser(user_id);
ALTER TABLE user_item ADD CONSTRAINT u_item_fk_item FOREIGN KEY(item_id) REFERENCES item(item_id);
ALTER TABLE user_item ADD CONSTRAINT u_item_fk_prior FOREIGN KEY(priority_id) REFERENCES priority;

ALTER TABLE llike ADD CONSTRAINT like_fk_item FOREIGN KEY(item_id) REFERENCES item(item_id) ON DELETE CASCADE;
ALTER TABLE llike ADD CONSTRAINT like_fk_user FOREIGN KEY(user_id) REFERENCES uuser(user_id);

ALTER TABLE friend ADD CONSTRAINT friend_fk_user_s FOREIGN KEY(sender_id) REFERENCES uuser(user_id);
ALTER TABLE friend ADD CONSTRAINT friend_fk_user_r FOREIGN KEY(receiver_id) REFERENCES uuser(user_id);

ALTER TABLE user_event ADD CONSTRAINT u_event_fk_user FOREIGN KEY(user_id) REFERENCES uuser(user_id);
ALTER TABLE user_event ADD CONSTRAINT u_event_fk_event FOREIGN KEY(event_id) REFERENCES event(event_id);
ALTER TABLE user_event ADD CONSTRAINT u_event_fk_role FOREIGN KEY(role_id) REFERENCES rrole(role_id);

ALTER TABLE folder ADD CONSTRAINT folder_fk_user FOREIGN KEY(user_id) REFERENCES uuser(user_id);

ALTER TABLE event ADD CONSTRAINT event_fk_period FOREIGN KEY(periodicity_id) REFERENCES periodicity(periodicity_id);
ALTER TABLE event ADD CONSTRAINT event_fk_e_type FOREIGN KEY(event_type_id) REFERENCES event_type(event_type_id);
ALTER TABLE event ADD CONSTRAINT event_fk_folder FOREIGN KEY(folder_id) REFERENCES folder(folder_id);

ALTER TABLE message ADD CONSTRAINT message_fk_user FOREIGN KEY(chat_id) REFERENCES chat(chat_id);
ALTER TABLE message ADD CONSTRAINT message_fk_chat FOREIGN KEY(sender_id) REFERENCES uuser(user_id);

ALTER TABLE chat ADD CONSTRAINT chat_fk_c_type FOREIGN KEY(chat_type_id) REFERENCES chat_type(chat_type_id);
ALTER TABLE chat ADD CONSTRAINT chat_fk_event FOREIGN KEY(event_id) REFERENCES event(event_id);

ALTER TABLE tag_item ADD CONSTRAINT tag_item_fk_tag FOREIGN KEY (tag_id) REFERENCES tag(tag_id);
ALTER TABLE tag_item ADD CONSTRAINT tag_item_fk_item FOREIGN KEY (item_id) REFERENCES item(item_id) ON DELETE CASCADE;