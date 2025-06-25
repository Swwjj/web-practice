package com.example.webproj.mappers;

import com.example.webproj.pojo.ProductType;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProductTypeMapper {
    @Select("SELECT COUNT(*) FROM product_type WHERE parent_id = #{id}")
    int countChildren(Integer id);

    @Select("SELECT COUNT(*) FROM product WHERE product_id = #{id} OR parts_id = #{id}")
    int countProductsByProductIdOrPartsId(Integer id);

    @Delete("DELETE FROM product_type WHERE id = #{id}")
    void deleteById(Integer id);

    @Select("SELECT * FROM product_type WHERE level > 0 AND status = 1")
    List<ProductType> findPathParam();

    @Select("SELECT * FROM product_type WHERE parent_id = #{productTypeId} AND status = 1")
    List<ProductType> findPartsType(Integer productTypeId);

    @Select("SELECT * FROM product_type WHERE parent_id = 0 AND status = 1")
    List<ProductType> findPType();

    @Select("SELECT * FROM product_type WHERE parent_id = #{id} AND status = 1")
    List<ProductType> findChildren(Integer id);

    @Update("UPDATE product_type SET name = #{name}, updated = #{updated} WHERE id = #{id}")
    void update(ProductType productType);

    @Insert("INSERT INTO product_type (parent_id, name, sort_order, status, level, created, updated) " +
            "VALUES (#{parentId}, #{name}, #{sortOrder}, #{status}, #{level}, #{created}, #{updated})")
    void insert(ProductType productType);
}
