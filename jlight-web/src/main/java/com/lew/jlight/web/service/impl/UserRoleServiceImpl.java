package com.lew.jlight.web.service.impl;


import com.google.common.base.Strings;
import static com.google.common.base.Preconditions.checkArgument;

import com.lew.jlight.web.dao.UserRoleDao;
import com.lew.jlight.web.entity.UserRole;
import com.lew.jlight.web.service.UserRoleService;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;

@Service
public class UserRoleServiceImpl  implements UserRoleService {

    @Resource
    private UserRoleDao userRoleDao;

    @Override
    public List<UserRole> getListByUserId(String userId) {
        return userRoleDao.find("getListByUserId",userId);
    }

    @Override
    public List<String> getRoleIdsByUserId(String userId){
        return userRoleDao.findColumn( "getRoleIdsByUserId", String.class, userId );
    }

    @Override
    public void add(String[] roleIds,String userId) {
        checkArgument(!Strings.isNullOrEmpty(userId),"用户编号不能为空");
        userRoleDao.delete("deleteByUserId",userId);
        if(!Objects.isNull(roleIds)&& roleIds.length>0){
            for(String roleId : roleIds){
                UserRole userRole = new UserRole();
                userRole.setUserId(userId);
                userRole.setRoleId(roleId);
                userRoleDao.save(userRole);
            }
        }else{
            userRoleDao.delete("deleteByUserId",userId);
        }
    }
}
