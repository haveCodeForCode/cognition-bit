package com.cognition.bit.system.dao;

import com.cognition.bit.system.entity.SysRoleMenu;
import com.cognition.bit.system.persistence.BaseDao;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 王睿
 * @version 2019/2/19
 */
@Mapper
@Repository("RoleMenuDao")
public interface RoleMenuDao extends BaseDao<SysRoleMenu> {
    /**
     * 根据role获取菜单列
     * @param roleId 角色ID
     * @return 菜单ID序列
     */
    List<Long> listMenuIdByRoleId(Long roleId);

    /**
     * 根据role删除菜单列
     * @param roleId 角色ID
     * @return 删除的数量
     */
    int removeByRoleId(Long roleId);

    /**
     * 根据MenuId批量删除
     * @param menuId  菜单ID
     * @return 删除的数量
     */
    int removeByMenuId(Long menuId);

    /**
     * 根据RoleMenu对象列批量插入
     * @param list RoleMenu列
     * @return 存入数量
     */
    int batchSave(List<SysRoleMenu> list);
}
