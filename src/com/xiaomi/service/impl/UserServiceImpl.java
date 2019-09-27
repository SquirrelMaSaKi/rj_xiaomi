package com.xiaomi.service.impl;

import com.xiaomi.dao.UserDao;
import com.xiaomi.dao.impl.UserDaoImpl;
import com.xiaomi.domain.PageBean;
import com.xiaomi.domain.User;
import com.xiaomi.service.UserService;
import com.xiaomi.utils.EmailUtils;
import com.xiaomi.utils.MD5Utils;

import java.util.List;

public class UserServiceImpl implements UserService {
    UserDao userDao = new UserDaoImpl();

    @Override
    public void register(User user) {
        //密码加密+发送邮件的业务
        user.setPassword(MD5Utils.md5(user.getPassword()));
        userDao.add(user);

        //发送邮件
        EmailUtils.sendEmail(user);
    }

    @Override
    public User checkUserName(String username) {
        return userDao.findByUserName(username);
    }

    @Override
    public User login(String username, String password) {
        //把密码加密后再比对
        password = MD5Utils.md5(password);
        User user = userDao.findByUserNameAndPassword(username, password);
        /*if (user != null) {
            if (user.getFlag() != 1) {
                throw new RuntimeException("此用户未激活或者已经失效");
            }
        }*/
        return user;
    }

    @Override
    public void modifyPassword(int uid, String newpassword) {
        newpassword = MD5Utils.md5(newpassword);
        userDao.updatePassword(uid, newpassword);
    }

    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }

    @Override
    public PageBean<User> findByPage(int pageNum, int pageSize, String condition) {
        long totalSize = userDao.getCount(condition);
        List<User> data = userDao.findByPage(pageNum, pageSize, condition);

        PageBean<User> pageBean = new PageBean<>(pageNum, pageSize, totalSize, data);

        return pageBean;
    }

    @Override
    public void updateFlag(int uid, int flag) {
        userDao.updateFlag(uid,flag);
    }
}
