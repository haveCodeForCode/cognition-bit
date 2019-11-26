package com.cognition.bit.system.service;

import com.cognition.bit.system.domain.SysMenu;
import com.cognition.bit.system.persistence.Tree;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 菜单业务层
 * @author taoya
 */
public interface MenuService {

	/**
	 * 获取菜单并形成树列
	 *
	 * @return
	 */
	Tree<SysMenu> getTree();

	/**
	 * 获取id对应的树列
	 *
	 * @param id
	 * @return
	 */
	Tree<SysMenu> getTree(String id);

	/**
	 * 根据用户id查询对应的数据
	 *
	 * @param id
	 * @return
	 */
	List<Tree<SysMenu>> listMenuTree(Long id);

	/**
	 * 根据用户ID
	 * @param userId
	 * @return
	 */
	Set<String> menuListPerms(Long userId);

//*************************************************

	/**
	 * 根据id查询菜单
	 * @param id
	 * @return
	 */
	SysMenu get(Long id);

	/**
	 * 根据条件查询菜单
	 * @param params
	 * @return
	 */
	SysMenu get(Map<String, Object> params);

	/**
	 * 根据条件查询
	 * @param params
	 * @return
	 */
	List<SysMenu> findList(Map<String, Object> params);

	/**
	 * 根据条件统计
	 * @param map
	 * @return
	 */
	int count(Map<String, Object> map);

	/**
	 * 根据entity更新菜单
	 * @param sysMenu
	 * @return
	 */
	int update(SysMenu sysMenu);

	/**
	 * 保存entity
	 * @param sysMenu
	 * @return
	 */
	int save(SysMenu sysMenu);

	/**
	 * 根据id移除
	 * @param id
	 * @return
	 */
	int delete(Long id);

	/**
	 * 批量删除
	 * @param menuIds
	 * @return
	 */
	int batchDelete(Long[] menuIds);

}
