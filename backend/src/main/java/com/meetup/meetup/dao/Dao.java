package com.meetup.meetup.dao;

/**
 * Generic Dao interface for all entities.
 *
 * @param <T> generic parameter for entity
 */
public interface Dao<T> {

    T findById(int id);

    int insert(T model);

    int update(T model);

    int delete(T model);
}
