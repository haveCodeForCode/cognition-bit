package com.cognition.bit.modules.dao;

import com.cognition.bit.system.persistence.BaseDao;
import com.cognition.bit.modules.entity.ModTask;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 *
 * @author taoya
 * @date 2017-10-03 15:45:42
 */
@Mapper
@Repository("TaskDao")
public interface TaskDao extends BaseDao<ModTask> {

}
