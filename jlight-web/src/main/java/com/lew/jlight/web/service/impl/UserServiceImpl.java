package com.lew.jlight.web.service.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import com.lew.jlight.core.IdGenerator;
import com.lew.jlight.core.util.DigestUtil;
import com.lew.jlight.core.util.RegexUtil;
import com.lew.jlight.mybatis.ParamFilter;
import com.lew.jlight.web.dao.UserDao;
import com.lew.jlight.web.dao.UserRoleDao;
import com.lew.jlight.web.entity.User;
import com.lew.jlight.web.service.UserService;
import com.lew.jlight.web.util.UserContextUtil;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@Service
public class UserServiceImpl  implements UserService {

    @Resource
    private UserDao userDao;

    @Resource
    private UserRoleDao userRoleDao;

    @Override
    public List getList(ParamFilter param) {
        return userDao.findMap("getList", param.getParam(), param.getPage());
    }

    @Override
    public void updateDefaultPwd(List<String> userIds) {
        checkArgument((userIds != null && userIds.size() > 0), "用户编号不能为空");
        for (String userId : userIds) {
            User user = userDao.findUnique("getByUserId", userId);
            checkNotNull(user, "用户不存在");
        }
        for (String userId : userIds) {
            String defaultPwd = DigestUtil.sha256().digest("123456");
            Map<String, String> paramMap = Maps.newHashMap();
            paramMap.put("defaultPwd", defaultPwd);
            paramMap.put("userId", userId);
            userDao.update("updateDefaultPwd", paramMap);
        }
    }

    @Override
    public void update(User user) {
        checkNotNull(user, "用户不能为空");
        checkArgument(!Strings.isNullOrEmpty(user.getAccount()), "帐号名不能为空");
        checkArgument(!Strings.isNullOrEmpty(user.getPassword()), "密码不能为空");
        checkArgument(!Strings.isNullOrEmpty(user.getMobile()), "手机号码不能为空");
        checkNotNull(user.getIsLock(), "帐号名不能为空");
        checkArgument(RegexUtil.isMobile(user.getMobile()), "手机号码格式不正确");
        if (!Strings.isNullOrEmpty(user.getEmail())) {
            checkArgument(RegexUtil.isEmail(user.getEmail()), "邮箱格式不正确");
        }
        User model = userDao.findUnique("getByUserId", user.getUserId());
        checkNotNull(model, "用户信息不存在");
        String oldPwd = user.getPassword();
        if (model.getPassword().equals(oldPwd)) {
            user.setPassword(oldPwd);
        } else {
            user.setPassword(DigestUtil.sha256().digest(oldPwd));
        }
        userDao.update("updateUser", user);
    }

    @Override
    @Transactional
    public void add(User user) {
        checkNotNull(user, "用户不能为空");
        checkArgument(!Strings.isNullOrEmpty(user.getAccount()), "帐号名不能为空");
        checkArgument(!Strings.isNullOrEmpty(user.getPassword()), "密码不能为空");
        checkArgument(!Strings.isNullOrEmpty(user.getMobile()), "手机号码不能为空");
        checkNotNull(user.getIsLock(), "帐号名不能为空");
        checkArgument(RegexUtil.isMobile(user.getMobile()), "手机号码格式不正确");
        if (!Strings.isNullOrEmpty(user.getEmail())) {
            checkArgument(RegexUtil.isEmail(user.getEmail()), "邮箱格式不正确");
        }

        String account = user.getAccount();
        User model = userDao.findUnique("getByAccount", account);
        checkArgument(model == null, "用户已存在");
        String password = DigestUtil.sha256().digest(user.getPassword());
        user.setErrorCount(BigInteger.ZERO.intValue());
        String userId = IdGenerator.getInstance().nextId();
        user.setUserId(userId);
        user.setPassword(password);
        userDao.save(user);
    }

    @Override
    public void updatePwd(String originPwd, String confirmPwd, String newPwd) {
        checkArgument(!Strings.isNullOrEmpty(originPwd), "原密码不能为空");
        checkArgument(!Strings.isNullOrEmpty(confirmPwd), "确认密码不能为空");
        checkArgument(!Strings.isNullOrEmpty(newPwd), "新密码不能为空");
        checkArgument(confirmPwd.equals(newPwd), "新密码与确认密码不一致");
        String userId = UserContextUtil.getUserId();
        User user = userDao.findUnique("getByUserId", userId);
        checkNotNull(user, "用户对象不存在");
        checkArgument(user.getPassword().equals(DigestUtil.sha256().digest(originPwd)), "原密码不正确");

        String newPassword = DigestUtil.sha256().digest(confirmPwd);
        Map<String, String> paramMap = Maps.newHashMap();
        paramMap.put("newPassword", newPassword);
        paramMap.put("userId", userId);
        userDao.update("updatePwd", paramMap);
    }

    @Override
    public void delete(List<String> userIds) {
        checkArgument((userIds != null && userIds.size() > 0), "用户编号不能为空");
        for (String userId : userIds) {
            userDao.delete("deleteByUserId", userId);
            userRoleDao.delete("deleteByUserId", userId);
        }
    }

    @Override
    public Map getDetail(String userId) {
        checkArgument(!Strings.isNullOrEmpty(userId), "用户编号不能为空");
        Map<String, Object> resultMap = Maps.newHashMap();
        Map userMap = userDao.findOneColumn("getUserDetail", Map.class, userId);
        checkNotNull(userMap, "用户对象不存在");
        resultMap.put("user", userMap);
        return resultMap;
    }

    @Override
    public User getByUserId(String userId) {
        checkArgument(!Strings.isNullOrEmpty(userId), "用户编号不能为空");
        return userDao.findUnique("getByUserId", userId);
    }

    @Override
    public List<String> getPermission(String account) {
        return userDao.findColumn("getPermission", String.class, account);
    }

}
