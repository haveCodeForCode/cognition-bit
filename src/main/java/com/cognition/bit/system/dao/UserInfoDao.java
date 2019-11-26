package com.cognition.bit.system.dao;

import com.cognition.bit.system.domain.SysUserInfo;
import com.cognition.bit.system.persistence.BaseDao;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 用户信息Dao层
 *
 * @author Worry
 * @version 2019/4/1
 */
@Mapper
@Repository("UserInfoDao")
public interface UserInfoDao extends BaseDao<SysUserInfo> {

}
