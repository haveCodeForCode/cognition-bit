package com.cognition.bit.modules.service;


import com.cognition.bit.modules.entity.ModFileRecord;

import java.util.List;
import java.util.Map;

/**
 * 文件上传
 * 
 * @author 1122
 * @date 2017-09-19 16:02:20
 */
public interface FileService {
	
	ModFileRecord get(Long id);
	
	List<ModFileRecord> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(ModFileRecord sysModFileRecord);
	
	int update(ModFileRecord sysModFileRecord);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	/**
	 * 判断一个文件是否存在
	 * @param url File中存的路径
	 * @return
	 */
    Boolean isExist(String url);
}
