package com.lew.jlight.web.controller;

import com.google.common.base.Strings;

import com.lew.jlight.core.Response;
import com.lew.jlight.core.page.Page;
import com.lew.jlight.mybatis.ParamFilter;
import com.lew.jlight.web.aop.annotaion.WebLogger;
import com.lew.jlight.web.entity.Menu;
import com.lew.jlight.web.service.MenuService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import javax.annotation.Resource;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@Controller
@RequestMapping("menu")
public class MenuController {

    @Resource
    private MenuService menuService;

    @GetMapping("listPage")
    public String list() {
        return "menuList";
    }

    @ResponseBody
    @PostMapping("list")
    @WebLogger("查询菜单列表")
    public Response list(@RequestBody ParamFilter queryFilter) {
        List<Menu> menuList = menuService.getList(queryFilter);
        Page page = queryFilter.getPage();
        return new Response(menuList, page);
    }

    @ResponseBody
    @PostMapping("add")
    @WebLogger("添加菜单")
    public Response add(@RequestBody Menu menu) {
        checkNotNull(menu, "菜单信息不能为空");
        Response response = new Response();
        if (Strings.isNullOrEmpty(menu.getMenuId())) {
            menuService.add(menu);
            response.setMsg("添加成功");
        } else {
            menuService.update(menu);
            response.setMsg("更新成功");
        }
        return response;
    }

    @ResponseBody
    @PostMapping("edit")
    @WebLogger("编辑菜单")
    public Response edit(@RequestBody Menu menu ) {
        checkNotNull(menu, "菜单信息不能为空");
        menuService.update(menu);
        return new Response("修改成功");
    }

    @ResponseBody
    @GetMapping("detail")
    @WebLogger("查询菜单详细")
    public Response detail(String menuId) {
        Menu menu = menuService.detail(menuId);
        return new Response(menu);
    }


    @ResponseBody
    @GetMapping("getByParentId")
    public Response getByParentId(String parentId) {
        List<Menu> menuList = menuService.getByParentId(parentId);
        return new Response(menuList);
    }

    @ResponseBody
    @PostMapping("delete")
    @WebLogger("删除菜单")
    public Response delete(@RequestBody List<String> menuIds) {
        checkArgument((menuIds != null && menuIds.size() > 0), "角色编号不能为空");
        menuService.delete(menuIds);
        return new Response("删除成功");
    }

    @ResponseBody
    @GetMapping("listTree")
    public Object listTree(String roleId) {
        Response response = menuService.getResTree(roleId);
        return response.getData();
    }

    @ResponseBody
    @GetMapping("getTree")
    public Object getTree() {
        Response response = menuService.getTree();
        return response.getData();
    }

}
