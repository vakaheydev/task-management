package com.vaka.daily.service.abstraction;

import java.util.List;

public interface CommonService<T> {
    List<T> getAll();

    T getById(Integer id);

    T create(T entity);

    T updateById(Integer id, T entity);

    void deleteById(Integer id);
}
