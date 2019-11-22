package com.cognition.bit.system.service.impl;


import com.cognition.bit.system.entity.SysDept;
import com.cognition.bit.system.service.DeptService;
import com.cognition.bit.common.config.Constant;
import com.cognition.bit.system.persistence.Tree;
import com.cognition.bit.common.until.BuildTree;
import com.cognition.bit.common.until.Query;
import com.cognition.bit.system.dao.DeptDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 部门管理表
 * @author taoya
 */
@Service
public class DeptServiceImpl implements DeptService {


    private DeptDao deptDao;

    @Autowired
    public void setDeptDao (DeptDao deptDao){
        this.deptDao = deptDao;
    }

    @Override
    public SysDept get(Long deptId) {
        return deptDao.get(deptId);
    }

    @Override
    public SysDept get(Map<String, Object> map) {
        return deptDao.getByEntity(map);
    }

    @Override
    public List<SysDept> findList(Map<String, Object> map) {
        return deptDao.findList(map);
    }

    @Override
    public int count(Map<String, Object> map) {
        return deptDao.count(map);
    }

    @Override
    public int save(SysDept sysDept) {
        sysDept.preInsert();
        return deptDao.insert(sysDept);
    }

    @Override
    public int update(SysDept sysDept) {
        return deptDao.update(sysDept);
    }

    @Override
    public int delete(Long deptId) {
        return deptDao.remove(deptId);
    }

    @Override
    public int batchDelete(Long[] deptIds) {
        return deptDao.batchRemove(deptIds);
    }




    @Override
    public Tree<SysDept> getTree() {
        List<Tree<SysDept>> trees = new ArrayList<Tree<SysDept>>();
        Map<String, Object> query =Query.withDelFlag();
        List<SysDept> sysSysDepts = deptDao.findList(query);
        for (SysDept sysDept : sysSysDepts) {
            Tree<SysDept> tree = new Tree<SysDept>();

            tree.setId(sysDept.getId().toString());
            tree.setParentId(sysDept.getParentId().toString());
            tree.setText(sysDept.getName());

            Map<String, Object> state = new HashMap<>(16);
            state.put("opened", true);
            tree.setState(state);
            trees.add(tree);
        }
        // 默认顶级菜单为０，根据数据库实际情况调整
        Tree<SysDept> tree = BuildTree.build(trees);
        return tree;
    }

    @Override
    public boolean checkDeptHasUser(Long deptId) {
        // TODO Auto-generated method stub
        //查询部门以及此部门的下级部门
        Map<String, Object> query = new HashMap<>();
        query.put("id", deptId);
        int result = deptDao.count(query);
        return result == Constant.INT_ZERO;
    }

    @Override
    public List<Long> listChildrenIds(Long parentId) {
        Map<String, Object> query = new HashMap<>();
        query.put("parentId", parentId);
        List<SysDept> sysDeptDOS = findList(query);
        return treeMenuList(sysDeptDOS, parentId);
    }

    @Override
    public String[] listParentDept() {
        return deptDao.listParentDept();
    }

    @Override
    public String[] listAllDept() {
        return deptDao.listAllDept();
    }

    List<Long> treeMenuList(List<SysDept> menuList, Long pid) {
        List<Long> childIds = new ArrayList<>();
        for (SysDept sysDept : menuList) {
            //遍历出父id等于参数的id，add进子节点集合
            if (sysDept.getParentId().equals(pid)) {
                //递归遍历下一级
                treeMenuList(menuList, sysDept.getId());
                childIds.add(sysDept.getId());
            }
        }
        return childIds;
    }

}
