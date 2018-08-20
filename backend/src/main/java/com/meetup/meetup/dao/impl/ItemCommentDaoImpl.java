package com.meetup.meetup.dao.impl;

import com.meetup.meetup.dao.AbstractDao;
import com.meetup.meetup.dao.ItemCommentDao;
import com.meetup.meetup.dao.rowMappers.ItemCommentRowMapper;
import com.meetup.meetup.entity.ItemComment;
import com.meetup.meetup.exception.runtime.DatabaseWorkException;
import com.meetup.meetup.exception.runtime.DeleteException;
import com.meetup.meetup.exception.runtime.EntityNotFoundException;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.meetup.meetup.keys.Key.*;

@Repository
@PropertySource("classpath:sqlDao.properties")
@PropertySource("classpath:strings.properties")
@PropertySource("classpath:image.properties")
public class ItemCommentDaoImpl extends AbstractDao<ItemComment> implements ItemCommentDao {

    public ItemCommentDaoImpl() {
        log = LoggerFactory.getLogger(ItemCommentDaoImpl.class);
    }

    @Override
    public ItemComment findById(int id) {
        log.debug("Try find comment by comment id: '{}'", id);
        ItemComment itemComment;
        try {
            itemComment = jdbcTemplate.queryForObject(env.getProperty(ITEM_COMMENT_FIND_BY_ID), new Object[]{id}, new ItemCommentRowMapper());
        } catch (EmptyResultDataAccessException e) {
            log.error("Folder was not found by  folderId '{}'", id, e);
            throw new EntityNotFoundException(String.format(env.getProperty(EXCEPTION_ENTITY_NOT_FOUND), "ItemComment", "id", id));
        } catch (DataAccessException e) {
            log.error("Query fails by find comment by comment id: '{}'", id);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }
        return itemComment;
    }

    @Override
    public ItemComment insert(ItemComment model) {
        log.debug("Try insert item '{}'", model);

        SimpleJdbcInsert insertItem = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName(TABLE_ITEM_COMMENT)
                .usingGeneratedKeyColumns(ITEM_COMMENT_COMMENT_ID);

        log.debug("Try to get current time");
        model.setPostTime(new Timestamp(System.currentTimeMillis()));

        Map<String, Object> itemCommentParameters = new HashMap<>();
        itemCommentParameters.put(ITEM_COMMENT_BODY_TEXT, model.getBodyText());
        itemCommentParameters.put(ITEM_COMMENT_POST_TIME, model.getPostTime());
        itemCommentParameters.put(ITEM_COMMENT_AUTHOR_ID, model.getAuthorId());
        itemCommentParameters.put(ITEM_COMMENT_ITEM_ID, model.getItemId());
        try {
            model.setCommentId(insertItem.executeAndReturnKey(itemCommentParameters).intValue());
        } catch (DataAccessException e) {
            log.error("Query fails by insert item !!!'{}'", model);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }

        return model;
    }

    @Override
    public ItemComment update(ItemComment model) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ItemComment delete(ItemComment model) {
        log.debug("Try to delete comment with id '{}'", model.getCommentId());
        int result;
        try {
            result = jdbcTemplate.update(env.getProperty(ITEM_COMMENT_DELETE), model.getCommentId());
        } catch (DataAccessException e) {
            log.error("Query fails by deleting comment with id '{}'", model.getCommentId());
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }

        if (result == 0) {
            throw new DeleteException(EXCEPTION_DELETE);
        }

        log.debug("Deleting comment with id '{}' was successful", model.getCommentId());

        return model;
    }


    @Override
    public List<ItemComment> getCommentsForItemId(int itemId) {
        log.debug("Try get list comments by item id: '{}'", itemId);
        List<ItemComment> itemComment;
        try {
            itemComment = jdbcTemplate.query(env.getProperty(ITEM_COMMENT_FIND_COMMENTS_BY_ITEM_ID),
                    new Object[]{itemId} , new ItemCommentRowMapper());
        } catch (DataAccessException e) {
            log.error("Query fails by find comments by item id: '{}'", itemId);
            throw new DatabaseWorkException(env.getProperty(EXCEPTION_DATABASE_WORK));
        }
        return itemComment;
    }
}
