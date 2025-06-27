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
    public List<ProductType> findAllParams() {
        // 查询顶级节点（parent_id = 0）
        List<ProductType> topLevelTypes = productTypeMapper.findChildren(0);
        // 递归构建树形结构
        for (ProductType type : topLevelTypes) {
            buildTree(type);
        }
        return topLevelTypes;
    }

    // 递归构建树形结构
    private void buildTree(ProductType parent) {
        // 查询子节点
        List<ProductType> children = productTypeMapper.findChildren(parent.getId());
        // 设置子节点
        parent.setChildren(children);
        // 递归处理每个子节点
        for (ProductType child : children) {
            buildTree(child);
        }
    }


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
    @Transactional(rollbackFor = Exception.class) // 明确指定异常回滚
    public void updateParam(ProductType productType) {
        // 1. 检查ID是否存在
        ProductType existing = productTypeMapper.selectById(productType.getId());
        if (existing == null) {
            throw new IllegalArgumentException("ID不存在");
        }

        // 2. 更新时间并执行更新
        productType.updateTime();
        int affectedRows = productTypeMapper.update(productType);

        // 3. 验证影响行数
        if (affectedRows == 0) {
            throw new RuntimeException("更新失败，记录可能不存在或数据未变化");
        }
    }

    @Override
    @Transactional
    public void saveParam(ProductType productType) {
        productType.initCreateTime(); // 初始化时间
        productTypeMapper.insert(productType);
    }
}
