package com.root.cognition.system.service.impl;


import com.root.cognition.common.config.Constant;
import com.root.cognition.common.until.BuildTree;
import com.root.cognition.common.until.Query;
import com.root.cognition.common.until.StringUtils;
import com.root.cognition.common.until.codegenerate.SnowFlake;
import com.root.cognition.system.dao.*;
import com.root.cognition.system.entity.*;
import com.root.cognition.system.persistence.Tree;
import com.root.cognition.system.service.DeptService;
import com.root.cognition.system.service.UserService;
import com.root.cognition.system.vo.UserVo;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    public User getWihtLogininfo(String loginInfo) {
        return userDao.getWihtLogininfo(loginInfo);
    }

    /***
     * 声明用户相关所有对象
     * @param userId
     * @return
     */
    @Cacheable(value = "zero", key = "targetClass + methodName +#p0")
    @Override
    public UserVo getbyUserId(Long userId) {
        //声明用户相关关系变量
        UserVo userVo = new UserVo();
        //查找存放用户
        Map<String, Object> userQuery = Query.withDelFlag();
        userQuery.put("id", userId);
        User user = userDao.getByEntity(userQuery);
        userVo.setUser(user);
        //用户信息
        Map<String, Object> query = Query.withDelFlag();
        query.put("userId", userId);
        UserInfo userInfo = userInfoDao.getByEntity(query);
        userVo.setUserInfo(userInfo);
        //用户角色
        List<Role> roles = roleDao.findWithUserId(query);
        userVo.setRoles(roles);
        //用户菜单
        List<Menu> menuList = menuDao.listMenuByUserId(userId);
        userVo.setMenus(menuList);
        return userVo;
    }

    @Override
    public User get(Long id) {
        return userDao.get(id);
    }

    @Override
    public User get(Map<String, Object> params) {
        return userDao.getByEntity(params);
    }

    @Override
    public List<User> list(Map<String, Object> map) {
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
    public int save(User user) {
        //判断是否传入ID为空，若为空则补全ID
        if (user.getId() == null) {
            user.preInsert();
        }
        int insert = userDao.insert(user);
        batchModifyRoles(user);
        return insert;
    }

    @Override
    public int update(User user) {
        int update = userDao.update(user);
        batchModifyRoles(user);
        return update;
    }

    /**
     * 批量修改用户角色信息
     *
     * @param user 用户对象
     */
    private void batchModifyRoles(User user) {
        try {
            //获取用户ID
            Long userId = user.getId();
            //根据ID获取角色相关信息
            UserVo userVo = getbyUserId(userId);
            //获取角色，若没有付初始
            List<Role> roles;
            if (userVo.getRoles().size() < Constant.INT_ONE) {
                //为空则为新用户，放入默认角色
                Map<String, Object> params = Query.withDelFlag();
                params.put("id", "339780111572140032");
                roles = roleDao.findList(params);
            } else {
                //获取需要存放角色列
                roles = userVo.getRoles();
                //根据用户ID删除中间表
                userRoleDao.removeByUserId(userId);
            }
            //角色信息权限修改方法
            List<UserRole> userRoleList = new ArrayList<>();
            for (Role role : roles) {
                UserRole userRole = new UserRole();
                userRole.setId(SnowFlake.createSFid());
                userRole.setUserId(userId);
                userRole.setRoleId(role.getId());
                userRoleList.add(userRole);
            }
            if (userRoleList.size() > 0) {
                //批量插入角色
                userRoleDao.batchSave(userRoleList);
            }

            //放入用户角色信息
            if (userVo.getUserInfo() == null) {
                UserInfo userInfo = new UserInfo();
                userInfo.setUserId(userId);
                userInfo.preInsert();
                userInfo.setUserName(user.getLoginName());
                userInfoDao.insert(userInfo);
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
    public Tree<Dept> getTree() {
        List<Tree<Dept>> trees = new ArrayList<Tree<Dept>>();
        List<Dept> depts = deptService.findList(new HashMap<String, Object>(16));
        String[] pDepts = deptService.listParentDept();
        String[] uDepts = deptService.listAllDept();
        String[] allDepts = (String[]) ArrayUtils.addAll(pDepts, uDepts);
        for (Dept dept : depts) {
            if (!ArrayUtils.contains(allDepts, dept.getId())) {
                continue;
            }
            Tree<Dept> tree = new Tree<Dept>();
            tree.setId(dept.getId().toString());
            tree.setParentId(dept.getParentId().toString());
            tree.setText(dept.getName());
            Map<String, Object> state = new HashMap<>(16);
            state.put("opened", true);
            state.put("mType", "dept");
            tree.setState(state);
            trees.add(tree);
        }
        List<User> users = userDao.findList(new HashMap<String, Object>(16));
        for (User user : users) {
            Tree<Dept> tree = new Tree<Dept>();
            tree.setId(user.getId().toString());
            tree.setParentId(user.getDeptId().toString());
            tree.setText(user.getLoginName());
            Map<String, Object> state = new HashMap<>(16);
            state.put("opened", true);
            state.put("mType", "user");
            tree.setState(state);
            trees.add(tree);
        }
        // 默认顶级菜单为０，根据数据库实际情况调整
        Tree<Dept> deptTree = BuildTree.build(trees);
        return deptTree;
    }

    @Override
    public int updatePersonal(User user) {
        return userDao.update(user);
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
