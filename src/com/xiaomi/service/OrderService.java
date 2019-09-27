package com.xiaomi.service;

import com.xiaomi.domain.Order;
import com.xiaomi.domain.OrderDetail;
import com.xiaomi.domain.PageBean;

import java.util.List;

public interface OrderService {
    void saveOrder(Order order, List<OrderDetail> orderDetails);

    void updateStatus(String r6_order, String s);

    List<Order> orderQuery(int uid);

    Order orderDetailQuery(String oid);

    PageBean<Order> orderByPage(int pageNum, int pageSize, String condition);
}
