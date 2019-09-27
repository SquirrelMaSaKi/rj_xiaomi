package com.xiaomi.service;

import com.xiaomi.domain.PageBean;
import com.xiaomi.domain.User;

import java.util.List;

public interface UserService {
    void register(User user);
    //检查用户名是否存在
    User checkUserName(String username);
    //登录
    User login(String username, String password);

    void modifyPassword(int uid, String newpassword);

    List<User> findAll();

    PageBean<User> findByPage(int pageNum, int pageSize, String condition);

    void updateFlag(int uid, int flag);
}
