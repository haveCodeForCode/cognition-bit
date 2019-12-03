package com.cognition.bit.framework.dao;

import com.cognition.bit.system.persistence.BaseDao;
import com.cognition.bit.framework.entity.ModSmsLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 短息日志表
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2019-06-15 15:54:43
 */
@Mapper
@Repository("SmsLogDao")
public interface SmsLogDao extends BaseDao<ModSmsLog> {

}
