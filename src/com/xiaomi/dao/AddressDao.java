package com.xiaomi.dao;

import com.xiaomi.domain.Address;

import java.util.List;

public interface AddressDao {
    public List<Address> findByUid(int uid);

    void add(Address address);

    void updateDefault(int aid, int uid);

    void delete(int id);

    void update(Address address);

    Address findById(int aid);
}
