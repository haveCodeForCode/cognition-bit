package com.cognition.bit.system.service.impl;


import com.cognition.bit.system.domain.SysMenu;
import com.cognition.bit.system.service.MenuService;
import com.cognition.bit.system.persistence.Tree;
import com.cognition.bit.common.until.BuildTree;
import com.cognition.bit.common.until.Query;
import com.cognition.bit.system.dao.MenuDao;
import com.cognition.bit.system.dao.RoleMenuDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 菜单服务层
 * @author taoya
 */
@Service
public class MenuServiceImpl implements MenuService {

    private MenuDao menuDao;

    private RoleMenuDao roleMenuDao;

    @Autowired
    public void setMenuDao(MenuDao menuDao) {
        this.menuDao = menuDao;
    }
    @Autowired
    public void setRoleMenuDao(RoleMenuDao roleMenuDao) {
        this.roleMenuDao = roleMenuDao;
    }



    @Override
    public SysMenu get(Long id) {
        return menuDao.get(id);
    }

    @Override
    public SysMenu get(Map<String, Object> params) {
        return menuDao.getByEntity(params);
    }

    @Override
    public int count(Map<String, Object> map) {
        return menuDao.count(map);
    }

    /**
     * 根据对象更新
     * @param sysMenu
     * @return
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    @Override
    public int update(SysMenu sysMenu) {
        return menuDao.update(sysMenu);
    }

    /**
     * 根据条件查询
     * @param params
     * @return
     */
    @Override
    public List<SysMenu> findList(Map<String, Object> params) {
        List<SysMenu> sysMenuList = menuDao.findList(params);
        return sysMenuList;
    }

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    @Override
    public int delete(Long id) {
        return menuDao.remove(id);
    }

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    @Override
    public int save(SysMenu sysMenu) {
        sysMenu.preInsert();
        return menuDao.insert(sysMenu);
    }


    @Override
    public Tree<SysMenu> getTree() {
        List<Tree<SysMenu>> trees = new ArrayList<Tree<SysMenu>>();

        Map<String, Object> query = Query.withDelFlag();
        List<SysMenu> sysMenuDOS = menuDao.findList(query);
        for (SysMenu sysSysMenuDO : sysMenuDOS) {
            Tree<SysMenu> tree = new Tree<SysMenu>();
            tree.setId(sysSysMenuDO.getId().toString());
            tree.setParentId(sysSysMenuDO.getParentId().toString());
            tree.setText(sysSysMenuDO.getName());
            trees.add(tree);
        }
        // 默认顶级菜单为０，根据数据库实际情况调整
        return BuildTree.build(trees);
    }

    /**
     * 获取一用户已选数
     * 逻辑：先取出除了父级的所有菜单的状态，
     *      在将所有菜单取出循环对比，赋值子菜单状态，回传树
     * @param id
     * @return
     */
    @Override
    public Tree<SysMenu> getTree(String id) {
        // 根据roleId查询权限
        //去除父级菜单
        Map<String,Object> query = Query.withDelFlag();
        List<SysMenu> sysMenus = menuDao.findList(query);

        List<Long> menuIds = roleMenuDao.listMenuIdByRoleId(Long.parseLong(id));
        for (SysMenu sysMenu : sysMenus) {
            menuIds.remove(sysMenu.getParentId());
        }
        List<Tree<SysMenu>> trees = new ArrayList<Tree<SysMenu>>();
        for (SysMenu sysSysMenuDO : sysMenus) {
            Tree<SysMenu> tree = new Tree<SysMenu>();
            tree.setId(sysSysMenuDO.getId().toString());
            tree.setParentId(sysSysMenuDO.getParentId().toString());
            tree.setText(sysSysMenuDO.getName());
            Map<String, Object> state = new HashMap<>(16);
            Long menuId = sysSysMenuDO.getId();
            if (menuIds.contains(menuId)) {
                state.put("selected", true);
            } else {
                state.put("selected", false);
            }
            tree.setState(state);
            trees.add(tree);
        }
        // 默认顶级菜单为０，根据数据库实际情况调整
        Tree<SysMenu> t = BuildTree.build(trees);
        return t;
    }

    @Override
    public Set<String> menuListPerms(Long userId) {
        List<String> perms = menuDao.listUserPerms(userId);
        Set<String> permsSet = new HashSet<>();
        for (String perm : perms) {
            if (StringUtils.isNotBlank(perm)) {
                permsSet.addAll(Arrays.asList(perm.trim().split(",")));
            }
        }
        return permsSet;
    }

    @Override
    public List<Tree<SysMenu>> listMenuTree(Long id) {
        List<SysMenu> sysMenuDOS = menuDao.listMenuByUserId(id);
        List<Tree<SysMenu>> trees = treeFormation(sysMenuDOS);
        // 默认顶级菜单为０，根据数据库实际情况调整
        return BuildTree.buildList(trees, "0");
    }

    @Override
    public int batchDelete(Long[] menuIds) {
        return menuDao.batchRemove(menuIds);
    }

    /**
     * 形成树
     *
     * @param sysMenuList
     * @return
     */
    private List<Tree<SysMenu>> treeFormation(List<SysMenu> sysMenuList) {
        List<Tree<SysMenu>> trees = new ArrayList<Tree<SysMenu>>();
        for (SysMenu sysSysMenuDO : sysMenuList) {
            Tree<SysMenu> tree = new Tree<SysMenu>();
            tree.setId(sysSysMenuDO.getId().toString());
            tree.setParentId(sysSysMenuDO.getParentId().toString());
            tree.setText(sysSysMenuDO.getName());
            Map<String, Object> attributes = new HashMap<>(16);
            attributes.put("url", sysSysMenuDO.getUrl());
            attributes.put("icon", sysSysMenuDO.getIcon());
            tree.setAttributes(attributes);
            trees.add(tree);
        }
        return trees;
    }
}
