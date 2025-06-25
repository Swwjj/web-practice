package com.example.webproj.controller;

import com.example.webproj.pojo.ProductType;
import com.example.webproj.service.ProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/actionmall/mgr/param")
public class ProductTypeController {

    @Autowired
    private ProductTypeService productTypeService;

    /**
     * 删除配件类型参数接口
     * @param id 产品类型ID
     * @return 结果
     */
    @PostMapping("/delparam.do")
    public Map<String, Object> deleteParam(@RequestParam String id) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean hasChildren = productTypeService.hasChildren(Integer.parseInt(id));
            boolean hasProducts = productTypeService.hasProducts(Integer.parseInt(id));

            if (hasChildren) {
                result.put("status", 1);
                result.put("msg", "请先删除子类型！");
            } else if (hasProducts) {
                result.put("status", 1);
                result.put("msg", "不能删除有商品的类型！");
            } else {
                productTypeService.deleteParam(Integer.parseInt(id));
                result.put("status", 0);
            }
        } catch (Exception e) {
            result.put("status", 1);
            result.put("msg", "删除失败！");
        }
        return result;
    }

    /**
     * 获取带路径的参数信息接口
     * @return 结果
     */
    @GetMapping("/findpathparam.do")
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
    @GetMapping("/findpartstype.do")
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
    @GetMapping("/findptype.do")
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
    @PostMapping("/findchildren.do")
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
     * @param id 产品参数ID
     * @param name 产品参数名称
     * @return 结果
     */
    @PostMapping("/updateparam.do")
    public Map<String, Object> updateParam(@RequestParam String id, @RequestParam String name) {
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
            result.put("status", 1);
            result.put("msg", "产品参数修改失败！");
        }
        return result;
    }

    /**
     * 产品类型新增接口
     * @param name 产品参数类型
     * @param parentId 父类型ID
     * @param sortOrder 序号
     * @return 结果
     */
    @PostMapping("/saveparam.do")
    public Map<String, Object> saveParam(@RequestParam String name, @RequestParam String parentId, @RequestParam String sortOrder) {
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
            result.put("status", 1);
            result.put("msg", "产品参数新增失败！");
        }
        return result;
    }
}
