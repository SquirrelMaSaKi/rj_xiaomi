package com.xiaomi.dao;

import com.xiaomi.domain.Goods;

import java.util.List;

public interface GoodsDao {
    long getCount(String condition);
    List<Goods> findPageByWhere(int pageNum, int pageSize, String condition);

    Goods findById(int gid);

    void add(Goods goods);

    List<Goods> findeByTypeId(int typeid);

    void update(Goods goods);

    void deleteByPid(int pid);
}
