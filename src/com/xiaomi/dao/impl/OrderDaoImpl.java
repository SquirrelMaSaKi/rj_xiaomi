package com.xiaomi.dao.impl;

import com.xiaomi.dao.OrderDao;
import com.xiaomi.domain.Order;
import com.xiaomi.utils.DruidUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;
import java.util.List;

public class OrderDaoImpl implements OrderDao {
    @Override
    public void add(Order order) {
        QueryRunner queryRunner = new QueryRunner();
        Object[] params = {order.getId(), order.getUid(),order.getMoney(),order.getStatus(),order.getTime(),order.getAid()};
        try {
            queryRunner.update(DruidUtils.getConnection(), "insert into tb_order(id,uid,money,status,time,aid)values(?,?,?,?,?,?)",params);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("添加订单失败", e);
        }
    }

    @Override
    public void updateStatus(String r6_order, String s) {
        QueryRunner queryRunner = new QueryRunner(DruidUtils.getDataSource());
        try {
            queryRunner.update("update tb_order set status = ? where id = ?;",s,r6_order);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("添加订单失败", e);
        }
    }

    @Override
    public List<Order> orderFindById(int uid) {
        QueryRunner qr = new QueryRunner(DruidUtils.getDataSource());
        try {
            return qr.query("select * from tb_order where uid = ?", new BeanListHandler<>(Order.class),uid);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("查询失败", e);
        }
    }

    @Override
    public Order findByOid(String oid) {
        QueryRunner qr = new QueryRunner(DruidUtils.getDataSource());
        try {
            return qr.query("select * from tb_order where id = ?", new BeanHandler<>(Order.class),oid);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("根据订单号查询失败", e);
        }
    }

    @Override
    public long getCount(String condition) {
        QueryRunner qr = new QueryRunner(DruidUtils.getDataSource());
        String sql = "select count(*) from tb_order as o inner join tb_user as u on o.`uid`=u.`id` ";
        try {
            return qr.query(sql+condition, new ScalarHandler<>());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("查询订单个数失败", e);
        }
    }

    @Override
    public List<Order> orderByPage(int pageNum, int pageSize, String condition) {
        QueryRunner qr = new QueryRunner(DruidUtils.getDataSource());
        String sql = "select o.*,u.username from tb_order as o inner join tb_user as u on o.`uid`=u.`id` ";
        sql = sql +  condition  + " limit ?, ? ";
        try {
            return qr.query(sql, new BeanListHandler<>(Order.class),(pageNum - 1)*pageSize, pageSize);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("查询数据失败", e);
        }
    }
}
