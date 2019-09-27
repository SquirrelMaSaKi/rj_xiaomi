package com.xiaomi.service;

import com.xiaomi.domain.Goods;
import com.xiaomi.domain.PageBean;

import java.util.List;

public interface GoodsService {
    PageBean<Goods> findPageBywhere(int pageNum, int pageSize, String condition);

    Goods findByID(int gid);

    void add(Goods goods);

    List<Goods> findByTypeId(int typeid);

    void modify(Goods goods);

    void deleteByPid(int pid);
}
