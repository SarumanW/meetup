package com.meetup.meetup.keys;

public final class Key {

    private static Key instance = null;

    //TABLES
    public static final String TABLE_UUSER = "UUSER";
    public static final String TABLE_FRIEND = "FRIEND";
    public static final String TABLE_EVENT = "EVENT";
    public static final String TABLE_USER_EVENT = "USER_EVENT";
    public static final String TABLE_FOLDER= "FOLDER";
    public static final String TABLE_ITEM = "ITEM";
    public static final String TABLE_TAG = "TAG";
    public static final String TABLE_LLIKE = "LLIKE";
    public static final String TABLE_CHAT = "CHAT";
    public static final String TABLE_CHAT_TYPE = "CHAT_TYPE";
    public static final String TABLE_MESSAGE = "MESSAGE";
    public static final String TABLE_ITEM_COMMENT = "ITEM_COMMENT";

    //MESSAGE table fields
    public static final String MESSAGE_MESSAGE_ID = "MESSAGE_ID";
    public static final String MESSAGE_SENDER_ID = "SENDER_ID";
    public static final String MESSAGE_CHAT_ID = "CHAT_ID";
    public static final String MESSAGE_TEXT = "TEXT";
    public static final String MESSAGE_MESSAGE_DATE = "MESSAGE_DATE";
    public static final String MESSAGE_SENDER_LOGIN = "SENDER_LOGIN";

    //CHAT_TYPE table fields
    public static final String CHAT_CHAT_TYPE_ID = "CHAT_TYPE_ID";
    public static final String CHAT_TYPE = "TYPE";

    //CHAT table fields
    public static final String CHAT_CHAT_ID = "CHAT_ID";
    public static final String CHAT_TYPE_ID = "CHAT_TYPE_ID";
    public static final String CHAT_EVENT_ID = "EVENT_ID";


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
    public static final String LLIKE_ITEM_ID = "ITEM_ID";
    public static final String LLIKE_USER_ID = "USER_ID";

    //ITEM_COMMENT table field
    public static final String ITEM_COMMENT_COMMENT_ID = "COMMENT_ID";
    public static final String ITEM_COMMENT_BODY_TEXT = "BODY_TEXT";
    public static final String ITEM_COMMENT_POST_TIME = "POST_TIME";
    public static final String ITEM_COMMENT_AUTHOR_ID = "AUTHOR_ID";
    public static final String ITEM_COMMENT_ITEM_ID = "ITEM_ID";
    public static final String ITEM_COMMENT_AUTHOR_LOGIN = "AUTHOR_LOGIN";
    public static final String ITEM_COMMENT_AUTHOR_IMG = "AUTHOR_IMG";

    //TAG table fields
    public static final String TAG_TAG_ID = "TAG_ID";
    public static final String TAG_TAG_NAME = "NAME";

    //UUSER table fields
    public static final String UUSER_USER_ID ="USER_ID";
    public static final String UUSER_LOGIN ="login";
    public static final String UUSER_PASSWORD ="password";
    public static final String UUSER_NAME ="name";
    public static final String UUSER_SURNAME ="surname";
    public static final String UUSER_EMAIL ="email";
    public static final String UUSER_TIMEZONE ="timezone";
    public static final String UUSER_IMAGE_FILEPATH="image_filepath";
    public static final String UUSER_BDAY="bday";
    public static final String UUSER_PHONE="phone";
    public static final String UUSER_PINED_EVENT_ID="pined_event_id";
    public static final String UUSER_PERIODICAL_EMAIL="periodical_email";
    public static final String UUSER_REGISTER_DATE="register_date";

    //FRIEND table fields
    public static final String FRIEND_SENDER_ID ="SENDER_ID";
    public static final String FRIEND_RECEIVER_ID ="RECEIVER_ID";
    public static final String FRIEND_IS_CONFIRMED="IS_CONFIRMED";

    //EVENT table fields
    public static final String EVENT_EVENT_ID="EVENT_ID";
    public static final String EVENT_NAME="NAME";
    public static final String EVENT_EVENT_DATE="EVENT_DATE";
    public static final String EVENT_PERIODICITY_ID="PERIODICITY_ID";
    public static final String EVENT_DESCRIPTION="DESCRIPTION";
    public static final String EVENT_PLACE="PLACE";
    public static final String EVENT_EVENT_TYPE_ID="EVENT_TYPE_ID";
    public static final String EVENT_IS_DRAFT="IS_DRAFT";
    public static final String EVENT_FOLDER_ID="FOLDER_ID";
    public static final String EVENT_IMAGE_FILEPATH="IMAGE_FILEPATH";

    //USER_EVENT table fields
    public static final String USER_EVENT_USER_ID="USER_ID";
    public static final String USER_EVENT_EVENT_ID="EVENT_ID";
    public static final String USER_EVENT_ROLE_ID="ROLE_ID";

    //FOLDER table fields
    public static final String FOLDER_FOLDER_ID="FOLDER_ID";
    public static final String FOLDER_NAME="name";
    public static final String FOLDER_USER_ID="user_id";

    //UserDao
    public static final String USER_FIND_BY_LOGIN="user.findByLogin";
    public static final String USER_FIND_BY_ID="user.findById";
    public static final String USER_FIND_BY_EMAIL="user.findByEmail";
    public static final String USER_UPDATE="user.update";
    public static final String USER_UPDATE_PASSWORD="user.updatePassword";
    public static final String USER_DELETE="user.delete";
    public static final String USER_GET_ALL_BY_PART = "user.getAllByUsernamePart";
    public static final String USER_GET_FRIENDS_BY_USERNAME_PART = "user.getFriendsByUsernamePart";
    public static final String USER_GET_NOT_FRIENDS_BY_USERNAME_PART = "user.getNotFriendsByUsernamePart";
    public static final String USER_GET_FRIENDS = "user.getFriends";
    public static final String USER_GET_UNCONFIRMED = "user.getUnconfirmedFriends";
    public static final String USER_CONFIRM_FRIEND="user.confirmFriend";
    public static final String USER_DELETE_FRIEND="user.deleteFriend";
    public static final String USER_IS_LOGIN_FREE ="user.isLoginFree";
    public static final String USER_IS_EMAIL_FREE ="user.isEmailFree";
    public static final String USER_GET_BY_EMAIL_PERIOD="user.getByEmailPeriod";
    public static final String USER_SET_PINED_EVENT_ID="user.setPinedEventId";
    public static final String USER_DELETE_PINED_EVENT_ID="user.deletePinedEventId";
    public static final String USER_DELETE_UNCONFIRMED_ACCOUNTS="user.deleteUnconfirmedAccounts";
    public static final String USER_GET_LOGIN_BY_ID="user.getLoginById";

    //FolderDao
    public static final String FOLDER_GET_USER_FOLDERS="folder.getUserFolders";
    public static final String FOLDER_GET_BY_ID="folder.getById";
    public static final String FOLDER_GET_BY_NAME="folder.getByName";
    public static final String FOLDER_UPDATE="folder.update";
    public static final String FOLDER_DELETE="folder.delete";
    public static final String FOLDER_REMOVE_EVENTS="folder.removeEvents";

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
    public static final String EVENT_GET_IN_PERIOD_ALL_USERS="event.getInPeriodAllUsers";
    public static final String EVENT_GET_ALL_PUBLIC="event.getAllPublic";
    public static final String EVENT_ADD_PARTICIPANT="event.addParticipant";
    public static final String EVENT_DELETE_PARTICIPANT="event.deleteParticipant";
    public static final String EVENT_DELETE_PARTICIPANTS="event.deleteParticipants";
    public static final String EVENT_DELETE_MEMBERS="event.deleteMembers";
    public static final String EVENT_UNPIN_ALL_ON_DELETE="event.unpinAllOnDelete";
    //RoleDao
    public static final String GET_ROLE="role.getRole";
    public static final String ROLE_NAME = "name";

    //ItemDao
    public static final String ITEM_FIND_BY_ID = "item.findById";
    public static final String ITEM_GET_ITEMS_BY_USER_ID = "item.getItemsByUserId";
    public static final String ITEM_DELETE = "item.delete";
    public static final String ITEM_UPDATE = "item.update";
    public static final String ITEM_GET_TAG_BY_ITEM_ID = "item.getTagByItemId";
    public static final String ITEM_GET_POPULAR_ITEMS ="item.getPopularItems";
    public static final String ITEM_GET_PERSONAL_INFO_BY_ITEM_ID_USER_ID = "item.getPersonalInfoByUserIdItemId";
    public static final String ITEM_UPDATE_USER_ITEM = "item.updateUserItem";
    public static final String ITEM_DELETE_FROM_WISH_LIST = "item.deleteFromWishList";
    public static final String ITEM_ADD_BOOKER_ID_FOR_ITEM = "item.addBookerId";
    public static final String ITEM_DELETE_BOOKER_ID_FOR_ITEM = "item.deleteBookerId";
    public static final String ITEM_GET_NUMBER_OF_ITEM_USERS = "item.getNumberOfItemUsers";
    public static final String ITEM_GET_TAG_ID = "item.getTagIdByName";
    public static final String ITEM_ADD_TAG_TO_ITEM = "item.addTagToItem";
    public static final String ITEM_DELETE_TAGS = "item.deleteTags";
    public static final String ITEM_REMOVE_LIKE_BY_ITEM_ID_USER_ID = "item.removeLikeByItemIdUserId";
    public static final String ITEM_UPDATE_USER_ITEM_INFO = "item.updateUserItemInfo";
    public static final String ITEM_GET_BOOKED_ITEMS_BY_USER_ID = "item.getBookedItemsByUserId";
    public static final String TAG_SEARCH_TAGS_NAME = "tag.searchTagsName";
    public static final String ITEM_GET_LIKED_USER_LOGINS_BY_ITEM_ID = "item.getLikedUserLoginsById";
    public static final String ITEM_GET_LIKE_ID_BY_USER_ID_ITEM_ID = "item.getLikeIdByUserIdItemId";
    public static final String ITEM_GET_ITEMS_BY_TAG_NAMES = "item.getItemsByTagNames";

    //ItemCommentDao
    public static final String ITEM_COMMENT_FIND_BY_ID = "itemComment.findById";
    public static final String ITEM_COMMENT_FIND_COMMENTS_BY_ITEM_ID = "itemComment.findCommentsByItemId";
    public static final String ITEM_COMMENT_DELETE = "itemComment.delete";

    //ChatDao
    public static final String CHAT_DELETE_BY_EVENT_ID = "chat.deleteChatsByEventId";
    public static final String CHAT_DELETE_MESSAGES_BY_EVENT_ID = "chat.deleteMessagesByEventId";
    public static final String CHAT_FIND_CHAT_ID_BY_EVENT_ID_AND_CHAT_TYPE_ID = "chat.findChatIdByEventIdAndChatTypeId";
    public static final String CHAT_FIND_MESSAGES_BY_CHAT_ID = "chat.findMessagesByChatId";
    public static final String CHAT_CAN_JOIN_CHAT = "chat.canJoinChat";

    //Exceptions
    public static final String EXCEPTION_AUTHENTICATION = "authentication.exception";
    public static final String EXCEPTION_BAD_TOKEN = "bad.token.exception";
    public static final String EXCEPTION_DATABASE_WORK= "database.work.exception";
    public static final String EXCEPTION_EMAIL_USED= "email.used.exception";
    public static final String EXCEPTION_LOGIN_USED = "login.used.Exception";
    public static final String EXCEPTION_ENTITY_NOT_FOUND = "entity.not.found.exception";
    public static final String EXCEPTION_LOGIN_NOT_FOUND = "login.not.found.exception";
    public static final String EXCEPTION_EMAIL_NOT_FOUND = "email.not.found.exception";
    public static final String EXCEPTION_KEY_NOT_FOUND = "key.not.found.exception";
    public static final String EXCEPTION_FAILED_LOGIN = "failed.login.exception";
    public static final String EXCEPTION_JWT_AUTHENTICATION = "jwt.authentication.exception";
    public static final String EXCEPTION_JWT_IS_NOT_CORRECT = "jwt.bad.token.exception";
    public static final String EXCEPTION_FILE_UPLOAD = "file.upload.exception";
    public static final String EXCEPTION_HASH_ALGORITHM = "hash.algorithm.exception";
    public static final String EXCEPTION_NO_TOKEN = "no.token.exception";
    public static final String EXCEPTION_PARSE_DATE = "parse.date.exception";
    public static final String EXCEPTION_DELETE = "delete.exception";
    public static final String EXCEPTION_UPDATE = "update.exception";
    public static final String EXCEPTION_ITEM_IS_IN_WISHLIST= "item.is.in.wishlist.exception";
    public static final String EXCEPTION_REQUEST_ALREADY_SENT= "request.already.sent.exception";

    //Send mail periods
    public static final String MAIL_MONTHLY="MONTHLY";
    public static final String MAIL_EVERY_SUNDAY="SUNDAY";
    public static final String MAIL_EVERY_MONDAY="MONDAY";
    public static final String MAIL_EVERY_TUESDAY="TUESDAY";
    public static final String MAIL_EVERY_WEDNESDAY="WEDNESDAY";
    public static final String MAIL_EVERY_THURSDATY="THURSDAY";
    public static final String MAIL_EVERY_FRIDAY="FRIDAY";
    public static final String MAIL_EVERY_SATURDAY="SATURDAY";
    public static final String MAIL_DAILY="DAILY";

    //Security url patterns
    public static final String URL_AUTH_PATTERN="auth.all";
    public static final String URL_API_PATTERN="api.all";

    private Key(){}


}
