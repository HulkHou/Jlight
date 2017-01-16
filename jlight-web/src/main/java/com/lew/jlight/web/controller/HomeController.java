package com.lew.jlight.web.controller;

import com.lew.jlight.web.entity.Role;
import com.lew.jlight.web.entity.User;
import com.lew.jlight.web.service.RoleService;
import com.lew.jlight.web.service.UserService;
import com.lew.jlight.web.util.SystemInfoUtil;
import com.lew.jlight.web.util.UserContextUtil;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;

@Controller
public class HomeController {

    @Resource
    private UserService userService;

    @Resource
    private RoleService roleService;

    @GetMapping("home")
    public String home(ModelMap modelMap) {
        modelMap.put("systemInfo", SystemInfoUtil.getSystemInfo());
        User user = userService.getByUserId(UserContextUtil.getUserId());
        Role role = roleService.getByRoleId(UserContextUtil.getCurrentRoleId());
        modelMap.put("account",user.getAccount());
        modelMap.put("lastLoginIp",user.getLoginIp());
        modelMap.put("role",role.getName());
        return "home";
    }
}
