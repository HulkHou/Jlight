package com.lew.jlight.web.service;


import com.lew.jlight.mybatis.ParamFilter;
import com.lew.jlight.web.entity.Dict;
import com.lew.jlight.web.entity.pojo.JSTree;

import java.util.List;

public interface DictService{

	void add(Dict dict);

	List<Dict> getList(ParamFilter queryFilter);

	void update(Dict dict);

	List<JSTree> getTree();

	List<Dict> getListByParentId(String parentId);

	List<Dict> getCatagory();

	void delete(List<String> id);

	Dict getById(String id);

}
