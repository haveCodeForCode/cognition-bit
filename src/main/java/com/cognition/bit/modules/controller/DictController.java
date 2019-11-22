package com.cognition.bit.modules.controller;

import com.cognition.bit.modules.entity.ModDict;
import com.cognition.bit.modules.service.DictService;
import com.cognition.bit.system.persistence.BaseController;
import com.cognition.bit.common.config.Constant;
import com.cognition.bit.common.until.PageUtils;
import com.cognition.bit.common.until.Query;
import com.cognition.bit.common.until.ResultData;
import com.cognition.bit.common.until.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Worry
 * @version 2019/6/10
 */
@Controller
@RequestMapping("/modules/dict")
public class DictController extends BaseController {

    private DictService dictService;

    @Autowired
    public void setDictService(DictService dictService) {
        this.dictService = dictService;
    }

    @GetMapping("")
    @RequiresPermissions("modules:dict:dict")
    String dict() {
        return "modules/dict/dict";
    }

    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("modules:dict:dict")
    public PageUtils list(@RequestParam Map<String, Object> params) {
        // 查询列表数据
        Query query = new Query(params);
        List<ModDict> modDictList = dictService.list(query);
        int total = dictService.count(query);
        PageUtils pageUtils = new PageUtils(modDictList, total);
        return pageUtils;
    }

    @ResponseBody
    @GetMapping("/treelist")
    @RequiresPermissions("modules:dict:dict")
    public List<ModDict> Treelist(@RequestParam Map<String, Object> params) {
        // 查询列表数据
        Map<String,Object> query = Query.withDelFlag(params);
        List<ModDict> modDictList = dictService.list(query);
        return modDictList;
    }



    @GetMapping("/add/{parentId}")
    @RequiresPermissions("modules:dict:add")
    String add(Model model,@PathVariable("parentId")String parentId) {
        if (StringUtils.isEmpty(parentId)){
            parentId = Constant.STRING_ZERO;
        }
        model.addAttribute("parentId",parentId);
        return "modules/dict/add";
    }


    @GetMapping("/edit/{id}")
    @RequiresPermissions("modules:dict:edit")
    String edit(@PathVariable("id") Long id, Model model) {
        ModDict modDict = dictService.get(id);
        model.addAttribute("dict", modDict);
        return "modules/dict/edit";
    }

    /**
     * 保存
     */
    @ResponseBody
    @PostMapping("/save")
    @RequiresPermissions("modules:dict:add")
    public ResultData save(ModDict modDict) {
        //补充实体内信息
        modDict.setCreateBy(getUserId());
        if (dictService.save(modDict) > 0) {
            return ResultData.result(true);
        }
        return ResultData.result(false);
    }

    /**
     * 修改
     */
    @ResponseBody
    @RequestMapping("/update")
    @RequiresPermissions("modules:dict:edit")
    public ResultData update(ModDict modDict) {
        modDict.setUpdateBy(getUserId());
        modDict.setUpdateTime(new Date());
        dictService.update(modDict);
        return ResultData.result(true);
    }

    /**
     * 删除
     */
    @PostMapping("/remove")
    @ResponseBody
    @RequiresPermissions("modules:dict:remove")
    public ResultData remove(Long id) {
        if (dictService.remove(id) > 0) {
            return ResultData.result(true);
        }
        return ResultData.result(false);
    }

    /**
     * 批量删除
     */
    @PostMapping("/batchRemove")
    @ResponseBody
    @RequiresPermissions("modules:dict:batchRemove")
    public ResultData remove(@RequestParam("ids[]") Long[] ids) {
        dictService.batchRemove(ids);
        return ResultData.result(true);
    }

    @GetMapping("/type")
    @ResponseBody
    public List<ModDict> listType() {
        return dictService.listByType(null);
    }

    /**
     * 类别已经指定增加
     */
    @GetMapping("/add/{id}/{type}/{description}")
    @RequiresPermissions("modules:dict:add")
    String addD(Model model,@PathVariable("id") String id, @PathVariable("type") String type, @PathVariable("description") String description) {
        model.addAttribute("parentId",id);
        model.addAttribute("type", type);
        model.addAttribute("description", description);
        return "modules/dict/add";
    }

    @ResponseBody
    @GetMapping("/list/{type}")
    public List<ModDict> listByType(@PathVariable("type") String type) {
        // 查询列表数据
        Map<String, Object> map = Query.withDelFlag();
        map.put("type", type);
        return dictService.list(map);
    }
}
