package com.example.webproj.service;
import com.example.webproj.pojo.Address;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AddressService {
    Address findAddressById(Integer id, Integer uid);

    List<Address> findAddressesByUid(Integer uid);

    boolean updateAddress(Address address, Integer uid);

//    List<Address> setDefaultAddress(Long id, Long uid);
//
//    List<Address> deleteAddress(Long id, Long uid);
//
//    List<Address> saveAddress(Address address, Long uid);

//    Address findAddressById(Integer id, Integer userId);

    List<Address> findAddressesByUserId(Integer userId);

//    boolean updateAddress(Address address, Integer userId);

    @Transactional
    List<Address> setDefaultAddress(Integer id, Integer userId);

    List<Address> deleteAddress(Integer id, Integer userId);

    List<Address> saveAddress(Address address, Integer userId);
}
