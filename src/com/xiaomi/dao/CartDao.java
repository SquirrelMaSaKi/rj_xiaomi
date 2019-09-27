package com.xiaomi.dao;

import com.xiaomi.domain.Cart;

import java.util.List;

public interface CartDao {
    Cart findByUidAndPid(int uid, int pid);

    void add(Cart cart);

    void update(Cart cart);

    List<Cart> findByUid(int id);

    void delete(int id, int pid);

    void deleteByUid(int id);

    List<Cart> findByPid(int pid);
}
