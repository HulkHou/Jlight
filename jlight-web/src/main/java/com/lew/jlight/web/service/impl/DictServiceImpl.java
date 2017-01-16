package com.lew.jlight.web.service.impl;

import com.google.common.base.Strings;

import com.lew.jlight.mybatis.ParamFilter;
import com.lew.jlight.web.dao.DictDao;
import com.lew.jlight.web.entity.Dict;
import com.lew.jlight.web.entity.pojo.JSTree;
import com.lew.jlight.web.service.DictService;

import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@Service
public class DictServiceImpl implements DictService {

    @Resource
    private DictDao dictDao;

    @Override
    public void add(Dict dict) {
        checkNotNull(dict,"字典信息不能为空");
        checkArgument(!Strings.isNullOrEmpty(dict.getCode()),"字典Code不能为空");
        checkArgument(!Strings.isNullOrEmpty(dict.getName()),"字典名称不能为空");
        checkArgument(!Strings.isNullOrEmpty(dict.getIsCatagory()),"字典类型不能为空");
        if (dict.getParentId() == null) {
            dict.setParentId("#");
        }
        dictDao.save(dict);
    }

    @Override
    public List<Dict> getList(ParamFilter queryFilter) {
        return dictDao.find("getList", queryFilter.getParam(), queryFilter.getPage());
    }

    @Override
    public void update(Dict dict) {
        dictDao.update(dict);
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public List<JSTree> getTree() {
        return dictDao.findMap("getTree");
    }

    @Override
    public List<Dict> getListByParentId(String parentId) {
        List<Dict> dictList = new LinkedList<>();
        List<Dict> subList = dictDao.find("getListByParentId",parentId);
        Dict dict = dictDao.findUnique("getById",parentId);
        dictList.add(dict);
        if(subList!=null && subList.size()>0){
            dictList.addAll(subList);
        }
        return dictList;
    }

    @Override
    public List<Dict> getCatagory() {
        return dictDao.find("getCatagory");
    }

    @Override
    public void delete(List<String> ids) {
        for (String id : ids) {
            dictDao.delete("delete", id);
        }
    }

	@Override
	public Dict getById(String id) {
		return dictDao.findUnique("getById",id);
	}


}
