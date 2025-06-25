package com.example.webproj.service;

import com.example.webproj.mappers.ProductTypeMapper;
import com.example.webproj.pojo.ProductType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductTypeServiceImpl implements ProductTypeService {

    @Autowired
    private ProductTypeMapper productTypeMapper;

    @Override
    public boolean hasChildren(Integer id) {
        return productTypeMapper.countChildren(id) > 0;
    }

    @Override
    public boolean hasProducts(Integer id) {
        // 检查 product 表中是否有记录的 product_id 或 parts_id 等于 id
        return productTypeMapper.countProductsByProductIdOrPartsId(id) > 0;
    }

    @Override
    @Transactional
    public void deleteParam(Integer id) {
        productTypeMapper.deleteById(id);
    }

    @Override
    public List<ProductType> findPathParam() {
        return productTypeMapper.findPathParam();
    }

    @Override
    public List<ProductType> findPartsType(Integer productTypeId) {
        return productTypeMapper.findPartsType(productTypeId);
    }

    @Override
    public List<ProductType> findPType() {
        return productTypeMapper.findPType();
    }

    @Override
    public List<ProductType> findChildren(Integer id) {
        return productTypeMapper.findChildren(id);
    }

    @Override
    @Transactional
    public void updateParam(ProductType productType) {
        productTypeMapper.update(productType);
    }

    @Override
    @Transactional
    public void saveParam(ProductType productType) {
        productTypeMapper.insert(productType);
    }
}
