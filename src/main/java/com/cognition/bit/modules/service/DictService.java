package com.cognition.bit.modules.service;



import com.cognition.bit.modules.entity.ModDict;
import com.cognition.bit.system.persistence.Tree;


import java.util.List;
import java.util.Map;

/**
 * 字典表
 *
 * @author 1024
 * @date 2017-09-29 18:28:07
 */
public interface DictService {

    Tree<ModDict> getDictTree();

    /**
     * 根据名称
     * @param type
     * @param value
     * @return
     */
    String getName(String type, String value);

    /**
     * 根据type获取数据
     *
     * @param type
     * @return
     */
    List<ModDict> listByType(String type);

    //*****************************************

    /**
     * 根据id获取
     * @param id
     * @return
     */
    ModDict get(Long id);

    /**
     * 根据条件获取
     * @param map
     * @return
     */
    ModDict get(Map<String, Object> map);

    /**
     * 根据条件查询
     * @param map
     * @return
     */
    List<ModDict> list(Map<String, Object> map);

    /**
     * 分页，统计数量
     * @param map
     * @return
     */
    int count(Map<String, Object> map);

    /**
     * 插入
     * @param modDict
     * @return
     */
    int save(ModDict modDict);

    /**
     * 更新
     * @param modDict
     * @return
     */
    int update(ModDict modDict);

    /**
     * 移除
     * @param id
     * @return
     */
    int remove(Long id);

    /**
     * 批量移除
     * @param ids
     * @return
     */
    int batchRemove(Long[] ids);

}
