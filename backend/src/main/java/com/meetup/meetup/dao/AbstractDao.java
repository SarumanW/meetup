package com.meetup.meetup.dao;

import com.meetup.meetup.dao.impl.EventDaoImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Generic Dao abstract class for all entities.
 *
 * @param <T> generic parameter for entity
 */

public abstract class AbstractDao<T> implements Dao<T> {

    @Autowired
    protected Environment env;

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    protected static Logger log;

}
