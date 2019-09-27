package com.xiaomi.service.impl;

import com.xiaomi.dao.CartDao;
import com.xiaomi.dao.impl.CartDaoImpl;
import com.xiaomi.domain.Cart;
import com.xiaomi.service.CartService;
import com.xiaomi.service.GoodsService;

import java.util.List;

public class CartServiceImpl implements CartService {
    CartDao cartDao = new CartDaoImpl();

    @Override
    public Cart findByUidAndPid(int uid, int pid) {
        return cartDao.findByUidAndPid(uid, pid);
    }

    @Override
    public void add(Cart cart) {
        cartDao.add(cart);
    }

    @Override
    public List<Cart> findByUid(int id) {
        List<Cart> carts = cartDao.findByUid(id);
        GoodsService goodsService = new GoodsServiceIMpl();
        //在将数据传递到前端之前必须将Goods属性赋值
        for (Cart cart : carts) {
            cart.setGoods(goodsService.findByID(cart.getPid()));
        }
        return carts;
    }

    @Override
    public void update(Cart cart) {
        cartDao.update(cart);
    }

    @Override
    public void delete(int id, int pid) {
        cartDao.delete(id, pid);
    }

    @Override
    public void deleteByUid(int id) {
        cartDao.deleteByUid(id);
    }

    @Override
    public List<Cart> findByPid(int pid) {
        return cartDao.findByPid(pid);
    }
}
