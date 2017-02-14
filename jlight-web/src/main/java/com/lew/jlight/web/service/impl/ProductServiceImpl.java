package com.lew.jlight.web.service.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.lew.jlight.mybatis.ParamFilter;
import com.lew.jlight.web.dao.ProductDao;
import com.lew.jlight.web.entity.Product;
import com.lew.jlight.web.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@Service
public class ProductServiceImpl implements ProductService {

    @Resource
    private ProductDao productDao;

    @Override
    public List getList(ParamFilter param) {
        return productDao.findMap("getList", param.getParam(), param.getPage());
    }

    @Override
    @Transactional
    public void add(Product product) {
        checkNotNull(product, "商品信息不能为空");
        checkArgument(!Strings.isNullOrEmpty(product.getProductName()), "商品名称不能为空");
        productDao.save(product);
    }

    @Override
    public void update(Product product) {
        checkNotNull(product, "商品信息不能为空");
        checkArgument(!Strings.isNullOrEmpty(product.getProductName()), "商品名称不能为空");
        Product model = productDao.findUnique("getByProductId", product.getProductId());
        checkNotNull(model, "商品信息不存在");
        product.setUpdateTime(new Date());
        productDao.update("updateProduct", product);
    }

    @Override
    public Map getDetail(String productId) {
        checkArgument(!Strings.isNullOrEmpty(productId), "商品ID不能为空");
        Map<String, Object> resultMap = Maps.newHashMap();
        Map productMap = productDao.findOneColumn("getProductDetail", Map.class, productId);
        checkNotNull(productMap, "商品对象不存在");
        resultMap.put("product", productMap);
        return resultMap;
    }

    @Override
    public void delete(List<String> productIds) {
        checkArgument((productIds != null && productIds.size() > 0), "商品ID不能为空");
        for (String productId : productIds) {
            productDao.delete("deleteByProductId", productId);
        }
    }

}
