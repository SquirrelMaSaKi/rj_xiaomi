package com.xiaomi.dao.impl;

import com.xiaomi.dao.GoodsTypeDao;
import com.xiaomi.domain.GoodsType;
import com.xiaomi.utils.DruidUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;
import java.util.List;

public class GoodsTypeDaoImpl implements GoodsTypeDao {
    QueryRunner qr = new QueryRunner(DruidUtils.getDataSource());
    @Override
    public List<GoodsType> findTypeByLevel(int i) {
        try {
            return qr.query("select * from tb_goods_type where level = ?;", new BeanListHandler<>(GoodsType.class),i);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public GoodsType findById(int typeid) {
        try {
            return qr.query("select * from tb_goods_type where id = ?;", new BeanHandler<>(GoodsType.class),typeid);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("查询失败", e);
        }
    }

    @Override
    public List<GoodsType> findTypeAll(int pn, int ps, String condition) {
        String sql = "select * from tb_goods_type where 1=1 " + condition + " limit ?,? ";

        try {
            return qr.query(sql, new BeanListHandler<>(GoodsType.class),(pn-1)*ps,ps);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("分页查询商品类别失败", e);
        }
    }

    @Override
    public long getCount(String condition) {
        String sql = "select count(*) from tb_goods_type where 1=1 " + condition;

        try {
            return qr.query(sql, new ScalarHandler<>());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("分页查询商品类别数量", e);
        }
    }

    @Override
    public void modifyById(GoodsType goodsType) {
        try {
            Object[] params = {goodsType.getName(),goodsType.getLevel(),goodsType.getParent(),goodsType.getId()};
            qr.update("update tb_goods_type set name=?,level=?,parent=? where id=?", params);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("修改失败", e);
        }
    }

    @Override
    public List<GoodsType> findGoodsTypes() throws SQLException {
       return qr.query("select * from tb_goods_type", new BeanListHandler<>(GoodsType.class));
    }

    @Override
    public void deleteByID(int id) {
        try {
            qr.update("delete from tb_goods_type where id = ?", id);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("删除失败", e);
        }
    }

    @Override
    public void add(GoodsType goodsType) {

        Object[] params = {goodsType.getName(),goodsType.getLevel(),goodsType.getParent()};

        try {
            qr.update("insert into tb_goods_type(name,level,parent) values (?,?,?);", params);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("添加失败", e);
        }
    }
}
