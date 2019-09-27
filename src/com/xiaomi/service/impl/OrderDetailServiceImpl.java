package com.xiaomi.service.impl;

import com.xiaomi.dao.OrderDetailDao;
import com.xiaomi.dao.impl.OrderDetailDaoImpl;
import com.xiaomi.domain.OrderDetail;
import com.xiaomi.service.OrderDetailService;

import java.util.List;

public class OrderDetailServiceImpl implements OrderDetailService {
    @Override
    public List<OrderDetail> findByPid(int pid) {
        OrderDetailDao orderDetailDao = new OrderDetailDaoImpl();
        return orderDetailDao.findByPid(pid);
    }
}
