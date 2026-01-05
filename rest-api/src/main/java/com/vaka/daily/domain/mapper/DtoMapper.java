package com.vaka.daily.domain.mapper;

public interface DtoMapper<T, U> {
    T fromDto(U dto);

    U toDto(T entity);
}
