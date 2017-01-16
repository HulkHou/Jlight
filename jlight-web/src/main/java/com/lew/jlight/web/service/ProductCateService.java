package com.lew.jlight.web.service;


import com.lew.jlight.mybatis.ParamFilter;
import com.lew.jlight.web.entity.ProductCate;
import com.lew.jlight.web.entity.User;

import java.util.List;
import java.util.Map;

public interface ProductCateService {

    List getList(ParamFilter param);
//
//    void updateDefaultPwd(List<String> userIds);
//
    void update(ProductCate productCate);
//
    void add(ProductCate productCate);
//
//    void updatePwd(String originPwd, String confirmPwd, String newPwd);
//
    void delete(List<String> cateIds);
//
    Map getDetail(String cateId);
//
//    User getByUserId(String userId);
//
//    List<String> getPermission(String account);
}
