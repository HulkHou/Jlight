package com.lew.jlight.web.controller;

import com.google.common.base.Preconditions;

import com.lew.jlight.core.Response;
import com.lew.jlight.core.page.Page;
import com.lew.jlight.mybatis.ParamFilter;
import com.lew.jlight.web.aop.annotaion.WebLogger;
import com.lew.jlight.web.entity.Dict;
import com.lew.jlight.web.service.DictService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import javax.annotation.Resource;

import static com.google.common.base.Preconditions.checkArgument;


@Controller
@RequestMapping("dict")
public class DictController {

    @Resource
    private DictService dictService;

    @GetMapping("list")
    public String list() {
        return "dictList";
    }

    @ResponseBody
    @PostMapping("list")
    @WebLogger("查询字典列表")
    public Response list(@RequestBody ParamFilter queryFilter) {
        List<Dict> list = dictService.getList(queryFilter);
        Page page = queryFilter.getPage();
        return new Response(list, page);
    }

    @ResponseBody
    @PostMapping("add")
    @WebLogger("添加字典")
    public Object add(@RequestBody Dict dict) {
        Preconditions.checkNotNull(dict, "不能为空");
        Response response = new Response();
        if (dict.getId()==null) {
        	dictService.add(dict);
        } else {
        	dictService.update(dict);
        }
        response.setMsg("添加成功");
        return response;
    }
    
    @ResponseBody
    @GetMapping("detail")
    @WebLogger("查询字典详细")
    public Response detail(String id) {
        Preconditions.checkNotNull(id, "不能为空");
        Dict dict = dictService.getById(id);
        return new Response(dict);
    }


	@ResponseBody
    @PostMapping("delete")
    @WebLogger("删除字典")
    public Response delete(@RequestBody List<String> ids) {
		checkArgument((ids != null && ids.size() > 0), "不能为空");
        dictService.delete(ids);
        return new Response();
    }

    @ResponseBody
    @GetMapping("getTree")
    public Object getTree(){
        return dictService.getTree();
    }
    @ResponseBody
    @GetMapping("getByParentId")
    public Response getByParentId(String parentId){
        List<Dict> dictList = dictService.getListByParentId(parentId);
        return new Response(dictList);
    }

    @ResponseBody
    @GetMapping("getCatagory")
    public Response getCatagory(){
    	List<Dict> list = dictService.getCatagory();
    	Response response = new Response();
    	response.setData(list);
    	return response;
    } 
}
