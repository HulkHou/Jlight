package com.lew.jlight.web.service.impl;

import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.stereotype.Service;
import static com.google.common.base.Preconditions.checkArgument;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.lew.jlight.web.dao.UserDao;
import com.lew.jlight.web.entity.User;
import com.lew.jlight.web.service.LoginService;

@Service
public class LoginServiceImpl implements LoginService {

    @Resource
    private UserDao userDao;

    @Override
    public User doLogin(String account, String clientIp) {
        checkArgument(!Strings.isNullOrEmpty(account),"帐号不能为空");
        String accountToUse = account.toUpperCase();
        User user = userDao.findUnique("getByAccount", accountToUse);
        if(user==null){
            //帐号不存在
            throw new UnknownAccountException();
        }
        if(user.getIsLock()){
            //帐号被锁定
            throw new LockedAccountException();
        }
        String userId = user.getUserId();
        Map<String,Object> updateParam =Maps.newHashMap();
        updateParam.put("loginTime", new Date());
        updateParam.put("loginIp", clientIp);
        updateParam.put("errorCount", 0);
        updateParam.put("userId", userId);
        userDao.update("updateLoginInfo", updateParam);
        return user;
    }
}
