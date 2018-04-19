/*This sql-script creates FOREIGN KEY constraints for the database*/



ALTER TABLE tags DROP CONSTRAINT tags_fk_items;
ALTER TABLE items DROP CONSTRAINT items_fk_users;
ALTER TABLE user_items DROP CONSTRAINT u_items_fk_users;
ALTER TABLE user_items DROP CONSTRAINT u_items_fk_items;
ALTER TABLE user_items DROP CONSTRAINT u_items_fk_prior;
ALTER TABLE likes DROP CONSTRAINT likes_fk_items;
ALTER TABLE likes DROP CONSTRAINT likes_fk_users;
ALTER TABLE friends DROP CONSTRAINT friends_fk_users_s;
ALTER TABLE friends DROP CONSTRAINT friends_fk_users_r;
ALTER TABLE user_events DROP CONSTRAINT u_events_fk_users;
ALTER TABLE user_events DROP CONSTRAINT u_events_fk_events;
ALTER TABLE user_events DROP CONSTRAINT u_events_fk_roles;
ALTER TABLE folders DROP CONSTRAINT folders_fk_users;
ALTER TABLE events DROP CONSTRAINT events_fk_period;
ALTER TABLE events DROP CONSTRAINT events_fk_e_types;
ALTER TABLE events DROP CONSTRAINT events_fk_folders;
ALTER TABLE messages DROP CONSTRAINT messages_fk_users;
ALTER TABLE messages DROP CONSTRAINT messages_fk_chats;
ALTER TABLE chats DROP CONSTRAINT chats_fk_c_types;
ALTER TABLE chats DROP CONSTRAINT chats_fk_events;





ALTER TABLE tags ADD CONSTRAINT tags_fk_items FOREIGN KEY(item_id) REFERENCES items;

ALTER TABLE items ADD CONSTRAINT items_fk_users FOREIGN KEY(id_who_booked) REFERENCES users(user_id);

ALTER TABLE user_items ADD CONSTRAINT u_items_fk_users FOREIGN KEY(user_id) REFERENCES users(user_id);
ALTER TABLE user_items ADD CONSTRAINT u_items_fk_items FOREIGN KEY(item_id) REFERENCES items;
ALTER TABLE user_items ADD CONSTRAINT u_items_fk_prior FOREIGN KEY(priority_id) REFERENCES priorities;

ALTER TABLE likes ADD CONSTRAINT likes_fk_items FOREIGN KEY(item_id) REFERENCES items;
ALTER TABLE likes ADD CONSTRAINT likes_fk_users FOREIGN KEY(user_id) REFERENCES users(user_id);

ALTER TABLE friends ADD CONSTRAINT friends_fk_users_s FOREIGN KEY(sender_id) REFERENCES users;
ALTER TABLE friends ADD CONSTRAINT friends_fk_users_r FOREIGN KEY(receiver_id) REFERENCES users(user_id);

ALTER TABLE user_events ADD CONSTRAINT u_events_fk_users FOREIGN KEY(user_id) REFERENCES users(user_id);
ALTER TABLE user_events ADD CONSTRAINT u_events_fk_events FOREIGN KEY(event_id) REFERENCES events;
ALTER TABLE user_events ADD CONSTRAINT u_events_fk_roles FOREIGN KEY(role_id) REFERENCES roles;

ALTER TABLE folders ADD CONSTRAINT folders_fk_users FOREIGN KEY(user_id) REFERENCES users(user_id);

ALTER TABLE events ADD CONSTRAINT events_fk_period FOREIGN KEY(periodicity_id) REFERENCES periodicities;
ALTER TABLE events ADD CONSTRAINT events_fk_e_types FOREIGN KEY(event_type_id) REFERENCES event_types;
ALTER TABLE events ADD CONSTRAINT events_fk_folders FOREIGN KEY(folder_id) REFERENCES folders;

ALTER TABLE messages ADD CONSTRAINT messages_fk_users FOREIGN KEY(chat_id) REFERENCES chats;
ALTER TABLE messages ADD CONSTRAINT messages_fk_chats FOREIGN KEY(sender_id) REFERENCES users(user_id);

ALTER TABLE chats ADD CONSTRAINT chats_fk_c_types FOREIGN KEY(chat_type_id) REFERENCES chat_types;
ALTER TABLE chats ADD CONSTRAINT chats_fk_events FOREIGN KEY(event_id) REFERENCES users(user_id);
