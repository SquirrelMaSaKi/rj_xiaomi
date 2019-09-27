package com.xiaomi.dao;

import com.xiaomi.domain.OrderDetail;

import java.util.List;

public interface OrderDetailDao {
    void add(OrderDetail orderDetail);

    List<OrderDetail> findOrderDetails(String oid);

    List<OrderDetail> findByPid(int pid);
}
