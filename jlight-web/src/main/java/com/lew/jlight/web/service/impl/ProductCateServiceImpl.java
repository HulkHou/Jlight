package com.lew.jlight.web.service.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.lew.jlight.core.IdGenerator;
import com.lew.jlight.core.util.DigestUtil;
import com.lew.jlight.core.util.RegexUtil;
import com.lew.jlight.mybatis.ParamFilter;
import com.lew.jlight.web.dao.ProductCateDao;
import com.lew.jlight.web.dao.UserDao;
import com.lew.jlight.web.dao.UserRoleDao;
import com.lew.jlight.web.entity.ProductCate;
import com.lew.jlight.web.entity.User;
import com.lew.jlight.web.service.ProductCateService;
import com.lew.jlight.web.service.UserService;
import com.lew.jlight.web.util.UserContextUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@Service
public class ProductCateServiceImpl implements ProductCateService {

    @Resource
    private ProductCateDao productCateDao;

    @Override
    public List getList(ParamFilter param) {
        return productCateDao.findMap("getList", param.getParam(), param.getPage());
    }

    @Override
    @Transactional
    public void add(ProductCate productCate) {
        checkNotNull(productCate, "商品分类不能为空");
        checkArgument(!Strings.isNullOrEmpty(productCate.getCateName()), "分类名称不能为空");
        checkArgument(!Strings.isNullOrEmpty(productCate.getIsShow()), "是否显示不能为空");
        checkArgument(!Strings.isNullOrEmpty(productCate.getSortNum()), "排序ID不能为空");
        checkArgument(!Strings.isNullOrEmpty(productCate.getStatus()), "状态不能为空");
        productCateDao.save(productCate);
    }

    @Override
    public void update(ProductCate productCate) {
        checkNotNull(productCate, "商品分类不能为空");
        checkArgument(!Strings.isNullOrEmpty(productCate.getCateName()), "分类名称不能为空");
        ProductCate model = productCateDao.findUnique("getByCateId", productCate.getCateId());
        checkNotNull(model, "商品分类信息不存在");
        productCate.setUpdateTime(new Date());
        productCateDao.update("updateProductCate", productCate);
    }

    @Override
    public Map getDetail(String cateId) {
        checkArgument(!Strings.isNullOrEmpty(cateId), "分类编号不能为空");
        Map<String, Object> resultMap = Maps.newHashMap();
        Map productCateMap = productCateDao.findOneColumn("getProductCateDetail", Map.class, cateId);
        checkNotNull(productCateMap, "分类对象不存在");
        resultMap.put("productCate", productCateMap);
        return resultMap;
    }

    @Override
    public List getProductCate() {
        return productCateDao.findMap("getProductCate");
    }

    @Override
    public void delete(List<String> cateIds) {
        checkArgument((cateIds != null && cateIds.size() > 0), "商品分类编号不能为空");
        for (String cateId : cateIds) {
            productCateDao.delete("deleteByCateId", cateId);
        }
    }
}
