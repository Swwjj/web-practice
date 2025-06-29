package com.example.webproj.service;

import com.example.webproj.mappers.ProductMapper;
import com.example.webproj.pojo.PageResult;
import com.example.webproj.pojo.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService {


    @Autowired
    private ProductMapper productMapper;

    @Override
    public Product getProductDetailbyid(int id) {
        return productMapper.getproductbyid(id);
    }

    @Override
    public Map<String, Object> getProductList(String id) {
        Map<String, Object> result = new HashMap<>();

        List<Map<String, Object>> products;

        if (id == null || id.isEmpty()) {
            // 不传 ID，查找全部商品
            products = productMapper.findAllProductsWithCategory();
        } else {
            // 判断是否为合法数字
            if (id.chars().allMatch(Character::isDigit)) {
                Integer productId = Integer.parseInt(id);
                Map<String, Object> product = productMapper.findProductByIdWithCategory(productId);
                products = product != null ? Collections.singletonList(product) : Collections.emptyList();
            } else {
                // ID 不是数字，仍然查找全部商品
                products = productMapper.findAllProductsWithCategory();
            }
        }

        if (products.isEmpty()) {
            result.put("status", 1);
            result.put("msg", "未找到相关商品");
        } else {
            result.put("status", 0);
            result.put("data", products);
        }

        return result;
    }

    @Override
    public PageResult<Product> searchProducts(Integer pageNum, Integer pageSize, String id, String name, Integer status) {
        // 查询总记录数
        int total = productMapper.countProducts(id, name, status);
        // 计算偏移量
        int offset = (pageNum - 1) * pageSize;

        // 查询分页数据
        List<Product> products = productMapper.searchProductsWithCategory(id, name, status, offset, pageSize);

        // 计算总页数
        int totalPage = (int) Math.ceil((double) total / pageSize);

        return new PageResult<>(pageNum, pageSize, totalPage, products);
    }

    @Override
    public Map<String, Object> getProductDetail(Integer productId) {
        Map<String, Object> result = new HashMap<>();

        try {
            if (productId == null || productId <= 0) {
                result.put("status", 1);
                result.put("msg", "商品ID不能为空");
                return result;
            }

            Map<String, Object> productDetail = productMapper.findProductDetailById(productId);

            if (productDetail == null || productDetail.isEmpty()) {
                result.put("status", 1);
                result.put("msg", "商品不存在");
                return result;
            }

            // 检查商品状态，如果是下架状态返回特定错误
            Integer status = (Integer) productDetail.get("status");
            if (status != null && status == 3) {
                result.put("status", 1);
                result.put("msg", "产品已经下架！");
                return result;
            }

            result.put("status", 0);
            result.put("data", productDetail);
        } catch (Exception e) {
            result.put("status", 2);
            result.put("msg", "获取商品详情失败2");
        }

        return result;
    }




    @Override
    public Map<String, Object> updateProductStatus(Integer productId, Integer status, Integer hot) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> product = productMapper.findProductById(productId);
            if (product == null || product.isEmpty()) {
                result.put("status", 1);
                result.put("msg", "商品不存在");
                return result;
            }
            // 执行更新
            int affectedRows = productMapper.updateProductStatus(productId, status, hot);

            if (affectedRows > 0) {
                result.put("status", 0);
                result.put("msg", "修改产品状态成功！");
            } else {
                result.put("status", 1);
                result.put("msg", "修改产品状态失败！");
            }


        return result;
    }

    // 创建成功响应
    private Map<String, Object> createSuccessResponse(Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", 0);
        response.put("data", data);
        return response;
    }

    // 创建错误响应
    private Map<String, Object> createErrorResponse(int status, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", status);
        response.put("msg", message);
        return response;
    }


    @Override
    public Map<String, Object> addProduct(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();

            // 检查商品名称是否已存在
            if (checkProductNameExists(params.get("name").toString(), null)) {
                return createErrorResponse(1, "商品名称已存在!");
            }

            // 设置默认状态和热销标志
            params.put("status", 1); // 默认待售状态
            params.put("hot", 2);    // 默认非热销

            // 执行新增
            int affectedRows = productMapper.insertProductFromMap(params);

            if (affectedRows > 0) {
                result.put("status", 0);
                result.put("msg", "产品新增成功！");
            } else {
                result.put("status", 1);
                result.put("msg", "产品新增失败！");
            }
        return result;
    }

    //商品新增及更新接口
    @Override
    public Map<String, Object> updateProduct(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();

            // 检查商品是否存在
            Integer productId = Integer.parseInt(params.get("id").toString());
            Map<String, Object> existingProduct = productMapper.findProductById(productId);
            if (existingProduct == null) {
                return createErrorResponse(1, "商品不存在");
            }

            // 检查商品名称是否重复（排除当前商品）
            if (checkProductNameExists(params.get("name").toString(), productId)) {
                return createErrorResponse(1, "商品名称已存在");
            }

            // 执行更新
            int affectedRows = productMapper.updateProductFromMap(params);

            if (affectedRows > 0) {
                result.put("status", 0);
                result.put("msg", "产品更新成功！");
            } else {
                result.put("status", 1);
                result.put("msg", "产品更新失败！");
            }

        return result;
    }

    //商品新增及更新接口
    @Override
    public boolean checkProductNameExists(String name, Integer excludeId) {
        // 参数校验
        if (name == null || name.trim().isEmpty()) {
            return false;
        }


            int count = productMapper.countByName(name, excludeId);
            return count > 0;

    }

    @Override
    public Map<String, Object> getFloorProducts() {
        Map<String, Object> result = new HashMap<>();
            // 定义楼层对应的productId
            int[] floorProductIds = {10023, 10024, 10025, 10026}; // 假设有4个楼层

            // 获取每个楼层的商品
            List<Map<String, Object>> oneFloor = productMapper.findByProductId(floorProductIds[0]);
            List<Map<String, Object>> twoFloor = productMapper.findByProductId(floorProductIds[1]);
            List<Map<String, Object>> threeFloor = productMapper.findByProductId(floorProductIds[2]);
            List<Map<String, Object>> fourFloor = productMapper.findByProductId(floorProductIds[3]);

            // 检查每个楼层是否有足够商品
            if (oneFloor.size() < 8 || twoFloor.size() < 8 || threeFloor.size() < 8) {
                return createErrorResponse(1, "楼层商品数据尚未准备完整！");
            }

            // 构建返回数据
            Map<String, Object> data = new HashMap<>();
            data.put("oneFloor", oneFloor);
            data.put("twoFloor", twoFloor);
            data.put("threeFloor", threeFloor);
            data.put("fourFloor", fourFloor);

            result.put("status", 0);
            result.put("data", data);
        return result;
    }


    @Override
    public Map<String, Object> getHotProducts(int num) {
        Map<String, Object> result = new HashMap<>();
            // 参数验证
            if (num <= 0) {
                return createErrorResponse(1, "数量参数必须大于0");
            }
            // 获取热销商品
            List<Map<String, Object>> hotProducts = productMapper.findHotProducts(num);
            // 检查是否有热销商品
            if (hotProducts == null || hotProducts.isEmpty()) {
                return createErrorResponse(1, "尚未设置热销商品！");
            }
            // 构建成功响应
            result.put("status", 0);
            result.put("data", hotProducts);
        return result;
    }



    @Override
    public Map<String, Object> searchProductsByType(Integer pageNum, Integer pageSize,
                                                    String productTypeId, String partsId,
                                                    String name) {
        // 计算偏移量
        int offset = (pageNum - 1) * pageSize;

        // 查询分页数据
        List<Map<String, Object>> products = productMapper.searchProductsByType(
                productTypeId, partsId, name, offset, pageSize);

        // 查询总记录数
        int total = productMapper.countProductsByType(productTypeId, partsId, name);

        // 计算总页数
        int totalPage = (int) Math.ceil((double) total / pageSize);

        // 构建返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("pageNum", pageNum);
        result.put("pageSize", pageSize);
        result.put("totalRecord", total);
        result.put("totalPage", totalPage);
        result.put("startIndex", offset);
        result.put("data", products);
        result.put("prePage", pageNum > 1 ? pageNum - 1 : 1);
        result.put("nextPage", pageNum < totalPage ? pageNum + 1 : totalPage);

        return result;
    }
}