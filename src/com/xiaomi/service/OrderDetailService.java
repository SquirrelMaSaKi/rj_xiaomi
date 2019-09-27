package com.xiaomi.service;

import com.xiaomi.domain.OrderDetail;

import java.util.List;

public interface OrderDetailService {
    List<OrderDetail> findByPid(int pid);
}
