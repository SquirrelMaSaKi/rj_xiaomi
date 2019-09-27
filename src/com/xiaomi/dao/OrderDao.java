package com.xiaomi.dao;

import com.xiaomi.domain.Order;

import java.util.List;

public interface OrderDao {
    void add(Order order);

    void updateStatus(String r6_order, String s);

    List<Order> orderFindById(int uid);

    Order findByOid(String oid);

    long getCount(String condition);

    List<Order> orderByPage(int pageNum, int pageSize, String condition);
}
