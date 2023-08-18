package com.mate.kosmo.service;

import java.util.List;
import java.util.Optional;

public interface MateService<T> {

    List<T> selectList(List nums);

    Optional<T> selectOne(T t);

    int insert(T t);

    int delete(T T);

    int update(T T);

}// MateService