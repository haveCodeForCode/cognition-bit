package com.cognition.bit.modules.dao;

import com.cognition.bit.system.persistence.BaseDao;
import com.cognition.bit.modules.entity.ModDict;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 字典表
 * 
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2017-10-03 15:45:42
 */
@Mapper
@Repository("DictDao")
public interface DictDao extends BaseDao<ModDict> {

}
