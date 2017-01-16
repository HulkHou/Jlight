package com.lew.jlight.web.service.impl;

import com.lew.jlight.mybatis.ParamFilter;
import com.lew.jlight.web.dao.LoginLogDao;
import com.lew.jlight.web.entity.LoginLog;
import com.lew.jlight.web.service.LoginLogService;

import org.springframework.stereotype.Service;

import java.util.List;

import javax.annotation.Resource;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@Service
public class LoginLogServiceImpl implements LoginLogService {

    @Resource
    private LoginLogDao loginLogDao;

    @Override
    public void add(LoginLog loginLog) {
        checkNotNull(loginLog,"登录日志不能为空");
        loginLogDao.save(loginLog);
    }

    @Override
    public void delete(String[] loginLogIds) {
        checkArgument(loginLogIds!=null&& loginLogIds.length>0,"登录日志编号不能为空");
        for(String loginLogId : loginLogIds){
            loginLogDao.delete("delete",loginLogId);
        }
    }

    @Override
    public List<LoginLog> getList(ParamFilter paramFilter) {
        return loginLogDao.find("getList", paramFilter.getParam(), paramFilter.getPage());
    }
}
