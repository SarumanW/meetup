package com.meetup.meetup.dao.rowMappers;

import com.meetup.meetup.entity.Folder;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.meetup.meetup.keys.Key.*;

public class FolderRowMapper implements RowMapper<Folder> {
    @Override
    public Folder mapRow(ResultSet resultSet, int i) throws SQLException {
        Folder folder = new Folder();

        folder.setFolderId(resultSet.getInt(FOLDER_FOLDER_ID));
        folder.setUserId(resultSet.getInt(FOLDER_USER_ID));
        folder.setName(resultSet.getString(FOLDER_NAME));

        return folder;
    }
}
