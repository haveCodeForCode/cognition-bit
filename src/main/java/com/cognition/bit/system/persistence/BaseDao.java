package com.cognition.bit.system.persistence;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * DAO支持类实现 (9条)
 * @author worry
 */
@Mapper
public interface BaseDao<T> {
    /**
     * 获取单条数据（根据id）
     *
     * @param id 表主键
     * @return 实体类
     */
    T get(Long id);

    /**
     * 获取单条数据（根据实体）
     *
     * @param params 查询条件
     * @return entity   实体类
     */
    T getByEntity(Map<String, Object> params);

    /**
     * 根据实体中内容，查询数据列表
     *
     * @param params 查询条件
     * @return list<t> 实体类列
     */
    List<T> findList(Map<String, Object> params);

    /**
     * 数量统计
     *
     * @param params 查询条件
     * @return int   数量
     */
    int count(Map<String, Object> params);

    /**
     * 插入数据
     *
     * @param entity 实体类
     * @return 数量
     */
    int insert(T entity);

    /**
     * 更新数据
     *
     * @param entity 实体类
     * @return 数量
     */
    int update(T entity);

    /**
     * 删除数据（一般为逻辑删除，更新del_flag字段为1）
     *
     * @param id 实体类的Id
     * @return 数量
     * @see public int remove(T entity)
     */
    int remove(Long id);

    /**
     * 删除一系列数据
     *
     * @param ids 实体类id数组
     * @return 数量
     */
    int batchRemove(Long[] ids);

}