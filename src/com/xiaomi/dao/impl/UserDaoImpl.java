package com.xiaomi.dao.impl;

import com.xiaomi.dao.UserDao;
import com.xiaomi.domain.User;
import com.xiaomi.utils.DruidUtils;
import com.xiaomi.utils.StringUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;
import java.util.List;

public class UserDaoImpl implements UserDao {
    QueryRunner qr = new QueryRunner(DruidUtils.getDataSource());

    @Override
    public List<User> findAll() {
        try {
            return qr.query("select * from tb_user where flag != 2;", new BeanListHandler<>(User.class));
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("查询所有失败", e);
        }
    }

    @Override
    public User findById(Integer id) {
        try {
            return qr.query("select * from tb_user where id = ?", new BeanHandler<>(User.class),id);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("根据id查询失败", e);
        }
    }

    @Override
    public User findByUserNameAndPassword(String username, String password) {
        try {
            return qr.query("select * from tb_user where username = ? and password = ?", new BeanHandler<>(User.class),username, password);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("根据用户名和密码查询失败", e);
        }
    }

    @Override
    public User findByUserName(String username) {
        try {
            return qr.query("select * from tb_user where username = ?", new BeanHandler<>(User.class),username);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("根据用户名查询失败", e);
        }
    }

    @Override
    public void add(User user) {
        Object[] params = {user.getId(),user.getUsername(), user.getPassword(), user.getEmail(), user.getGender(), user.getFlag(), user.getRole(), user.getCode()};
        try {
            qr.update("insert into tb_user(id,username,password,email,gender,flag,role,code) values (?,?,?,?,?,?,?,?);", params);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("添加失败", e);
        }
    }

    @Override
    public void update(User user) {
        Object[] params = {user.getUsername(), user.getPassword(), user.getEmail(), user.getGender(), user.getFlag(), user.getId()};
        try {
            qr.update("update tb_user set username = ?,password = ?,email = ?,gender = ?,flag = ? where id = ?);", params);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("添加失败", e);
        }
    }

    @Override
    public void delete(Integer id) {
        try {
            qr.update("delete from tb_user where id = ?", id);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("删除失败", e);
        }
    }

    @Override
    public void updatePassword(int uid, String newpassword) {
        try {
            qr.update("update tb_user set password = ? where id = ?;", newpassword,uid);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("修改失败", e);
        }
    }

    @Override
    public long getCount(String condition) {
        String sql = "select count(*) from tb_user";

        if (!StringUtils.isEmpty(condition)) {
            //注意空格
            sql = sql + " where " + condition; //select count(*) from tb_goods where typeId = 1
        }

        try {
            return qr.query(sql, new ScalarHandler<>());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("查询用户数量失败", e);
        }
    }

    @Override
    public List<User> findByPage(int pageNum, int pageSize, String condition) {
        String sql = "select * from tb_user";
        if (!StringUtils.isEmpty(condition)) {
            sql = sql + " where " + condition;
        }
        sql = sql + " order by id limit ?,?";

        try {
            return qr.query(sql, new BeanListHandler<>(User.class),(pageNum-1)*pageSize, pageSize);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("查询用户失败", e);
        }
    }

    @Override
    public void updateFlag(int uid, int flag) {
        try {
            qr.update("update tb_user set flag = ? where id = ?", flag,uid);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("删除失败", e);
        }
    }
}
