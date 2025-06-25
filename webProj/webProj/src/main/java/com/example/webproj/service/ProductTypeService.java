package com.example.webproj.service;

import com.example.webproj.pojo.ProductType;
import java.util.List;

public interface ProductTypeService {
    boolean hasChildren(Integer id);
    boolean hasProducts(Integer id);
    void deleteParam(Integer id);
    List<ProductType> findPathParam();
    List<ProductType> findPartsType(Integer productTypeId);
    List<ProductType> findPType();
    List<ProductType> findChildren(Integer id);
    void updateParam(ProductType productType);
    void saveParam(ProductType productType);
}
