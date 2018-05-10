package com.meetup.meetup.keys;

public final class Key {

    private static Key instance = null;

    //TABLES
    public static final String TABLE_UUSER = "UUSER";
    public static final String TABLE_FRIEND = "FRIEND";
    public static final String TABLE_EVENT = "EVENT";
    public static final String TABLE_USER_EVENT = "USER_EVENT";
    public static final String TABLE_FOLDER = "FOLDER";
    public static final String TABLE_ITEM = "ITEM";
    public static final String TABLE_TAG = "TAG";


    //ITEM table fields
    public static final String ITEM_ITEM_ID = "ITEM_ID";
    public static final String ITEM_NAME = "NAME";
    public static final String ITEM_DESCRIPTION = "DESCRIPTION";
    public static final String ITEM_IMAGE_FILEPATH = "IMAGE_FILEPATH";
    public static final String ITEM_LINK = "LINK";
    public static final String ITEM_DUE_DATE = "DUE_DATE";
    public static final String ITEM_LIKES = "LIKES";

    //USER_ITEM table fields
    public static final String USER_ITEM_USER_ID = "USER_ID";
    public static final String USER_ITEM_BOOKER_ID = "ID_WHO_BOOKED";
    public static final String USER_ITEM_PRIORITY_ID = "PRIORITY_ID";

    //LLIKE table field
    public static final String LLIKE_LIKE_ID = "LIKE_ID";

    //TAG table fields
    public static final String TAG_TAG_ID = "TAG_ID";
    public static final String TAG_TAG_NAME = "NAME";


    //UUSER table fields
    public static final String UUSER_USER_ID = "USER_ID";
    public static final String UUSER_LOGIN = "login";
    public static final String UUSER_PASSWORD = "password";
    public static final String UUSER_NAME = "name";
    public static final String UUSER_SURNAME = "surname";
    public static final String UUSER_EMAIL = "email";
    public static final String UUSER_TIMEZONE = "timezone";
    public static final String UUSER_IMAGE_FILEPATH = "image_filepath";
    public static final String UUSER_BDAY = "bday";
    public static final String UUSER_PHONE = "phone";

    //FRIEND table fields
    public static final String FRIEND_SENDER_ID = "SENDER_ID";
    public static final String FRIEND_RECEIVER_ID = "RECEIVER_ID";
    public static final String FRIEND_IS_CONFIRMED = "IS_CONFIRMED";

    //EVENT table fields
    public static final String EVENT_EVENT_ID = "EVENT_ID";
    public static final String EVENT_NAME = "NAME";
    public static final String EVENT_EVENT_DATE = "EVENT_DATE";
    public static final String EVENT_PERIODICITY_ID = "PERIODICITY_ID";
    public static final String EVENT_DESCRIPTION = "DESCRIPTION";
    public static final String EVENT_PLACE = "PLACE";
    public static final String EVENT_EVENT_TYPE_ID = "EVENT_TYPE_ID";
    public static final String EVENT_IS_DRAFT = "IS_DRAFT";
    public static final String EVENT_FOLDER_ID = "FOLDER_ID";
    public static final String EVENT_IMAGE_FILEPATH = "IMAGE_FILEPATH";

    //USER_EVENT table fields
    public static final String USER_EVENT_USER_ID = "USER_ID";
    public static final String USER_EVENT_EVENT_ID = "EVENT_ID";
    public static final String USER_EVENT_ROLE_ID = "ROLE_ID";

    //FOLDER table fields
    public static final String FOLDER_FOLDER_ID = "FOLDER_ID";
    public static final String FOLDER_NAME = "name";
    public static final String FOLDER_USER_ID = "user_id";

    //UserDao
    public static final String USER_FIND_BY_LOGIN = "user.findByLogin";
    public static final String USER_FIND_BY_ID = "user.findById";
    public static final String USER_FIND_BY_EMAIL = "user.findByEmail";
    public static final String USER_UPDATE = "user.update";
    public static final String USER_UPDATE_PASSWORD = "user.updatePassword";
    public static final String USER_DELETE = "user.delete";
    public static final String USER_GET_FRIENDS = "user.getFriends";
    public static final String USER_GET_UNCONFIRMED = "user.getUnconfirmedFriends";
    public static final String USER_CONFIRM_FRIEND = "user.confirmFriend";
    public static final String USER_DELETE_FRIEND = "user.deleteFriend";
    public static final String USER_IS_LOGIN_FREE = "user.isLoginFree";
    public static final String USER_IS_EMAIL_FREE = "user.isEmailFree";

    //FolderDao
    public static final String FOLDER_GET_USER_FOLDERS = "folder.getUserFolders";
    public static final String FOLDER_GET_BY_ID = "folder.getById";
    public static final String FOLDER_GET_BY_NAME = "folder.getByName";
    public static final String FOLDER_UPDATE = "folder.update";
    public static final String FOLDER_DELETE = "folder.delete";
    public static final String FOLDER_REMOVE_EVENTS = "folder.removeEvents";

    //EventDao
    public static final String EVENT_FIND_BY_USER_ID="event.findByUserId";
    public static final String EVENT_FIND_BY_ID="event.findById";
    public static final String EVENT_UPDATE="event.update";
    public static final String EVENT_DELETE="event.delete";
    public static final String EVENT_FIND_BY_FOLDER_ID="event.findByFolderId";
    public static final String EVENT_GET_PARTICIPANTS="event.getParticipants";
    public static final String EVENT_FIND_BY_TYPE_IN_FOLDER="event.findByTypeInFolder";
    public static final String EVENT_GET_DRAFTS="event.getDrafts";
    public static final String EVENT_GET_IN_PERIOD="event.getInPeriod";
    public static final String EVENT_GET_ALL_PUBLIC="event.getAllPublic";
    public static final String EVENT_DELETE_PARTICIPANTS="event.deleteParticipants";
    public static final String EVENT_DELETE_MEMBERS="event.deleteMembers";
    public static final String EVENT_DELETE_PARTICIPANT="event.deleteParticipant";
    //RoleDao
    public static final String GET_ROLE = "role.getRole";

    //ItemDao
    public static final String ITEM_FIND_BY_ID = "item.findById";
    public static final String ITEM_GET_ITEMS_ID_BY_USER_ID = "item.getItemsIdByUserId";
    public static final String ITEM_DELETE = "item.delete";
    public static final String ITEM_UPDATE = "item.update";
    public static final String ITEM_GET_TAG_BY_ITEM_ID = "item.getTagByItemId";
    public static final String ITEM_GET_POPULAR_ITEMS_ID ="item.getPopularItemsIds";
    public static final String ITEM_GET_PERSONAL_INFO_BY_ITEM_ID_USER_ID = "item.getPersonalInfoByUserIdItemId";
    public static final String ITEM_UPDATE_USER_ITEM = "item.updateUserItem";
    public static final String ITEM_DELETE_FROM_WISH_LIST = "item.deleteFromWishList";
    public static final String ITEM_SET_BOOKER_ID_FOR_ITEM = "item.setBookerId";
    public static final String ITEM_GET_NUMBER_OF_ITEM_USERS = "item.getNumberOfItemUsers";
    public static final String ITEM_GET_TAG_ID = "item.getTagIdByName";
    public static final String ITEM_ADD_TAG_TO_ITEM = "item.addTagToItem";
    public static final String ITEM_DELETE_TAGS = "item.deleteTags";
    public static final String ITEM_ADD_LIKE_BY_ITEM_ID_USER_ID = "item.addLikeByItemIdUserId";
    public static final String ITEM_REMOVE_LIKE_BY_ITEM_ID_USER_ID = "item.removeLikeByItemIdUserId";
    public static final String ITEM_UPDATE_USER_ITEM_INFO = "item.updateUserItemInfo";
    public static final String ITEM_GET_BOOKED_ITEMS_BY_USER_ID = "item.getBookedItemsByUserId";
    public static final String ITEM_GET_ITEMS_ID_BY_TAG_NAMES = "item.getItemsIdByTagNames";
    public static final String TAG_SEARCH_TAGS_NAME = "tag.searchTagsName";

    //Exceptions
    public static final String EXCEPTION_AUTHENTICATION = "authentication.exception";
    public static final String EXCEPTION_BAD_TOKEN = "bad.token.exception";
    public static final String EXCEPTION_DATABASE_WORK = "database.work.exception";
    public static final String EXCEPTION_EMAIL_USED = "email.used.exception";
    public static final String EXCEPTION_LOGIN_USED = "login.used.Exception";
    public static final String EXCEPTION_ENTITY_NOT_FOUND = "entity.not.found.exception";
    public static final String EXCEPTION_LOGIN_NOT_FOUND = "login.not.found.exception";
    public static final String EXCEPTION_EMAIL_NOT_FOUND = "email.not.found.exception";
    public static final String EXCEPTION_KEY_NOT_FOUND = "key.not.found.exception";
    public static final String EXCEPTION_FAILED_LOGIN = "failed.login.exception";
    public static final String EXCEPTION_JWT_AUTHENTICATION = "jwt.authentication.exception";
    public static final String EXCEPTION_FILE_UPLOAD = "file.upload.exception";
    public static final String EXCEPTION_HASH_ALGORITHM = "hash.algorithm.exception";
    public static final String EXCEPTION_NO_TOKEN = "no.token.exception";
    public static final String EXCEPTION_MAIL_SERVER = "mail.server.exception";

    private Key() {
    }

    public static Key getInstance() {
        if (instance == null) {
            instance = new Key();
        }
        return instance;
    }

}