package com.lew.jlight.web.controller;

import com.lew.jlight.core.Response;
import com.lew.jlight.core.page.Page;
import com.lew.jlight.mybatis.ParamFilter;
import com.lew.jlight.web.aop.annotaion.WebLogger;
import com.lew.jlight.web.entity.ProductCate;
import com.lew.jlight.web.entity.User;
import com.lew.jlight.web.service.ProductCateService;
import com.lew.jlight.web.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@Controller
@RequestMapping("productCate")
public class ProductCateController {

    @Resource
    private ProductCateService productCateService;

    @GetMapping("listPage")
    public String list() {
        return "productCate";
    }


    @ResponseBody
    @PostMapping("list")
    @WebLogger("查询商品分类列表")
    public Response list(@RequestBody  ParamFilter queryFilter) {
        List productCateList = productCateService.getList(queryFilter);
        Page page = queryFilter.getPage();
        return new Response(productCateList,page);
    }

    @ResponseBody
    @PostMapping("add")
    @WebLogger("添加商品分类")
    public Response add(@RequestBody ProductCate productCate) {
        checkNotNull(productCate, "商品分类不能为空");
        productCateService.add(productCate);
        return new Response("添加成功");
    }


    @ResponseBody
    @PostMapping("edit")
    @WebLogger("编辑商品分类")
    public Response edit(@RequestBody ProductCate productCate) {
        productCateService.update(productCate);
        return new Response("修改成功");
    }

    @ResponseBody
    @PostMapping("delete")
    @WebLogger("删除商品分类")
    public Response delete(@RequestBody List<String> cateIds) {
        checkArgument((cateIds != null && cateIds.size() > 0), "分类编号不能为空");
        productCateService.delete(cateIds);
        return new Response("删除成功");
    }

//    @ResponseBody
//    @PostMapping("resetPwd")
//    @WebLogger("重置密码")
//    public Response resetPwd(@RequestBody List<String> userIds) {
//        checkArgument((userIds != null && userIds.size() > 0), "用户编号不能为空");
//        userService.updateDefaultPwd(userIds);
//        return new Response("重置成功");
//    }
//
//
//    @ResponseBody
//    @PostMapping("changePwd")
//    @WebLogger("更改密码")
//    public Response changePwd(String originPwd, String confirmPwd, String newPwd) {
//        userService.updatePwd(originPwd, newPwd, confirmPwd);
//        return new Response("更改密码成功");
//    }
//
    @ResponseBody
    @PostMapping("detail")
    @WebLogger("查询商品分类详细")
    public Response detail(@RequestBody String cateId) {
        Map productCate = productCateService.getDetail(cateId);
        return new Response(productCate);
    }
}
