package com.xiaomi.dao.impl;

import com.xiaomi.dao.AddressDao;
import com.xiaomi.domain.Address;
import com.xiaomi.utils.DruidUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.SQLException;
import java.util.List;

public class AddressDaoImpl implements AddressDao {
    QueryRunner qr = new QueryRunner(DruidUtils.getDataSource());
    @Override
    public List<Address> findByUid(int uid) {
        try {
            return qr.query("select * from tb_address where uid = ?", new BeanListHandler<>(Address.class),uid);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("查询失败", e);
        }
    }

    @Override
    public void add(Address address) {
        Object[] params = {address.getDetail(), address.getName(), address.getPhone(), address.getUid(),  address.getLevel()};
        try {
            qr.update("insert into tb_address(detail,name,phone,uid,level) values(?,?,?,?,?);", params);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("添加失败", e);
        }
    }

    @Override
    public void updateDefault(int aid, int uid) {
        try {
            //将所有的地址改为0
            qr.update("update tb_address set level = 0 where uid = ?", uid);
            //将指定的地址改为1，这样就是默认
            qr.update("update tb_address set level = 1 where id = ?", aid);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("修改失败", e);
        }
    }

    @Override
    public void delete(int id) {
        try {
            qr.update("delete from tb_address where id = ?", id);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("删除失败", e);
        }
    }

    @Override
    public void update(Address address) {
        Object[] params = {address.getDetail(), address.getName(), address.getPhone(), address.getId()};
        try {
            qr.update("update tb_address set detail=?, name=?, phone=? where id=? ", params);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("修改地址失败", e);
        }
    }

    @Override
    public Address findById(int aid) {
        try {
            return qr.query("select * from tb_address where id = ?", new BeanHandler<>(Address.class),aid);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("查询地址失败", e);
        }
    }
}
