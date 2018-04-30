package com.meetup.meetup.Keys;

public final class Key {

    private static Key instance = null;

    //UserDao
    public static final String USER_FIND_BY_LOGIN="user.findByLogin";

    public static final String USER_FIND_BY_ID="user.findById";

    public static final String USER_FIND_BY_EMAIL="user.findByEmail";

    public static final String USER_UPDATE="user.update";

    public static final String USER_UPDATE_PASSWORD="user.updatePassword";

    public static final String USER_DELETE="user.delete";

    public static final String USER_GET_FRIENDS_IDS="user.getFriendsIds";

    public static final String USER_GET_UNCONFIRMED_IDS="user.getUnconfirmedIds";

    public static final String USER_CONFIRM_FRIEND="user.confirmFriend";

    public static final String USER_DELETE_FRIEND="user.deleteFriend";

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

    //RoleDao
    public static final String GET_ROLE="role.getRole";


    private Key(){}


    public static Key getInstance() {
        if (instance == null) {
            instance = new Key();
        }
        return instance;
    }

}
