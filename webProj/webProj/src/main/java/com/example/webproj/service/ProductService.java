package com.example.webproj.service;

import com.example.webproj.pojo.PageResult;
import com.example.webproj.pojo.Product;

import java.util.Map;

public interface ProductService {
    Map<String, Object> getProductList(String id);
    PageResult<Product> searchProducts(Integer pageNum, Integer pageSize, String id, String name, Integer status);
    Map<String, Object> getProductDetail(Integer productId);
    public Product getProductDetailbyid(int productId);
    Map<String, Object> updateProductStatus(Integer productId, Integer status, Integer hot);

    //商品新增及更新接口
    Map<String, Object> updateProduct(Map<String, Object> params);
    Map<String, Object> addProduct(Map<String, Object> params);
    boolean checkProductNameExists(String name, Integer excludeId);

    //首页楼层商品数据接口
    Map<String, Object> getFloorProducts();

    Map<String, Object> getHotProducts(int num);

    Map<String, Object> searchProductsByType(Integer pageNum, Integer pageSize,
                                             String productTypeId, String partsId,
                                             String name);

}