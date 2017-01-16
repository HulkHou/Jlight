package com.lew.jlight.web.service.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import com.lew.jlight.core.IdGenerator;
import com.lew.jlight.core.Response;
import com.lew.jlight.core.util.BeanUtil;
import com.lew.jlight.mybatis.ParamFilter;
import com.lew.jlight.web.dao.MenuDao;
import com.lew.jlight.web.dao.RoleDao;
import com.lew.jlight.web.dao.RoleMenuDao;
import com.lew.jlight.web.entity.Menu;
import com.lew.jlight.web.entity.Role;
import com.lew.jlight.web.entity.RoleMenu;
import com.lew.jlight.web.entity.pojo.MenuTitle;
import com.lew.jlight.web.service.MenuService;
import com.lew.jlight.web.util.Constants;
import com.lew.jlight.web.util.ResourceTreeUtil;

import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@Service
public class MenuServiceImpl implements MenuService {

    private static final Comparator<Menu> resourceComparator = (o1, o2) -> {
        int seq1 = o1.getSeq() != null ? o1.getSeq() : Integer.MAX_VALUE;
        int seq2 = o2.getSeq() != null ? o2.getSeq() : Integer.MAX_VALUE;
        return (seq1 < seq2 ? -1 : (seq1 == seq2 ? 0 : 1));
    };

    private static final Comparator<MenuTitle> titleComparator = (o1, o2) -> {
        int seq1 = o1.getSeq() != null ? o1.getSeq() : Integer.MAX_VALUE;
        int seq2 = o2.getSeq() != null ? o2.getSeq() : Integer.MAX_VALUE;
        return (seq1 < seq2 ? -1 : (seq1 == seq2 ? 0 : 1));
    };

    @Resource
    private MenuDao menuDao;

    @Resource
    private RoleDao roleDao;

    @Resource
    private RoleMenuDao roleMenuDao;

    @Override
    public void add(Menu menu) {
        checkNotNull(menu, "菜单信息不能为空");
        checkArgument(Constants.STATUS_SET.contains(menu.getType()), "菜单是否显示不正确");
        checkArgument(Constants.STATUS_SET.contains(menu.getIsShow()), "菜单类型不正确");
        if(menu.getType()==1){
            checkArgument(!Strings.isNullOrEmpty(menu.getUrl()), "菜单地址不能为空");
        }
        if (!Strings.isNullOrEmpty(menu.getParentId()) && !"0".equals(menu.getParentId())) {
            Menu parentMenu = this.detail(menu.getParentId());
            checkArgument(parentMenu!=null, "父菜单不存在");
            checkArgument(parentMenu.getType() == 0, "按钮不能添加子菜单");
            menu.setParentName(parentMenu.getName());
        }
        String menuId = IdGenerator.getInstance().nextId();
        menu.setMenuId(menuId);
        menuDao.save("add", menu);
    }

    @Override
    public void delete(List<String> menuIds) {
        checkArgument((menuIds != null && menuIds.size() > 0), "菜单编号不能为空");
        for (String menuId : menuIds) {
            menuDao.delete("deleteByMenuId", menuId);
            menuDao.delete("deleteByParentId", menuId);
        }
    }

    @Override
    public void update(Menu menu) {
        checkNotNull(menu, "菜单不能为空");
        checkArgument(!Strings.isNullOrEmpty(menu.getMenuId()), "菜单编号不能为空");
        checkArgument(!Strings.isNullOrEmpty(menu.getName()), "菜单名称不能为空");
        checkArgument(Constants.STATUS_SET.contains(menu.getType()), "菜单是否显示不正确");
        checkArgument(Constants.STATUS_SET.contains(menu.getIsShow()), "菜单类型不正确");
        if(menu.getType()==1){
            checkArgument(!Strings.isNullOrEmpty(menu.getUrl()), "菜单地址不能为空");
        }
        Menu model = menuDao.findUnique("getMenuById", menu.getMenuId());
        checkNotNull(model, "菜单不存在");
        menuDao.update("update", menu);
    }

    @Override
    public List<MenuTitle> getListByRoleId(String roleId) {
        checkArgument(!Strings.isNullOrEmpty(roleId), "角色编号不能为空");
        Role model = roleDao.findUnique("getRoleByRoleId", roleId);
        checkNotNull(model, "角色不存在");
        return this.loadResources(roleId);
    }

    @Override
    public List<Menu> getByParentId(String menuId) {
        List<Menu> menuList = Lists.newLinkedList();
        Menu parentMenu = menuDao.findUnique("getMenuById", menuId);
        if (parentMenu != null) {
            menuList.add(parentMenu);
        }
        List<Menu> subMenuList = menuDao.find("getMenuByParentId", menuId);
        menuList.addAll(subMenuList);
        return menuList;
    }

    private List<MenuTitle> loadResources(String roleId) {
        Map<String, List<MenuTitle>> roleResMap = Maps.newHashMap();
        // roleResMap = cacheManager.get( key, Map.class, cacheCallback );
        List<MenuTitle> list = roleResMap.get(roleId);
        if (list != null) {
            return list;
        }
        List<RoleMenu> roleMenuList = roleMenuDao.find("getListByRoleId", roleId);
        Map<String, List<Menu>> map = new HashMap<>();
        list = new ArrayList<>(8);
        for (RoleMenu roleMenu : roleMenuList) {
            Menu menu = menuDao.findUnique("getMenuById", roleMenu.getMenuId());
            if (menu == null || BigInteger.ONE.intValue() == menu.getType()) {
                continue;
            }
            String parentId = menu.getParentId();
            List<Menu> menuList = map.get(parentId);
            if (menuList == null) {
                menuList = new ArrayList<>(8);
                MenuTitle title = new MenuTitle();
                Menu parentMenu = menuDao.findUnique("getMenuById", parentId);
                if (parentMenu == null) {
                    boolean isEmpty = BeanUtil.isEmpty(map.get(menu.getMenuId()));
                    title.setName(menu.getName());
                    title.setSeq(menu.getSeq());
                    title.setMenuList(menuList);
                    title.setTitleId(menu.getMenuId());
                    title.setIcon(menu.getIcon());
                    if (isEmpty) {
                        list.add(title);
                        map.put(title.getTitleId(), menuList);
                    }
                    continue;
                }
                title.setMenuList(menuList);
                title.setName(parentMenu.getName());
                title.setIcon(parentMenu.getIcon());
                title.setSeq(parentMenu.getSeq());
                list.add(title);
                map.put(parentId, menuList);
            }
            menuList.add(menu);
        }

        for (List<Menu> resources : map.values()) {
            resources.sort(resourceComparator);
        }
        list.sort(titleComparator);
        roleResMap.put(roleId, list);
        return list;
    }

    @Override
    public List<Menu> getList(ParamFilter paramFilter) {
        return menuDao.find("getMenuList", paramFilter.getParam(), paramFilter.getPage());
    }

    @Override
    public Response getResTree(String roleId) {
        Response response = new Response();
        if (Strings.isNullOrEmpty(roleId)) {
            response.setCode(Response.INVALID_PARAM);
            response.setMsg("角色编号不能为空");
            return response;
        }
        Map<String, String> paramMap = Maps.newHashMap();
        paramMap.put("roleId", roleId);
        List resList = menuDao.findMap("getMenuTree", paramMap);
        response.setData(ResourceTreeUtil.generateJSTree(resList));
        return response;
    }

    @Override
    public Response getSelectResTree() {
        Response response = new Response();
        List<Map<String, Object>> parentList = menuDao.findMap("getMenuIdAndName", BigInteger.ZERO.toString());
        List<Map<String, Object>> resList = new LinkedList<>();
        for (Map<String, Object> resMap : parentList) {
            List<Menu> subRes = menuDao.find("getMenuByParentId", resMap.get("menuId"));
            Map<String, Object> subMap;
            resList.add(resMap);
            for (Menu menu : subRes) {
                subMap = Maps.newHashMap();
                String subName = menu.getName();
                subName = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + subName;
                subMap.put("resId", menu.getMenuId());
                subMap.put("name", subName);
                resList.add(subMap);
            }
        }
        response.setData(resList);
        return response;
    }

    @Override
    public Menu detail(String resId) {
        Response response = new Response();
        if (Strings.isNullOrEmpty(resId)) {
            response.setCode(Response.INVALID_PARAM);
            response.setMsg("资源编号不能为空");
        }
        return menuDao.findUnique("getMenuById", resId);
    }

    @SuppressWarnings("rawtypes")
	@Override
    public Response getTree() {
        Response response = new Response();
        List resList = menuDao.findMap("getAllMenuTree");
        response.setData(resList);
        return response;
    }
}
