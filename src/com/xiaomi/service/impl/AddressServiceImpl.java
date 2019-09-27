package com.xiaomi.service.impl;

import com.xiaomi.dao.AddressDao;
import com.xiaomi.dao.impl.AddressDaoImpl;
import com.xiaomi.domain.Address;
import com.xiaomi.service.AddressService;

import java.util.List;

public class AddressServiceImpl implements AddressService {
    AddressDao addressDao = new AddressDaoImpl();
    @Override
    public List<Address> findByUid(int uid) {
        return addressDao.findByUid(uid);
    }

    @Override
    public void add(Address address) {
        addressDao.add(address);
    }

    @Override
    public void updateDefault(int aid, int uid) {
        addressDao.updateDefault(aid, uid);
    }

    @Override
    public void delete(int id) {
        addressDao.delete(id);
    }

    @Override
    public void update(Address address) {
        addressDao.update(address);
    }
}
