package com.meetup.meetup.dao;

import com.meetup.meetup.entity.Item;

import java.util.List;

public interface ItemDao extends Dao<Item> {

    List<Item> findByUserId(int userId);

    List<Item> getPopularItems();

    List<Item> getUserWishList(int userId);
}
