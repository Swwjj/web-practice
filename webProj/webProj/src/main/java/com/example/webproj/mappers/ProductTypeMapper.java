package com.example.webproj.mappers;

import com.example.webproj.pojo.ProductType;
import org.apache.ibatis.annotations.*;

import java.util.List;
@Mapper
public interface ProductTypeMapper {
    List<ProductType> findAll();
    List<ProductType> findChildren(Integer parentId);
    int countChildren(Integer id);
    int countProductsByProductIdOrPartsId(Integer id);
    List<ProductType> findPathParam();
    List<ProductType> findPartsType(Integer productTypeId);
    List<ProductType> findPType();
    void deleteById(Integer id);
    int update(ProductType productType);
    void insert(ProductType productType);
    ProductType selectById(Integer id);
}