package com.lew.jlight.web.controller;

import com.lew.jlight.core.Response;
import com.lew.jlight.core.page.Page;
import com.lew.jlight.mybatis.ParamFilter;
import com.lew.jlight.web.aop.annotaion.WebLogger;
import com.lew.jlight.web.entity.Product;
import com.lew.jlight.web.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@Controller
@RequestMapping("product")
public class ProductController {

    @Resource
    private ProductService productService;

    @GetMapping("listPage")
    public String list() {
        return "product";
    }

    @ResponseBody
    @PostMapping("list")
    @WebLogger("查询商品列表")
    public Response list(@RequestBody ParamFilter queryFilter) {
        List productList = productService.getList(queryFilter);
        Page page = queryFilter.getPage();
        return new Response(productList, page);
    }

    @ResponseBody
    @PostMapping("add")
    @WebLogger("添加商品信息")
    public Response add(@RequestBody Product product) {
        checkNotNull(product, "商品信息不能为空");
        productService.add(product);
        return new Response("添加成功");
    }

    @ResponseBody
    @PostMapping("edit")
    @WebLogger("编辑商品信息")
    public Response edit(@RequestBody Product product) {
        productService.update(product);
        return new Response("修改成功");
    }

    @ResponseBody
    @PostMapping("delete")
    @WebLogger("删除商品信息")
    public Response delete(@RequestBody List<String> productIds) {
        checkArgument((productIds != null && productIds.size() > 0), "商品ID不能为空");
        productService.delete(productIds);
        return new Response("删除成功");
    }

    @ResponseBody
    @PostMapping("detail")
    @WebLogger("查询商品详细信息")
    public Response detail(@RequestBody String productId) {
        Map product = productService.getDetail(productId);
        return new Response(product);
    }

//    @ResponseBody
//    @PostMapping("getProduct")
//    @WebLogger("查询商品分类")
//    public Response getProduct() {
//        List product = productService.getProduct();
//        return new Response(product);
//    }

}
