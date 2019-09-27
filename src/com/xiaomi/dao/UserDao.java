package com.xiaomi.dao;

import com.xiaomi.domain.User;

import java.util.List;

public interface UserDao {
    List<User> findAll();
    User findById(Integer id);
    User findByUserNameAndPassword(String username, String password);
    User findByUserName(String username);
    void add(User user);
    void update(User user);
    void delete(Integer id);

    void updatePassword(int uid, String newpassword);

    long getCount(String condition);

    List<User> findByPage(int pageNum, int pageSize, String condition);

    void updateFlag(int uid, int flag);
}
