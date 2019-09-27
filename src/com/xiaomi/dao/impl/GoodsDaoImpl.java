package com.xiaomi.dao.impl;

import com.xiaomi.dao.GoodsDao;
import com.xiaomi.domain.Goods;
import com.xiaomi.utils.DruidUtils;
import com.xiaomi.utils.StringUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;
import java.util.List;

public class GoodsDaoImpl implements GoodsDao {
    QueryRunner qr = new QueryRunner(DruidUtils.getDataSource());
    @Override
    public long getCount(String condition) {
        String sql = "select count(*) from tb_goods";

        if (!StringUtils.isEmpty(condition)) {
            sql = sql + " where " + condition; //select count(*) from tb_goods where typeId = 1
        }

        try {
            return qr.query(sql, new ScalarHandler<>());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("查询商品个数失败", e);
        }
    }

    @Override
    public List<Goods> findPageByWhere(int pageNum, int pageSize, String condition) {
        String sql = "select * from tb_goods";

        if (!StringUtils.isEmpty(condition)) {
            sql = sql + " where " + condition; //select count(*) from tb_goods where typeId = 1
        }

        sql = sql + " order by id limit ?, ?";

        try {
            return qr.query(sql, new BeanListHandler<>(Goods.class),(pageNum-1)*pageSize, pageSize);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Goods findById(int gid) {
        try {
            return qr.query("select * from tb_goods where id = ?", new BeanHandler<>(Goods.class),gid);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("查询失败", e);
        }
    }

    @Override
    public void add(Goods goods) {
        try {
            qr.update("insert into tb_goods(name,pubdate,picture,price,star,intro,typeid) values (?,?,?,?,?,?,?);", goods.getName(),goods.getPubdate(),goods.getPicture(),goods.getPrice(),goods.getStar(),goods.getIntro(),goods.getTypeid());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("添加失败", e);
        }
    }

    @Override
    public List<Goods> findeByTypeId(int typeid) {
        try {
            return qr.query("select * from tb_goods where typeid=?", new BeanListHandler<>(Goods.class),typeid);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("查询失败", e);
        }
    }

    @Override
    public void update(Goods goods) {
        try {
            qr.update("update tb_goods set name=?,pubdate=?,picture=?,price=?,star=?,intro=?,typeid=? where id=?;", goods.getName(),goods.getPubdate(),goods.getPicture(),goods.getPrice(),goods.getStar(),goods.getIntro(),goods.getTypeid(),goods.getId());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("更新失败", e);
        }
    }

    @Override
    public void deleteByPid(int pid) {
        try {
            qr.update("delete from tb_goods where id=?",pid );
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("删除失败", e);
        }
    }
}
