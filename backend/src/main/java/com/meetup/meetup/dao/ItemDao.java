package com.meetup.meetup.dao;

import com.meetup.meetup.entity.Item;
import com.meetup.meetup.entity.ItemPriority;

import java.util.List;

public interface ItemDao extends Dao<Item> {

    List<Item> getWishListByUserId(int userId);

    Item findByUserIdItemId(int userId, int itemId);

    List<Item> getPopularItems();

    Item addToUserWishList(Item item);

    Item deleteFromUserWishList(int userId, int itemId);

    Item addBookerForItem(int ownerId, int itemId, int bookerId);

    Item removeBookerForItem(int ownerId, int itemId, int bookerId);

    Item addLike(int itemId, int userId);

    Item removeLike(int itemId, int userId);

    List<Item> findBookedItemsByUserId(int userId);

    List<Item> findItemsByTagName(String[] tagName);

    List<String> searchTag(String aboutTag);

    List<String> getLoginsWhoLikedItem(int itemId);

    boolean itemExistInWishList(Item item);
}
