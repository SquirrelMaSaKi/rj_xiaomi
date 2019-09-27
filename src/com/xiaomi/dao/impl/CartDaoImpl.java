package com.xiaomi.dao.impl;

import com.xiaomi.dao.CartDao;
import com.xiaomi.domain.Cart;
import com.xiaomi.utils.DruidUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.SQLException;
import java.util.List;

public class CartDaoImpl implements CartDao {
    QueryRunner qr = new QueryRunner(DruidUtils.getDataSource());
    @Override
    public Cart findByUidAndPid(int uid, int pid) {
        try {
            return qr.query("select * from tb_cart where id=? and pid=?", new BeanHandler<>(Cart.class),uid,pid);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("查询失败", e);
        }
    }

    @Override
    public void add(Cart cart) {
        Object[] params = {cart.getId(), cart.getPid(), cart.getNum(),cart.getMoney()};
        try {
            qr.update("insert into tb_cart(id,pid,num,money)values(?,?,?,?)",params);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("添加失败", e);
        }
    }

    @Override
    public void update(Cart cart) {
        Object[] params = { cart.getNum(),cart.getMoney(), cart.getId(), cart.getPid()};
        try {
            //注意使用and
            qr.update("update tb_cart set num=?, money=? where id=? and pid=?",params);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("更新失败", e);
        }
    }

    @Override
    public List<Cart> findByUid(int id) {
        try {
            return qr.query("select * from tb_cart where id=?", new BeanListHandler<>(Cart.class),id);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("根据id查找cart失败", e);
        }
    }

    @Override
    public void delete(int id, int pid) {
        try {
            qr.update("delete from tb_cart where id = ? and pid = ?;",id, pid);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("删除失败", e);
        }
    }

    @Override
    public void deleteByUid(int id) {
        try {
            qr.update("delete from tb_cart where id = ?;",id);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("删除失败", e);
        }
    }

    @Override
    public List<Cart> findByPid(int pid) {
        try {
            return qr.query("select * from tb_cart where pid=?", new BeanListHandler<>(Cart.class),pid);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("查询失败", e);
        }
    }
}
