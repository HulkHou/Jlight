package com.lew.jlight.web.controller;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import com.lew.jlight.core.Response;
import com.lew.jlight.core.util.JsonUtil;
import com.lew.jlight.web.aop.annotaion.WebLogger;
import com.lew.jlight.web.entity.RoleMenu;
import com.lew.jlight.web.service.RoleMenuService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@Controller
@RequestMapping("roleMenu")
public class RoleMenuController {

    @Resource
    private RoleMenuService roleMenuService;

    @ResponseBody
    @PostMapping("getList")
    @WebLogger("查询菜单权限列表")
    public Response getList(@RequestBody String json) {
        Map<String, String> param = JsonUtil.parseStringMap(json);
        assert param != null;
        String roleId = param.get("roleId");
        List<RoleMenu> roleMenuList = roleMenuService.getList(roleId);
        return new Response(roleMenuList);
    }
    
    @ResponseBody
    @PostMapping("add")
    @WebLogger("添加角色-菜单权限")
    public Response add(String roleId,@RequestParam(name="menuIds[]",required=false) String[] menuIds ) {
        checkArgument(!Strings.isNullOrEmpty(roleId), "角色编号不能为空");
       roleMenuService.update(roleId, menuIds);
        return new Response();
    }
    

    @ResponseBody
    @PostMapping("getMenuByRole")
    public Response getMenuByRole(@RequestBody  String roleId) {
        List<String> list = roleMenuService.getMenuByRole(roleId);
        return new Response(list);
    }

}
