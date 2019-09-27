package com.xiaomi.service.impl;

import com.xiaomi.dao.*;
import com.xiaomi.dao.impl.*;
import com.xiaomi.domain.*;
import com.xiaomi.service.OrderService;
import com.xiaomi.utils.DruidUtils;

import java.util.List;

public class OrderServiceImpl implements OrderService {

    @Override
    public void saveOrder(Order order, List<OrderDetail> orderDetails) {
        //开启事务
        try {
            //1.开启事务
            DruidUtils.startTransaction();
            //2.调用OrderDao向order表添加订单
            OrderDao orderDao = new OrderDaoImpl();
            orderDao.add(order);
            //3.向订单详情表添加
            OrderDetailDao orderDetailDao = new OrderDetailDaoImpl();
            for (OrderDetail orderDetail : orderDetails) {
                orderDetailDao.add(orderDetail);
            }
            //4.清空购物车
            CartDao cartDao = new CartDaoImpl();
            cartDao.deleteByUid(order.getUid());
            //5.提交
            DruidUtils.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                //回滚
                DruidUtils.rollback();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            throw new RuntimeException(e.getMessage(), e);
        }finally {
            //关闭
            DruidUtils.close();
        }
    }

    @Override
    public void updateStatus(String r6_order, String s) {
        OrderDao orderDao = new OrderDaoImpl();
        orderDao.updateStatus(r6_order, s);
    }

    @Override
    public List<Order> orderQuery(int uid) {
        OrderDao orderDao = new OrderDaoImpl();
        AddressDao addressDao = new AddressDaoImpl();
        List<Order> orderList = orderDao.orderFindById(uid);
        if (orderList != null && orderList.size() > 0) {
            for (Order order : orderList) {
                Address address = addressDao.findById(order.getAid());
                order.setAddress(address);
            }
        }
        return orderList;
    }

    @Override
    public Order orderDetailQuery(String oid) {
        OrderDao orderDao = new OrderDaoImpl();
        Order order = orderDao.findByOid(oid);
        GoodsDao goodsDao = new GoodsDaoImpl();

        //根据order查询地址
        AddressDao addressDao = new AddressDaoImpl();
        Address address = addressDao.findById(order.getAid());
        order.setAddress(address);

        //根据order查询订单详情
        OrderDetailDao orderDetailDao = new OrderDetailDaoImpl();
        List<OrderDetail> orderDetailList = orderDetailDao.findOrderDetails(oid);

        //查询【订单详情】中所关联的商品信息
        if (orderDetailList != null && orderDetailList.size() > 0) {
            for (OrderDetail detail : orderDetailList) {
                Goods goods = goodsDao.findById(detail.getPid());
                detail.setGoods(goods);
            }
        }
        order.setOrderDetails(orderDetailList);

        return order;
    }

    @Override
    public PageBean<Order> orderByPage(int pageNum, int pageSize, String condition) {
        OrderDao orderDao = new OrderDaoImpl();
        long totalSize = orderDao.getCount(condition);
        List<Order> data = orderDao.orderByPage(pageNum, pageSize, condition);

        return new PageBean<>(pageNum, pageSize, totalSize, data);

    }
}
