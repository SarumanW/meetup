package com.meetup.meetup.dao;

import com.meetup.meetup.entity.Item;
import com.meetup.meetup.entity.ItemPriority;

import java.util.List;

public interface ItemDao extends Dao<Item> {

    List<Item> findByUserId(int userId);

    List<Item> getPopularItems();

    Item addToUserWishList(int userId, int itemId, ItemPriority priority);

    Item deleteFromUserWishList(int userId, int itemId);

    Item addBookerForItem(int ownerId, int itemId, int bookerId);

    Item removeBookerForItem(int ownerId, int itemId);

    List<Item> getUserWishList(int id);

    List<Item> getRecommendations(int id);

    List<Item>  findBookingByUserId (int id);
}
