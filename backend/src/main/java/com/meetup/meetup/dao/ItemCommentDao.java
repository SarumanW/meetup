package com.meetup.meetup.dao;

import com.meetup.meetup.entity.ItemComment;

import java.util.List;
import java.util.Map;

public interface ItemCommentDao extends Dao<ItemComment> {
    List<ItemComment> getCommentsForItemId(int id);
}
