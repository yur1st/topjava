package ru.javawebinar.topjava.dao;

import java.util.List;

public interface CRUDInterface<T> {
    void add(T t);
    void delete(int id);
    void update(int id, T t);
    List<T> getAll();
    T getById(int id);
}
