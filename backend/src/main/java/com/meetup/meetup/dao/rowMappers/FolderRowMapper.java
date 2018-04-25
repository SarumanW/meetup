package com.meetup.meetup.dao.rowMappers;

import com.meetup.meetup.entity.Folder;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FolderRowMapper implements RowMapper<Folder> {
    @Override
    public Folder mapRow(ResultSet resultSet, int i) throws SQLException {
        Folder folder = new Folder();

        folder.setFolderId(resultSet.getInt("folder_id"));
        folder.setUserId(resultSet.getInt("user_id"));
        folder.setName(resultSet.getString("name"));

        return folder;
    }
}
