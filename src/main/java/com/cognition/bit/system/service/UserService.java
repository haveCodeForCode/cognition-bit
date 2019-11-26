package com.cognition.bit.system.service;

import com.cognition.bit.system.domain.SysDept;
import com.cognition.bit.system.domain.SysUser;
import com.cognition.bit.system.persistence.Tree;
import com.cognition.bit.system.vo.SysUserVo;

import java.util.List;
import java.util.Map;

public interface UserService {

	/**
	 * 根据邮箱、手机号模糊查询
	 * @param loginInfo
	 * @return
	 */
	SysUser getWihtLogininfo(String loginInfo);


	/**
	 * 根据id查询用户
	 * @param userid
	 * @return
	 */
	SysUserVo getbyUserId(Long userid);

	/**
	 * 查询是否存在该用户
	 *
	 * @param params
	 * @return
	 */
	boolean exit(Map<String, Object> params);


	/**
	 * 根据部门获取用户树
	 *
	 * @return
	 */
	Tree<SysDept> getTree();

//	int resetPwd(UserVO userVO, UserDO userDO) throws Exception;
//
//	int adminResetPwd(UserVO userVO) throws Exception;


	/**
	 * 更新个人信息
	 *
	 * @param sysUserDO
	 * @return
	 */
	int updatePersonal(SysUser sysUserDO);

	/**
	 * 更新个人图片
	 *
	 * @param file        图片
	 * @param avatar_data 裁剪信息
	 * @param userId      用户ID
	 * @throws Exception
	 */
//	Map<String, Object> updatePersonalImg(MultipartFile file, String avatar_data, Long userId) throws Exception;

//************************************************


	/**
	 * 根据id查询
	 * @param id
	 * @return
	 */
	SysUser get (Long id);

	/**
	 * 根据条件查询
	 * @param params
	 * @return
	 */
	SysUser get(Map<String, Object> params);

	/**
	 * 根据条件查询
	 * @param map
	 * @return
	 */
	List<SysUser> list(Map<String, Object> map);

	/**
	 * 根据条件统计
	 * @param map
	 * @return
	 */
	int count(Map<String, Object> map);

	/**
	 * 根据entity保存
	 * @param sysUser
	 * @return
	 */
	int save(SysUser sysUser);

	/**
	 * 根据entity更新
	 * @param sysUser
	 * @return
	 */
	int update(SysUser sysUser);

	/**
	 * 根据id 删除
	 * @param userId
	 * @return
	 */
	int delete(Long userId);

	/**
	 * 根据 id数组批量删除
	 * @param userIds
	 * @return
	 */
	int batchDelete(Long[] userIds);
}
