package com.meetup.meetup.Keys;

public final class Key {

    private static Key instance = null;

    //UserDao
    public static final String USER_FIND_BY_LOGIN="SELECT USER_ID,login, password, name, surname, email, "+
            "timezone, image_filepath, bday, phone "+
            "FROM UUSER WHERE LOGIN = ?";

    public static final String USER_FIND_BY_ID="SELECT USER_ID,login, password, name, surname, email, "+
            "timezone, image_filepath, bday, phone "+
            "FROM UUSER WHERE USER_ID = ?";

    public static final String USER_FIND_BY_EMAIL="SELECT USER_ID,login, password, name, surname, email, "+
            "timezone, image_filepath, bday, phone "+
            "FROM UUSER WHERE EMAIL = ?";

    public static final String USER_UPDATE="UPDATE UUSER SET LOGIN = ?, NAME = ?, SURNAME = ?, EMAIL = ?, "+
            "TIMEZONE = ?, IMAGE_FILEPATH = ?, BDAY = ?, PHONE = ? WHERE USER_ID = ?";

    public static final String USER_UPDATE_PASSWORD="UPDATE UUSER SET PASSWORD = ? WHERE USER_ID = ?";

    public static final String USER_DELETE="DELETE FROM UUSER WHERE USER_ID = ?";

    public static final String USER_GET_FRIENDS_IDS="SELECT SENDER_ID, RECEIVER_ID "+
            "FROM FRIEND WHERE IS_CONFIRMED=1 and (SENDER_ID=? or RECEIVER_ID=?)";

    public static final String USER_GET_UNCONFIRMED_IDS="SELECT SENDER_ID "+
            "FROM FRIEND WHERE RECEIVER_ID=? and IS_CONFIRMED=0";

    public static final String USER_CONFIRM_FRIEND="UPDATE FRIEND SET IS_CONFIRMED=1 "+
            "where SENDER_ID=? AND RECEIVER_ID=?";

    public static final String USER_DELETE_FRIEND="DELETE FROM FRIEND "+
            "WHERE ((SENDER_ID=? and RECEIVER_ID=?) OR (SENDER_ID=? and RECEIVER_ID=?))";

//FolderDao

    public static final String GET_USER_FOLDERS="SELECT FOLDER_ID, NAME, USER_ID "+
            "FROM FOLDER WHERE USER_ID = ?";

    public static final String FOLDER_GET_BY_ID="SELECT FOLDER_ID, NAME, USER_ID "+
            "FROM FOLDER WHERE FOLDER_ID = ? AND USER_ID = ?";

    public static final String FOLDER_GET_BY_NAME="SELECT FOLDER_ID, NAME, USER_ID "+
            "FROM FOLDER WHERE NAME= ?";

    public static final String FOLDER_UPDATE="UPDATE FOLDER SET NAME = ? WHERE FOLDER_ID = ?";

    public static final String FOLDER_DELETE="DELETE FROM FOLDER WHERE FOLDER_ID = ?";

    public static final String FOLDER_REMOVE_EVENTS="UPDATE EVENT E SET FOLDER_ID = "+
            "(SELECT FOLDER_ID FROM FOLDER WHERE NAME = 'general' AND USER_ID = "+
            "(SELECT USER_ID FROM USER_EVENT WHERE EVENT_ID = E.EVENT_ID)) "+
            "WHERE FOLDER_ID = ?";



    //EventDao
    public static final String EVENT_FIND_BY_USER_ID="SELECT E.EVENT_ID EVENT_ID , E.NAME NAME, EVENT_DATE, DESCRIPTION, "+
            "E.PERIODICITY_ID PERIODICITY_ID, P.PERIODICITY_NAME PERIODICITY_NAME, PLACE, "+
            "E.EVENT_TYPE_ID EVENT_TYPE_ID, TYPE, IS_DRAFT, FOLDER_ID, IMAGE_FILEPATH, UE.USER_ID OWNER_ID "+
            "FROM EVENT E "+
            "INNER JOIN PERIODICITY P ON E.PERIODICITY_ID = P.PERIODICITY_ID "+
            "INNER JOIN EVENT_TYPE ET ON E.EVENT_TYPE_ID = ET.EVENT_TYPE_ID "+
            "INNER JOIN USER_EVENT UE ON E.EVENT_ID = UE.EVENT_ID "+
            "INNER JOIN RROLE R ON UE.ROLE_ID = R.ROLE_ID "+
            "WHERE UE.USER_ID = ? AND E.EVENT_TYPE_ID<>2";

    public static final String EVENT_FIND_BY_ID="SELECT E.EVENT_ID EVENT_ID , E.NAME NAME, EVENT_DATE, DESCRIPTION, "+
            "E.PERIODICITY_ID PERIODICITY_ID, P.PERIODICITY_NAME PERIODICITY_NAME, PLACE, "+
            "E.EVENT_TYPE_ID EVENT_TYPE_ID, TYPE, IS_DRAFT, FOLDER_ID, IMAGE_FILEPATH, UE.USER_ID OWNER_ID "+
            "FROM EVENT E "+
            "INNER JOIN PERIODICITY P ON E.PERIODICITY_ID = P.PERIODICITY_ID "+
            "INNER JOIN EVENT_TYPE ET ON E.EVENT_TYPE_ID = ET.EVENT_TYPE_ID "+
            "INNER JOIN USER_EVENT UE ON E.EVENT_ID = UE.EVENT_ID "+
            "INNER JOIN RROLE R ON UE.ROLE_ID = R.ROLE_ID "+
            "WHERE E.EVENT_ID = ? AND R.NAME = 'OWNER'";

    public static final String EVENT_UPDATE="UPDATE EVENT "+
            "SET NAME = ?, EVENT_DATE = TO_TIMESTAMP( ?, 'YYYY-MM-DD HH24:MI:SS.FF'), DESCRIPTION = ?, "+
            "PERIODICITY_ID = ?, PLACE = ?, EVENT_TYPE_ID = ?, IS_DRAFT = ?, FOLDER_ID = ?, IMAGE_FILEPATH = ? "+
            "WHERE EVENT_ID = ?";

    public static final String EVENT_DELETE="DELETE FROM EVENT WHERE EVENT_ID = ?";

    public static final String EVENT_FIND__BY_FOLDER_ID="SELECT EVENT_ID , NAME, EVENT_DATE, DESCRIPTION, E.PERIODICITY_ID "+
            "PERIODICITY_ID, P.PERIODICITY_NAME PERIODICITY_NAME, PLACE, "+
            "E.EVENT_TYPE_ID EVENT_TYPE_ID, TYPE, IS_DRAFT, FOLDER_ID, IMAGE_FILEPATH, 0 OWNER_ID "+
            "FROM EVENT E "+
            "INNER JOIN PERIODICITY P ON E.PERIODICITY_ID = P.PERIODICITY_ID "+
            "INNER JOIN EVENT_TYPE ET ON E.EVENT_TYPE_ID = ET.EVENT_TYPE_ID "+
            "WHERE FOLDER_ID = ?";

    public static final String EVENT_GET_PARTICIPANTS="SELECT us.user_id user_id,login, password, "+
            "us.name name, surname, email, timezone, image_filepath, bday, phone "+
            "FROM uuser us "+
            "INNER JOIN user_event ue ON us.user_id = ue.user_id "+
            "INNER JOIN rrole r ON ue.role_id = r.role_id "+
            "WHERE ue.event_id = ? AND r.name = 'PARTICIPANT'";


    public static final String EVENT_FIND_BY_TYPE_IN_FOLDER="SELECT E.EVENT_ID EVENT_ID , E.NAME NAME, EVENT_DATE, DESCRIPTION, "+
            "E.PERIODICITY_ID PERIODICITY_ID, P.PERIODICITY_NAME PERIODICITY_NAME, PLACE, "+
            "E.EVENT_TYPE_ID EVENT_TYPE_ID, TYPE, IS_DRAFT, FOLDER_ID, IMAGE_FILEPATH, UE.USER_ID OWNER_ID "+
            "FROM EVENT E "+
            "INNER JOIN PERIODICITY P ON E.PERIODICITY_ID = P.PERIODICITY_ID "+
            "INNER JOIN EVENT_TYPE ET ON E.EVENT_TYPE_ID = ET.EVENT_TYPE_ID "+
            "INNER JOIN USER_EVENT UE ON E.EVENT_ID = UE.EVENT_ID "+
            "WHERE ET.TYPE = ? and E.FOLDER_ID = ? and IS_DRAFT = 0";

    public static final String EVENT_GET_DRAFTS="SELECT E.EVENT_ID EVENT_ID , E.NAME NAME, EVENT_DATE, DESCRIPTION, "+
            "E.PERIODICITY_ID PERIODICITY_ID, P.PERIODICITY_NAME PERIODICITY_NAME, PLACE, "+
            "E.EVENT_TYPE_ID EVENT_TYPE_ID, TYPE, IS_DRAFT, FOLDER_ID, IMAGE_FILEPATH, UE.USER_ID OWNER_ID "+
            "FROM EVENT E "+
            "INNER JOIN PERIODICITY P ON E.PERIODICITY_ID = P.PERIODICITY_ID "+
            "INNER JOIN EVENT_TYPE ET ON E.EVENT_TYPE_ID = ET.EVENT_TYPE_ID "+
            "INNER JOIN USER_EVENT UE ON E.EVENT_ID = UE.EVENT_ID "+
            "WHERE IS_DRAFT = 1 AND E.FOLDER_ID = ? ";

    //RoleDao
    public static final String GET_ROLE="SELECT r.name name FROM user_event ue "+
            "INNER JOIN rrole r ON ue.role_id = r.role_id WHERE ue.user_id = ? AND ue.event_id = ?";


    private Key(){}


    public static Key getInstance() {
        if (instance == null) {
            instance = new Key();
        }
        return instance;
    }

}
