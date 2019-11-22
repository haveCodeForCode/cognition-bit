package com.cognition.bit.modules.dao;

import com.cognition.bit.system.persistence.BaseDao;
import com.cognition.bit.modules.entity.ModFileRecord;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 文件上传
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2017-10-03 15:45:42
 */
@Mapper
@Repository("FileDao")
public interface FileRecordDao extends BaseDao<ModFileRecord> {
}
