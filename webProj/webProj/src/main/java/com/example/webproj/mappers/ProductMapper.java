package com.example.webproj.mappers;

import com.example.webproj.pojo.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

@Mapper
public interface ProductMapper {
    // 查询所有商品（返回Map列表）
    List<Map<String, Object>> findAllProducts();

    // 按ID查询商品（返回Map）
    Map<String, Object> findProductById(@Param("id") Integer id);

    // 查询所有商品（带分类名称）
    List<Map<String, Object>> findAllProductsWithCategory();

    // 按ID查询商品（带分类名称）
    Map<String, Object> findProductByIdWithCategory(@Param("id") Integer id);

    // 分页查询商品
    List<Product> searchProductsWithCategory(
            @Param("id") String id,
            @Param("name") String name,
            @Param("status") Integer status,
            @Param("offset") int offset,
            @Param("pageSize") int pageSize
    );

    // 查询总记录数
    int countProducts(
            @Param("id") String id,
            @Param("name") String name,
            @Param("status") Integer status
    );

    //根据ID查询商品详情
    Map<String, Object> findProductDetailById(@Param("productId") Integer productId);

    // 更新商品状态
    int updateProductStatus(
            @Param("productId") Integer productId,
            @Param("status") Integer status,
            @Param("hot") Integer hot
    );

    Product getproductbyid(int productId);

    // 插入商品
    int insertProduct(Product product);

    // 更新商品
    int updateProduct(Product product);

    // 检查商品名称是否存在
    int countByName(@Param("name") String name, @Param("excludeId") Integer excludeId);

    int insertProductFromMap(Map<String, Object> params);

    int updateProductFromMap(Map<String, Object> params);


    //首页楼层商品数据接口
    List<Map<String, Object>> findByProductId(@Param("productId") int productId);

    //热销商品接口
    List<Map<String, Object>> findHotProducts(@Param("limit") int limit);

    //商品分页列表接口
    List<Map<String, Object>> searchProductsByType(
            @Param("productTypeId") String productTypeId,
            @Param("partsId") String partsId,
            @Param("name") String name,
            @Param("offset") int offset,
            @Param("pageSize") int pageSize);

    int countProductsByType(
            @Param("productTypeId") String productTypeId,
            @Param("partsId") String partsId,
            @Param("name") String name);

    List<Map<String, Object>> findProductsByCategoryType(Integer type);

    List<Map<String, Object>> findProductsByCategory(Integer categoryId);
}

