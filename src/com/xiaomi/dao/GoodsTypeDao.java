package com.xiaomi.dao;

import com.xiaomi.domain.GoodsType;

import java.sql.SQLException;
import java.util.List;

public interface GoodsTypeDao {

    List<GoodsType> findTypeByLevel(int i);

    GoodsType findById(int typeid);

    List<GoodsType> findTypeAll(int pn, int ps, String condition);

    long getCount(String condition);

    void modifyById(GoodsType goodsType);

    List<GoodsType> findGoodsTypes() throws SQLException;

    void deleteByID(int id);

    void add(GoodsType goodsType);
}
