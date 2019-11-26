package com.cognition.bit.system.dao;

import com.cognition.bit.system.domain.SysUserRole;
import com.cognition.bit.system.persistence.BaseDao;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户相关信息Dao
 * 
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2017-10-03 11:08:59
 */
@Mapper
@Repository("UserRelationDao")
public interface UserRoleDao extends BaseDao<SysUserRole> {

	List<Long> listRoleId(Long userId);

	int removeByUserId(Long userId);

	int removeByRoleId(Long roleId);

	int batchSave(List<SysUserRole> list);

	int batchRemoveByUserId(Long[] ids);
}
