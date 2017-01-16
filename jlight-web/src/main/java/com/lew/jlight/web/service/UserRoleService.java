package com.lew.jlight.web.service;

import com.lew.jlight.web.entity.UserRole;

import java.util.List;

public interface UserRoleService {

    List<UserRole> getListByUserId(String userId);

    List<String> getRoleIdsByUserId(String userId);

    void add(String[] roleIds,String userId);
}
