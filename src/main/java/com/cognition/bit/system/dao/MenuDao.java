package com.cognition.bit.system.dao;


import com.cognition.bit.system.entity.SysMenu;
import com.cognition.bit.system.persistence.BaseDao;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 菜单管理
 *
 * @author LineInkBook
 */
@Mapper
@Repository("MenuDao")
public interface MenuDao extends BaseDao<SysMenu> {

    /**
     * 根据用户id查询用户下的菜单
     *
     * @param id
     * @return
     */
    List<SysMenu> listMenuByUserId(Long id);

    /**
     * 根据用户id查询用户下的权限
     *
     * @param id
     * @return
     */
    List<String> listUserPerms(Long id);
}
