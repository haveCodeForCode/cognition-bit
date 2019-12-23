package com.cognition.bit.system.dao;

import com.cognition.bit.system.domain.SysUser;
import com.cognition.bit.system.persistence.BaseDao;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


/**
 * @author 王睿
 * @version 2018/12/24
 */
@Mapper
@Repository("UserDao")
public interface UserDao extends BaseDao<SysUser> {

    /**
     * 根据登陆传入信息擦护心用户（手机、邮箱）
     * @param loginInfo
     * @return
     */
    SysUser getWihtLogininfo(String loginInfo);
}
