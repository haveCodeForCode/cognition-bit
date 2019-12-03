package com.cognition.bit.framework.service.impl;


import com.cognition.bit.framework.dao.DictDao;
import com.cognition.bit.framework.entity.ModDict;
import com.cognition.bit.framework.service.DictService;
import com.cognition.bit.system.persistence.Tree;
import com.cognition.bit.common.until.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author 1024
 */
@Service
public class DictServiceImpl implements DictService {


    private DictDao dictDao;

    @Autowired
    public void setDictDao(DictDao dictDao) {
        this.dictDao = dictDao;
    }

    @Override
    public ModDict get(Long id) {
        return dictDao.get(id);
    }

    @Override
    public ModDict get(Map<String, Object> map) {
        return null;
    }

    @Override
    public List<ModDict> list(Map<String, Object> map) {
        return dictDao.findList(map);
    }

    @Override
    public int count(Map<String, Object> map) {
        return dictDao.count(map);
    }

    @Override
    public int save(ModDict modDict) {
        modDict.preInsert();
        return dictDao.insert(modDict);
    }

    @Override
    public int update(ModDict modDict) {
        return dictDao.update(modDict);
    }

    @Override
    public int remove(Long id) {
        return dictDao.remove(id);
    }

    @Override
    public int batchRemove(Long[] ids) {
        return dictDao.batchRemove(ids);
    }

    @Override
    public Tree<ModDict> getDictTree() {
        //去除父级菜单
        Map<String,Object> query = Query.withDelFlag();
        List<ModDict> modDictList = dictDao.findList(query);
        return null;
    }

    @Override
    public String getName(String type, String value) {
        Map<String, Object> param = new HashMap<String, Object>(16);
        param.put("type", type);
        param.put("value", value);
        String rString = dictDao.findList(param).get(0).getName();
        return rString;
    }

    @Override
    public List<ModDict> listByType(String type) {
        Map<String, Object> param = Query.withDelFlag();
        param.put("type", type);
        return dictDao.findList(param);
    }

}
