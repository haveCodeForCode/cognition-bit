package com.cognition.bit.system.service.impl;


import com.cognition.bit.system.domain.SysRole;
import com.cognition.bit.system.domain.SysRoleMenu;
import com.cognition.bit.system.service.RoleService;
import com.cognition.bit.common.config.Constant;
import com.cognition.bit.common.until.codegenerate.SnowFlake;
import com.cognition.bit.system.dao.RoleDao;
import com.cognition.bit.system.dao.RoleMenuDao;
import com.cognition.bit.system.dao.UserDao;
import com.cognition.bit.system.dao.UserRoleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
 * 角色业务层
 *
 * @author taoya
 */
@Service
public class RoleServiceImpl implements RoleService {

    public static final String ROLE_ALL_KEY = "\"role_all\"";

    public static final String DEMO_CACHE_NAME = "role";


    private RoleDao roleDao;

    private RoleMenuDao roleMenuDao;

    private UserDao userDao;

    private UserRoleDao userRoleDao;

    @Autowired
    public void setRoleDao(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Autowired
    public void setRoleMenuDao(RoleMenuDao roleMenuDao) {
        this.roleMenuDao = roleMenuDao;
    }

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Autowired
    public void setUserRoleDao(UserRoleDao userRoleDao) {
        this.userRoleDao = userRoleDao;
    }

    @Override
    public SysRole get(Map<String, Object> params) {
        return roleDao.getByEntity(params);
    }

    @Override
    public SysRole get(Long id) {
        return roleDao.get(id);
    }

    @Override
    public List<SysRole> findList(Map<String, Object> params) {
        return roleDao.findList(params);
    }

    @Override
    public int count(Map<String, Object> map) {
        return roleDao.count(map);
    }

    @Override
    public List<SysRole> list(Long userId) {
        List<Long> rolesIds = userRoleDao.listRoleId(userId);
        List<SysRole> sysRoles = roleDao.findList(new HashMap<>(16));
        for (SysRole sysRole : sysRoles) {
//            role.setRoleSign("false");
            for (Long roleId : rolesIds) {
                if (Objects.equals(sysRole.getId(), roleId)) {
//                    role.setRoleSign("true");
                    break;
                }
            }
        }
        return sysRoles;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int save(SysRole sysRole) {
        sysRole.preInsert();
        sysRole.setPermissions(Constant.STRING_ZERO);
        sysRole.setDataScope(Constant.STRING_ZERO);
        //插入
        int count = roleDao.insert(sysRole);
        //根据插入的角色批量修改角色菜单
        if (count>0) {
            List<Long> menuIds = sysRole.getMenuIds();
            List<SysRoleMenu> rms = new ArrayList<>();
            for (Long menuId : menuIds) {
                SysRoleMenu rmDo = new SysRoleMenu();
                rmDo.setId(SnowFlake.createSFid());
                rmDo.setRoleId(sysRole.getId());
                rmDo.setMenuId(menuId);
                rms.add(rmDo);
            }
            roleMenuDao.removeByRoleId(sysRole.getId());
            if (rms.size() > 0) {
                roleMenuDao.batchSave(rms);
            }
        }
        return count;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int delete(Long id) {
        int count = roleDao.remove(id);
        userRoleDao.removeByRoleId(id);
        roleMenuDao.removeByRoleId(id);
        return count;
    }

    @Override
    public int update(SysRole sysRole) {
        //更新角色对象
        int r = roleDao.update(sysRole);
        //获取角色中菜单列
        List<Long> menuIds = sysRole.getMenuIds();
        //获取角色id
        Long roleId = sysRole.getId();
        if (roleId == null) {
            return Constant.RETURN_STATUS_INFOBUG;
        }
        //移除所有角色id相关的菜单
        roleMenuDao.removeByRoleId(roleId);
        List<SysRoleMenu> sysRoleMenuList = new ArrayList<>();
        for (Long menuId : menuIds) {
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setId(SnowFlake.createSFid());
            sysRoleMenu.setRoleId(roleId);
            sysRoleMenu.setMenuId(menuId);
            sysRoleMenuList.add(sysRoleMenu);
        }
        if (sysRoleMenuList.size() > 0) {
             r = roleMenuDao.batchSave(sysRoleMenuList);
        }
        return r;
    }

    @Override
    public int batchDelect(Long[] ids) {
        return roleDao.batchRemove(ids);
    }


}
