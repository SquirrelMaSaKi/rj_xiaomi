package com.xiaomi.service;

import com.xiaomi.domain.Address;

import java.util.List;

public interface AddressService {
    List<Address> findByUid(int uid);

    void add(Address address);

    void updateDefault(int aid, int uid);

    void delete(int id);

    void update(Address address);
}
