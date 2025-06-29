package com.example.webproj.controller;

import com.example.webproj.pojo.PageResult;
import com.example.webproj.pojo.Product;
import com.example.webproj.service.ProductService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@RequestMapping("/actionmall/mgr/product")
public class ProductController {

    private String uploadPath = "./uploads"; // 默认值，可根据需要修改

    @Autowired
    private ProductService productService;

    // 初始化上传目录
    @PostConstruct
    public void init() throws IOException {
        Path uploadDir = Paths.get(uploadPath);
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
    }

    // 商品查询列表接口
    @PostMapping("/productlist.do")
    public Map<String, Object> getProductList(@RequestParam(required = false) String id) {
        return productService.getProductList(id);
    }

    // 商品图片上传接口(原有)
    @PostMapping("/upload-image.do")
    public Map<String, Object> uploadProductImage(@RequestParam("file") MultipartFile file) {
        return handleSingleFileUpload(file);
    }

    // 富文本编辑器多图片上传接口
    @PostMapping("/pic_upload.do")
    public Map<String, Object> uploadEditorImages(@RequestParam("files") MultipartFile[] files) {
        Map<String, Object> result = new HashMap<>();

        try {
            if (files == null || files.length == 0) {
                return createErrorResponse(1, "请选择至少一个文件");
            }

            List<Map<String, String>> fileData = new ArrayList<>();
            for (MultipartFile file : files) {
                try {
                    if (!file.getContentType().startsWith("image/")) {
                        return createErrorResponse(1, "只能上传图片文件");
                    }
                    String fileName = saveFile(file);
                    Map<String, String> fileInfo = new HashMap<>();
                    fileInfo.put("url", "/upload/" + fileName);
                    fileData.add(fileInfo);
                } catch (Exception e) {
                    // Log the error for individual file processing
                    // You might want to continue processing other files or return error
                    return createErrorResponse(1, "处理文件时出错: " + e.getMessage());
                }
            }

            result.put("status", 0);
            result.put("data", fileData);
        } catch (Exception e) {
            // Log the general error
            return createErrorResponse(1, "上传过程中发生错误: " + e.getMessage());
        }

        return result;
    }

    // 商品图片上传接口(单文件，符合规范)
    @PostMapping("/upload.do")
    public Map<String, Object> uploadSingleImage(@RequestParam("file") MultipartFile file) {
        Map<String, Object> response = handleSingleFileUpload(file);
        // 调整返回格式完全符合规范
        if (response.get("status").equals(0)) {
            Map<String, String> data = new HashMap<>();
            data.put("url", (String) ((Map<?, ?>) response.get("data")).get("url"));
            response.put("data", data);
        }
        return response;
    }

    // 通用单文件上传处理
    private Map<String, Object> handleSingleFileUpload(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return createErrorResponse(1, "上传文件不能为空");
            }

            if (!file.getContentType().startsWith("image/")) {
                return createErrorResponse(1, "只能上传图片文件");
            }

            String fileName = saveFile(file);

            Map<String, Object> data = new HashMap<>();
            data.put("url", "/upload/" + fileName);

            return createSuccessResponse(data);
        } catch (IOException e) {
            return createErrorResponse(1, "文件上传错误");
        }
    }

    // 保存文件到服务器
    private String saveFile(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID() +
                getFileExtension(file.getOriginalFilename());
        Path targetPath = Paths.get(uploadPath).resolve(fileName);
        Files.copy(file.getInputStream(), targetPath);
        return fileName;
    }

    // 获取文件扩展名
    private String getFileExtension(String filename) {
        return filename.substring(filename.lastIndexOf("."));
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

    @PostMapping("/searchproducts.do")
    public Map<String, Object> searchProducts(@RequestBody Map<String, String> params) {
        if (!validateSearchParams(params)) {
            return createErrorResponse(1, "参数错误");
        }

            // 解析参数
            Integer pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum")) : 1;
            Integer pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize")) : 10;
            String id = params.get("id");
            String name = params.get("name");
            Integer status = params.get("status") != null ? Integer.parseInt(params.get("status")) : null;

            // 调用服务层
        PageResult<Product> pageResult= productService.searchProducts(pageNum, pageSize, id, name, status);

        Map<String, Object> result= new HashMap<>();
        Map<String, Object> resultVo= new HashMap<>();
        resultVo.put("pageNum", pageResult.getPageNum());
        resultVo.put("pageSize", pageResult.getPageSize());
        resultVo.put("totalRecord", pageResult.getTotalRecord());
        resultVo.put("totalPage", pageResult.getTotalPage());
        resultVo.put("startIndex", pageResult.getStartIndex());
        resultVo.put("prePage", pageResult.getPageNum()-1);
        resultVo.put("nextPage", pageResult.getPageNum()+1);
        resultVo.put("data", pageResult.getData());

        result.put("status", 0);
        result.put("data", resultVo);
        return result;

    }

    private boolean validateSearchParams(Map<String, String> params) {
        if (params == null) {
            return false;
        }

        // 检查pageNum和pageSize是否为有效数字
        try {
            if (params.get("pageNum") != null) {
                int pageNum = Integer.parseInt(params.get("pageNum"));
                if (pageNum < 1) return false;
            }
            if (params.get("pageSize") != null) {
                int pageSize = Integer.parseInt(params.get("pageSize"));
                if (pageSize < 1 || pageSize > 100) return false; // 限制最大100条
            }
        } catch (NumberFormatException e) {
            return false;
        }

        // 商品分页列表接口的特有参数验证
        if (params.containsKey("productTypeId") || params.containsKey("partsId")) {
            if (params.get("productTypeId") == null || params.get("partsId") == null) {
                return false;
            }
        }
        return true;
    }



    @PostMapping("/getdetail.do/{id}")
    public ResponseEntity<Product> getProductDetailbyid(@PathVariable("id") int id) {
        Product product = productService.getProductDetailbyid(id);
        if (product == null) {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("error", "Student " + id + " not found");
            return new ResponseEntity(errorMap, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(product, HttpStatus.OK);
    }



    @PostMapping("/setstatus.do")
    public Map<String, Object> setProductStatus(@RequestBody Map<String, String> params) {
        // 验证参数
        if (params == null) {
            return createErrorResponse(1, "参数不能为空");
        }

        // 解析参数
        Integer productId = parseInteger(params.get("productId"));
        Integer status = parseInteger(params.get("status"));
        Integer hot = parseInteger(params.get("hot"));

        // 参数验证
        if (productId == null || productId <= 0) {
            return createErrorResponse(1, "商品ID不能为空");
        }

        if (status == null || (status < 1 || status > 3)) {
            return createErrorResponse(1, "非法状态参数！");
        }

        if (hot == null || (hot != 1 && hot != 2)) {
            return createErrorResponse(1, "非法热销参数！");
        }

        // 调用服务层更新状态
        return productService.updateProductStatus(productId, status, hot);

    }

    // 辅助方法：安全转换字符串为Integer
    private Integer parseInteger(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }


    //商品新增及更新接口
    @PostMapping("/saveproduct.do")
    public Map<String, Object> saveProduct(@RequestBody Map<String, Object> params) {
            // 检查必填参数
            if (!validateProductParams(params)) {
                return createErrorResponse(1, "缺少必要参数");
            }

            // 根据是否有id决定是新增还是更新
            if (params.containsKey("id") && params.get("id") != null) {
                return productService.updateProduct(params);
            } else {
                return productService.addProduct(params);
            }
    }

    //商品新增及更新接口
    // 验证商品参数
    private boolean validateProductParams(Map<String, Object> params) {
        String[] requiredFields = {"name", "productId", "partsId", "detail",
                "specParam", "price", "stock", "subImages"};

        for (String field : requiredFields) {
            Object value = params.get(field);
            if (value == null || !(value instanceof String) || ((String) value).trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }



    //首页楼层商品数据接口
    @PostMapping("/findfloors.do")
    public Map<String, Object> getFloorProducts() {
        return productService.getFloorProducts();
    }

    @PostMapping("/findhotproducts.do")
    public Map<String, Object> getHotProducts(@RequestBody Map<String, String> params) {
        try {
            // 验证参数
            if (params == null || params.get("num") == null) {
                return createErrorResponse(1, "数量参数不能为空");
            }

            int num;
            try {
                num = Integer.parseInt(params.get("num"));
            } catch (NumberFormatException e) {
                return createErrorResponse(1, "数量参数格式错误");
            }

            return productService.getHotProducts(num);
        } catch (Exception e) {
            return createErrorResponse(2, "系统错误，获取热销商品失败");
        }
    }


    @PostMapping("/findproducts.do")
    public Map<String, Object> searchProductsByType(@RequestBody Map<String, String> params) {

            // 参数验证
            if (!validateSearchParams(params)) {
                return createErrorResponse(1, "参数错误");
            }

            // 解析参数
            Integer pageNum = Integer.parseInt(params.getOrDefault("pageNum", "1"));
            Integer pageSize = Integer.parseInt(params.getOrDefault("pageSize", "10"));
            String productTypeId = params.get("productTypeId");
            String partsId = params.get("partsId");
            String name = params.get("name");

            // 调用服务层
            Map<String, Object> result = productService.searchProductsByType(
                    pageNum, pageSize, productTypeId, partsId, name);

            // 构建返回格式
            Map<String, Object> response = new HashMap<>();
            response.put("status", 0);
            response.put("data", result);
            return response;

    }

}