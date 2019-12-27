package com.cognition.bit.system.service.impl;


import com.cognition.bit.common.config.Constant;
import com.cognition.bit.common.until.BuildTree;
import com.cognition.bit.common.until.Query;
import com.cognition.bit.common.until.StringUtils;
import com.cognition.bit.system.dao.*;
import com.cognition.bit.system.domain.*;
import com.cognition.bit.system.persistence.Tree;
import com.cognition.bit.system.service.DeptService;
import com.cognition.bit.system.service.UserService;
import com.cognition.bit.system.vo.SysUserVo;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author taoya
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);


    private UserDao userDao;

    private RoleDao roleDao;

    private UserRoleDao userRoleDao;

    private UserInfoDao userInfoDao;

    private DeptService deptService;

    private MenuDao menuDao;


//    @Autowired
//    private FileService sysFileService;
//    @Autowired
//    private BootdoConfig bootdoConfig;

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Autowired
    public void setUserRoleDao(UserRoleDao userRoleDao) {
        this.userRoleDao = userRoleDao;
    }

    @Autowired
    public void setRoleDao(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Autowired
    public void setDeptService(DeptService deptService) {
        this.deptService = deptService;
    }

    @Autowired
    public void setUserInfoDao(UserInfoDao userInfoDao) {
        this.userInfoDao = userInfoDao;
    }

    @Autowired
    public void setMenuDao(MenuDao menuDao) {
        this.menuDao = menuDao;
    }

    /**
     * 根据传入信息查询用户
     *
     * @param loginInfo
     * @return
     */
    @Override
    public SysUser getWihtLogininfo(String loginInfo) {
        return userDao.getWihtLogininfo(loginInfo);
    }

    /***
     * 声明用户相关所有对象
     * @param userId
     * @return
     */
    @Cacheable(value = "zero", key = "targetClass + methodName +#p0")
    @Override
    public SysUserVo getbyUserId(Long userId) {
        //声明用户相关关系变量
        SysUserVo sysUserVo = new SysUserVo();
        //查找存放用户
        Map<String, Object> userQuery = Query.withDelFlag();
        userQuery.put("id", userId);
        sysUserVo.setUserId(userId);
        SysUser sysUser = userDao.getByEntity(userQuery);
        BeanUtils.copyProperties(sysUser,sysUserVo);
        //用户信息
        Map<String, Object> query = Query.withDelFlag();
        query.put("userId", userId);
        SysUserInfo sysUserInfo = userInfoDao.getByEntity(query);
        BeanUtils.copyProperties(sysUserInfo,sysUserVo);
        //用户角色
        List<SysRole> sysRoles = roleDao.findWithUserId(query);
        sysUserVo.setSysRoles(sysRoles);
        //用户菜单
        List<SysMenu> sysMenuList = menuDao.listMenuByUserId(userId);
        sysUserVo.setSysMenus(sysMenuList);
        return sysUserVo;
    }

    @Override
    public SysUser get(Long id) {
        return userDao.get(id);
    }

    @Override
    public SysUser get(Map<String, Object> params) {
        return userDao.getByEntity(params);
    }

    @Override
    public List<SysUser> list(Map<String, Object> map) {
        String deptId = map.get("deptId").toString();
        if (StringUtils.isNotBlank(deptId)) {
            long deptIdL = Long.parseLong(deptId);
            List<Long> childIds = deptService.listChildrenIds(deptIdL);
            childIds.add(deptIdL);
            map.put("deptId", null);
            map.put("deptIds", childIds);
        }
        return userDao.findList(map);
    }

    @Override
    public int count(Map<String, Object> map) {
        return userDao.count(map);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int save(SysUser sysUser) {
        //判断是否传入ID为空，若为空则补全ID
        if (sysUser.getId() == null) {
            sysUser.preInsert();
        }
        int insert = userDao.insert(sysUser);
        batchModifyRoles(sysUser);
        return insert;
    }

    @Override
    public int update(SysUser sysUser) {
        int update = userDao.update(sysUser);
        batchModifyRoles(sysUser);
        return update;
    }

    /**
     * 批量修改用户角色信息
     *
     * @param sysUser 用户对象
     */
    private void batchModifyRoles(SysUser sysUser) {
        try {
            //获取用户ID
            Long userId = sysUser.getId();
            //根据ID获取角色相关信息
            SysUserVo sysUserVo = getbyUserId(userId);
            //获取角色，若没有付初始
            List<SysRole> sysRoles;
            if (sysUserVo.getSysRoles().size() < Constant.INT_ONE) {
                //为空则为新用户，放入默认角色
                Map<String, Object> params = Query.withDelFlag();
                params.put("id", "339780111572140032");
                sysRoles = roleDao.findList(params);
            } else {
                //获取需要存放角色列
                sysRoles = sysUserVo.getSysRoles();
                //根据用户ID删除中间表
                userRoleDao.removeByUserId(userId);
            }
            //角色信息权限修改方法
            List<SysUserRole> sysUserRoleList = new ArrayList<>();
            for (SysRole sysRole : sysRoles) {
                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setUserId(userId);
                sysUserRole.setRoleId(sysRole.getId());
                sysUserRoleList.add(sysUserRole);
            }
            if (sysUserRoleList.size() > 0) {
                //批量插入角色
                userRoleDao.batchSave(sysUserRoleList);
            }

            //放入用户角色信息
            if (StringUtils.isNotEmpty(sysUserVo.getNickName())) {
                SysUserInfo sysUserInfo = new SysUserInfo();
                sysUserInfo.setUserId(userId);
                userInfoDao.insert(sysUserInfo);
            }
        } catch (Exception e) {
            e.fillInStackTrace();
            throw e;
        }
    }

    //    @CacheEvict(value = "user")
    @Override
    public int delete(Long userId) {
        userRoleDao.removeByUserId(userId);
        return userDao.remove(userId);
    }

    @Override
    public boolean exit(Map<String, Object> params) {
        boolean exit;
        exit = userDao.findList(params).size() > 0;
        return exit;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int batchDelete(Long[] userIds) {
        int count = userDao.batchRemove(userIds);
        userRoleDao.batchRemoveByUserId(userIds);
        return count;
    }


    @Override
    public Tree<SysDept> getTree() {
        List<Tree<SysDept>> trees = new ArrayList<Tree<SysDept>>();
        List<SysDept> sysDepts = deptService.findList(new HashMap<String, Object>(16));
        String[] pDepts = deptService.listParentDept();
        String[] uDepts = deptService.listAllDept();
        String[] allDepts = (String[]) ArrayUtils.addAll(pDepts, uDepts);
        for (SysDept sysDept : sysDepts) {
            if (!ArrayUtils.contains(allDepts, sysDept.getId())) {
                continue;
            }
            Tree<SysDept> tree = new Tree<SysDept>();
            tree.setId(sysDept.getId().toString());
            tree.setParentId(sysDept.getParentId().toString());
            tree.setText(sysDept.getName());
            Map<String, Object> state = new HashMap<>(16);
            state.put("opened", true);
            state.put("mType", "dept");
            tree.setState(state);
            trees.add(tree);
        }
        List<SysUser> sysUsers = userDao.findList(new HashMap<String, Object>(16));
        for (SysUser sysUser : sysUsers) {
            Tree<SysDept> tree = new Tree<SysDept>();
            tree.setId(sysUser.getId().toString());
            tree.setParentId(sysUser.getDeptId().toString());
            Map<String, Object> state = new HashMap<>(16);
            state.put("opened", true);
            state.put("mType", "user");
            tree.setState(state);
            trees.add(tree);
        }
        // 默认顶级菜单为０，根据数据库实际情况调整
        Tree<SysDept> deptTree = BuildTree.build(trees);
        return deptTree;
    }

    @Override
    public int updatePersonal(SysUser sysUser) {
        return userDao.update(sysUser);
    }


    //    @Override
//    public int resetPwd(UserVO userVO, UserDO userDO) throws Exception {
//        if (Objects.equals(userVO.getUserDO().getUserId(), userDO.getUserId())) {
//            if (Objects.equals(Md5Utils.encrypt(userDO.getUsername(), userVO.getPwdOld()), userDO.getPassword())) {
//                userDO.setPassword(Md5Utils.encrypt(userDO.getUsername(), userVO.getPwdNew()));
//                return userMapper.update(userDO);
//            } else {
//                throw new Exception("输入的旧密码有误！");
//            }
//        } else {
//            throw new Exception("你修改的不是你登录的账号！");
//        }
//    }

//    @Override
//    public int adminResetPwd(UserVO userVO) throws Exception {
//        UserDO userDO = get(userVO.getUserDO().getUserId());
//        if ("admin".equals(userDO.getUsername())) {
//            throw new Exception("超级管理员的账号不允许直接重置！");
//        }
//        userDO.setPassword(Md5Utils.encrypt(userDO.getUsername(), userVO.getPwdNew()));
//        return userMapper.update(userDO);
//
//
//    }

//    @Override
//    public Map<String, Object> updatePersonalImg(MultipartFile file, String avatar_data, Long userId) throws Exception {
//        return null;
//    }

//    @Override
//    public Map<String, Object> updatePersonalImg(MultipartFile file, String avatar_data, Long userId) throws Exception {
//        String fileName = file.getOriginalFilename();
//        fileName = FileUtil.renameToUUID(fileName);
//        FileDO sysFile = new FileDO(FileType.fileType(fileName), "/files/" + fileName, new Date());
//        //获取图片后缀
//        String prefix = fileName.substring((fileName.lastIndexOf(".") + 1));
//        String[] str = avatar_data.split(",");
//        //获取截取的x坐标
//        int x = (int) Math.floor(Double.parseDouble(str[0].split(":")[1]));
//        //获取截取的y坐标
//        int y = (int) Math.floor(Double.parseDouble(str[1].split(":")[1]));
//        //获取截取的高度
//        int h = (int) Math.floor(Double.parseDouble(str[2].split(":")[1]));
//        //获取截取的宽度
//        int w = (int) Math.floor(Double.parseDouble(str[3].split(":")[1]));
//        //获取旋转的角度
//        int r = Integer.parseInt(str[4].split(":")[1].replaceAll("}", ""));
//        try {
//            BufferedImage cutImage = ImageUtils.cutImage(file, x, y, w, h, prefix);
//            BufferedImage rotateImage = ImageUtils.rotateImage(cutImage, r);
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            boolean flag = ImageIO.write(rotateImage, prefix, out);
//            //转换后存入数据库
//            byte[] b = out.toByteArray();
//            FileUtil.uploadFile(b, bootdoConfig.getUploadPath(), fileName);
//        } catch (Exception e) {
//            throw new Exception("图片裁剪错误！！");
//        }
//        Map<String, Object> result = new HashMap<>();
//        if (sysFileService.save(sysFile) > 0) {
//            UserDO userDO = new UserDO();
//            userDO.setUserId(userId);
//            userDO.setPicId(sysFile.getId());
//            if (userMapper.update(userDO) > 0) {
//                result.put("url", sysFile.getUrl());
//            }
//        }
//        return result;
//    }

}
