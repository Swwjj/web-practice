package com.example.webproj.service;
import com.example.webproj.pojo.Address;
import com.example.webproj.mappers.AddressMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.time.LocalDateTime;


@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressMapper addressMapper;

//    @Override
//    public Address findAddressById(Integer id, Integer uid) {
//        return null;
//    }

    @Override
    public List<Address> findAddressesByUid(Integer uid) {
        return List.of();
    }

//    @Override
//    public boolean updateAddress(Address address, Integer uid) {
//        return false;
//    }

//    @Override
//    public List<Address> setDefaultAddress(Long id, Long uid) {
//        return List.of();
//    }
//
//    @Override
//    public List<Address> deleteAddress(Long id, Long uid) {
//        return List.of();
//    }
//
//    @Override
//    public List<Address> saveAddress(Address address, Long uid) {
//        return List.of();
//    }

    @Override
    public Address findAddressById(Integer id, Integer userId) {
        Address address = addressMapper.findById(id);
        return address != null && address.getUserId().equals(userId) ? address : null;
    }

    @Override
    public List<Address> findAddressesByUserId(Integer userId) {
        return addressMapper.findByUserId(userId);
    }

    @Override
    public boolean updateAddress(Address address, Integer userId) {
        if (address.getId() == null || userId == null) {
            return false;
        }
        address.setUserId(userId);
        address.setUpdated(LocalDateTime.now());
        return addressMapper.updateAddress(address) > 0;
    }

    @Transactional
    @Override
    public List<Address> setDefaultAddress(Integer id, Integer userId) {
        // 先将所有地址设为非默认
        addressMapper.resetDefaultAddress(userId);
        // 设置指定地址为默认
        addressMapper.setDefaultAddress(id, userId);
        return addressMapper.findByUserId(userId);
    }

    @Override
    public List<Address> deleteAddress(Integer id, Integer userId) {
        addressMapper.deleteAddress(id, userId);
        return addressMapper.findByUserId(userId);
    }

    @Override
    public List<Address> saveAddress(Address address, Integer userId) {
        address.setUserId(userId);
        address.setDfault(false);
        address.setIsDel(false);
        address.setCreated(LocalDateTime.now());
        address.setUpdated(LocalDateTime.now());
        addressMapper.insertAddress(address);
        return addressMapper.findByUserId(userId);
    }
}


