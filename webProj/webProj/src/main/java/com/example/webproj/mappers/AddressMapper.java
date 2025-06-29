package com.example.webproj.mappers;
import com.example.webproj.pojo.Address;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface AddressMapper {
    // 根据ID查询地址
    Address findById(@Param("id") Integer id);

    // 根据用户ID查询所有未删除地址
    List<Address> findByUserId(@Param("userId") Integer userId);

    // 更新地址信息
    int updateAddress(Address address);

    // 设置默认地址
    int setDefaultAddress(@Param("id") Integer id, @Param("userId") Integer userId);

    // 取消用户所有默认地址
    int resetDefaultAddress(@Param("userId") Integer userId);

    // 软删除地址
    int deleteAddress(@Param("id") Integer id, @Param("userId") Integer userId);

    // 新增地址
    int insertAddress(Address address);
}
