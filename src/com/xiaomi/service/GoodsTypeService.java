package com.xiaomi.service;

import com.xiaomi.domain.GoodsType;

import java.util.List;

public interface GoodsTypeService {
    List<GoodsType> findTypeByLevel(int i);

    List<GoodsType> findTypeAll(int pn, int ps, String condition);

    long getCount(String condition);

    GoodsType findTypeById(int id);

    void modifyById(GoodsType goodsType);

    List<GoodsType> findGoodsTypes();

    void deleteByID(int id);

    void add(GoodsType goodsType);
}
