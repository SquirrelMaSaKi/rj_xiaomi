package com.xiaomi.service;

import com.xiaomi.domain.Cart;

import java.util.List;

public interface CartService {
    Cart findByUidAndPid(int uid, int pid);

    void add(Cart cart);

    List<Cart> findByUid(int id);

    void update(Cart cart);

    void delete(int id, int pid);

    void deleteByUid(int id);

    List<Cart> findByPid(int pid);
}
