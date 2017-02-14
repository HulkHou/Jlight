package com.lew.jlight.web.service;


import com.lew.jlight.mybatis.ParamFilter;
import com.lew.jlight.web.entity.Product;

import java.util.List;
import java.util.Map;

public interface ProductService {

    List getList(ParamFilter param);

    void update(Product product);

    void add(Product product);

    void delete(List<String> productIds);

    Map getDetail(String productId);

}
