package com.meetup.meetup.dao;

/**
 * Generic Dao interface for all entities.
 *
 * @param <T> generic parameter for entity
 */
public interface Dao<T> {

    T findById(int id);

    void save(T model);

    void update(T model);

    void delete(T model);
}
