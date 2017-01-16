package com.lew.jlight.mybatis;

import com.lew.jlight.core.page.Page;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class ParamFilter {

    private Page page;

    private Map<String,Object> param;

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public Map<String, Object> getParam() {
        return param;
    }

    public void setParam(Map<String, Object> param) {
        this.param = param;
    }
}
