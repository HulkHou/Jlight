package com.lew.jlight.web.controller;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import com.lew.jlight.core.Response;
import com.lew.jlight.core.page.Page;
import com.lew.jlight.mybatis.ParamFilter;
import com.lew.jlight.web.aop.annotaion.WebLogger;
import com.lew.jlight.web.entity.Role;
import com.lew.jlight.web.service.RoleService;
import com.lew.jlight.web.service.UserRoleService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;


@Controller
@RequestMapping("role")
public class RoleController {

    @Resource
    private RoleService roleService;

    @Resource
    private UserRoleService userRoleService;

    @GetMapping("listPage")
    public String list() {
        return "roleList";
    }

    @ResponseBody
    @PostMapping("list")
    @WebLogger("查询角色列表")
    public Response list(@RequestBody ParamFilter queryFilter) {
        List<Role> roleList = roleService.getList(queryFilter);
        Page page = queryFilter.getPage();
        return new Response(roleList, page);
    }

    @ResponseBody
    @PostMapping("save")
    @WebLogger("添加角色")
    public Object save(@RequestBody Role role) {
        checkNotNull(role, "角色信息不能为空");
        Response response = new Response();
        if (Strings.isNullOrEmpty(role.getRoleId())) {
            roleService.add(role);
        } else {
            roleService.update(role);
        }
        response.setMsg("添加成功");
        return response;
    }

    @ResponseBody
    @PostMapping("add")
    public Object add(@RequestBody Role role) {
        checkNotNull(role, "角色信息不能为空");
        roleService.add(role);
        return new Response("添加成功");
    }

    @ResponseBody
    @RequestMapping("update")
    @WebLogger("编辑角色")
    public Response update(@RequestBody Role role) {
        checkNotNull(role, "角色信息不能为空");
        roleService.update(role);
        return new Response();
    }

    @ResponseBody
    @PostMapping("delete")
    @WebLogger("删除角色")
    public Response delete(@RequestBody List<String> roleIds) {
        checkArgument((roleIds != null && roleIds.size() > 0), "不能为空");
        roleService.delete(roleIds);
        return new Response();
    }


    @ResponseBody
    @PostMapping("detail")
    @WebLogger("查询角色详细")
    public Response detail(@RequestBody  String roleId) {
        Role role = roleService.getByRoleId(roleId);
        return new Response(role);
    }

    @ResponseBody
    @PostMapping("getRoleMap")
    public Response getRoleMap(@RequestBody String userId) {
        checkArgument(!Strings.isNullOrEmpty(userId), "用户编号不能为空");
        Response response = new Response();
        List list = roleService.getRoleMap();
        Map<String, Object> resultMap = Maps.newHashMap();
        List<String> roleIds = userRoleService.getRoleIdsByUserId(userId);
        resultMap.put("roleIds", roleIds);
        resultMap.put("roleList", list);
        response.setData(resultMap);
        return response;
    }
}
