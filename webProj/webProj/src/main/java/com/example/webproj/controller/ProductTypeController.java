package com.example.webproj.controller;

import com.example.webproj.pojo.ProductType;
import com.example.webproj.service.ProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/actionmall")
public class ProductTypeController {

    @Autowired
    private ProductTypeService productTypeService;

    /**
     * 递归获取产品类型数据接口(前端接口)
     * 该接口用于前台获取产品参数的数据。
     */
    @GetMapping("/param/findallparams.do")
    public Map<String, Object> findAllParams() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<ProductType> productTypes = productTypeService.findAllParams();
            if (productTypes.isEmpty()) {
                result.put("status", 1);
                result.put("msg", "没有有效的产品类型数据");
            } else {
                result.put("status", 0);
                result.put("data", productTypes);
            }
        } catch (Exception e) {
            result.put("status", 1);
            result.put("msg", "查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 删除配件类型参数接口
     * @param formId 产品类型ID
     * @return 结果
     */
    @PostMapping(value = "/mgr/param/delparam.do", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public Map<String, Object> deleteParam(
            @RequestBody(required = false) Map<String, String> jsonBody,
            @RequestParam(value = "id", required = false) String formId) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 从 form-urlencoded 或 JSON 中提取 id
            String id = formId != null ? formId : (jsonBody != null ? jsonBody.get("id") : null);
            if (id == null || id.trim().isEmpty()) {
                result.put("status", 1);
                result.put("msg", "ID 不能为空");
                return result;
            }
            Integer parsedId;
            try {
                parsedId = Integer.parseInt(id);
                if (parsedId <= 0) {
                    result.put("status", 1);
                    result.put("msg", "ID 必须为正整数");
                    return result;
                }
            } catch (NumberFormatException e) {
                result.put("status", 1);
                result.put("msg", "ID 格式无效：必须为数字");
                return result;
            }
            boolean hasChildren = productTypeService.hasChildren(parsedId);
            boolean hasProducts = productTypeService.hasProducts(parsedId);
            if (hasChildren) {
                result.put("status", 1);
                result.put("msg", "请先删除子类型！");
            } else if (hasProducts) {
                result.put("status", 1);
                result.put("msg", "不能删除有商品的类型！");
            } else {
                productTypeService.deleteParam(parsedId);
                result.put("status", 0);
                result.put("msg", "删除成功");
            }
        } catch (IllegalArgumentException e) {
            result.put("status", 1);
            result.put("msg", e.getMessage());
        } catch (Exception e) {
            result.put("status", 1);
            result.put("msg", "删除失败：" + e.getMessage());
        }
        return result;
    }
    /**
     * 获取带路径的参数信息接口
     * @return 结果
     */
    @GetMapping("/mgr/param/findpathparam.do")
    public Map<String, Object> findPathParam() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<ProductType> pathParams = productTypeService.findPathParam();
            result.put("status", 0);
            result.put("data", pathParams);
        } catch (Exception e) {
            result.put("status", 1);
            result.put("msg", "查询失败！");
        }
        return result;
    }

    /**
     * 获取配件类型参数接口
     * @param productTypeId 产品类型ID
     * @return 结果
     */
    @GetMapping("/mgr/param/findpartstype.do")
    public Map<String, Object> findPartsType(@RequestParam String productTypeId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<ProductType> partsTypes = productTypeService.findPartsType(Integer.parseInt(productTypeId));
            result.put("status", 0);
            result.put("data", partsTypes);
        } catch (Exception e) {
            result.put("status", 1);
            result.put("msg", "查询失败！");
        }
        return result;
    }

    /**
     * 获取产品类型参数接口
     * @return 结果
     */
    @GetMapping("/mgr/param/findptype.do")
    public Map<String, Object> findPType() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<ProductType> pTypes = productTypeService.findPType();
            result.put("status", 0);
            result.put("data", pTypes);
        } catch (Exception e) {
            result.put("status", 1);
            result.put("msg", "查询失败！");
        }
        return result;
    }

    /**
     * 获取产品参数数据接口
     * @param id 产品参数ID
     * @return 结果
     */
    @PostMapping("/mgr/param/findchildren.do")
    public Map<String, Object> findChildren(@RequestParam String id) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<ProductType> children = productTypeService.findChildren(Integer.parseInt(id));
            result.put("status", 0);
            result.put("data", children);
        } catch (Exception e) {
            result.put("status", 1);
            result.put("msg", "查询失败！");
        }
        return result;
    }

    /**
     * 产品参数更新接口
     * @param params 产品参数ID
     * @return 结果
     */
    @PostMapping("/mgr/param/updateparam.do")
    public Map<String, Object> updateParam(@RequestBody Map<String, String> params) {
        String id = params.get("id");
        String name = params.get("name");
        Map<String, Object> result = new HashMap<>();
        try {
            if (id == null || name == null) {
                result.put("status", 1);
                result.put("msg", "参数错误！");
                return result;
            }
            ProductType productType = new ProductType();
            productType.setId(Integer.parseInt(id));
            productType.setName(name);
            productType.updateTime();
            productTypeService.updateParam(productType);
            result.put("status", 0);
            result.put("msg", "产品参数修改成功！");
        } catch (Exception e) {
            System.out.println(e);
            result.put("status", 1);
            result.put("msg", "产品参数修改失败！");
        }
        return result;
    }

    /**
     * 产品类型新增接口
     *  name 产品参数类型
     *  parentId 父类型ID
     *  sortOrder 序号
     * @return 结果
     */
    @PostMapping("/mgr/param/saveparam.do")
    public Map<String, Object> saveParam(@RequestBody Map<String, String> params) {
        String name = params.get("name");
        String parentId = params.get("parentId");
        String sortOrder = params.get("sortOrder");
        Map<String, Object> result = new HashMap<>();
        try {
            if (name == null || parentId == null || sortOrder == null) {
                result.put("status", 1);
                result.put("msg", "参数错误！");
                return result;
            }
            ProductType productType = new ProductType();
            productType.setName(name);
            productType.setParentId(Integer.parseInt(parentId));
            productType.setSortOrder(Integer.parseInt(sortOrder));
            productType.setStatus(true);
            productType.setLevel(Integer.parseInt(parentId) == 0 ? 0 : 1);
            productType.initCreateTime();
            productTypeService.saveParam(productType);
            result.put("status", 0);
            result.put("msg", "产品参数新增成功！");
        } catch (Exception e) {
            System.out.println(e);
            result.put("status", 1);
            result.put("msg", "产品参数新增失败！");
        }
        return result;
    }

    
}

