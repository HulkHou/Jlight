package com.lew.jlight.web.service;


import com.lew.jlight.core.Response;
import com.lew.jlight.mybatis.ParamFilter;
import com.lew.jlight.web.entity.Menu;
import com.lew.jlight.web.entity.pojo.MenuTitle;

import java.util.List;

public interface MenuService {

	void add(Menu menu);

	void delete(List<String> menuIds);

	void update(Menu menu);

	List<MenuTitle> getListByRoleId(String roleId);
	
	List<Menu> getList(ParamFilter param);

	List<Menu> getByParentId(String menuId);
	
	Response getResTree(String roleId);
	
	Response getSelectResTree();

	Menu detail(String menuId);

	Response getTree();

}
