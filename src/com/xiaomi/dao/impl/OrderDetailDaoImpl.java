package com.xiaomi.dao.impl;

import com.xiaomi.dao.OrderDetailDao;
import com.xiaomi.domain.OrderDetail;
import com.xiaomi.utils.DruidUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.SQLException;
import java.util.List;

public class OrderDetailDaoImpl implements OrderDetailDao {
    @Override
    public void add(OrderDetail orderDetail) {
        QueryRunner qr = new QueryRunner();
        Object[] params = {orderDetail.getOid(), orderDetail.getPid(),orderDetail.getMoney(),orderDetail.getNum()};
        try {
            qr.update(DruidUtils.getConnection(), "insert into tb_orderdetail(oid,pid,num,money) values(?,?,?,?)", params);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("添加订单详情失败", e);
        }
    }

    @Override
    public List<OrderDetail> findOrderDetails(String oid) {
        QueryRunner qr = new QueryRunner(DruidUtils.getDataSource());
        try {
            return qr.query("select * from tb_orderdetail where oid=?;", new BeanListHandler<>(OrderDetail.class),oid);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("根据订单号查询订单详情失败", e);
        }
    }

    @Override
    public List<OrderDetail> findByPid(int pid) {
        QueryRunner qr = new QueryRunner(DruidUtils.getDataSource());
        try {
            return qr.query("select * from tb_orderdetail where pid=?",new BeanListHandler<>(OrderDetail.class) ,pid);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("查询失败", e);
        }
    }
}
