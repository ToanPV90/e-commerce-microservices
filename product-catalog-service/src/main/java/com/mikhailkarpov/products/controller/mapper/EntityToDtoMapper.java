package com.mikhailkarpov.products.controller.mapper;

public interface EntityToDtoMapper<T, V> {

    V map(T t);

}
