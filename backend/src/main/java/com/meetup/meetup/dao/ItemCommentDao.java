package com.meetup.meetup.dao;

import com.meetup.meetup.entity.ItemComment;

import java.util.List;

public interface ItemCommentDao extends Dao<ItemComment> {
    List<ItemComment> getCommentsForItemId(int id);
}
