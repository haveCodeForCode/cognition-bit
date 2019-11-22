package com.cognition.bit.modules.controller;

import com.cognition.bit.modules.config.AlibabaSms;
import com.cognition.bit.modules.entity.ModDict;
import com.cognition.bit.modules.entity.ModSmsLog;
import com.cognition.bit.modules.service.DictService;
import com.cognition.bit.modules.service.SmsLogService;
import com.cognition.bit.common.config.Constant;
import com.cognition.bit.common.until.PageUtils;
import com.cognition.bit.common.until.Query;
import com.cognition.bit.common.until.ResultData;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 短息日志表
 *
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2019-06-15 15:54:43
 */

@Controller
@RequestMapping("/system/sms")
public class SmsController {
    
    private SmsLogService smsLogService;

    private AlibabaSms alibabaSms;

    private DictService dictService;

    @Autowired
    public void setSmsLogService(SmsLogService smsLogService){
        this.smsLogService = smsLogService;
    }

    @Autowired
    public void setAlibabaSms(AlibabaSms alibabaSms) {
        this.alibabaSms = alibabaSms;
    }

    @Autowired
    public void setDictService(DictService dictService) {
        this.dictService = dictService;
    }

    /**
     * 前往短信发送页面
     *
     * @param model
     * @return
     */
    @GetMapping("/toSendMessage")
    public String sendMessage(Model model) {
        Map<String, Object> alibabaSms = new HashMap<>();
        //查询组
        Map<String, Object> param = Query.withDelFlag();
        //查询变量赋值
        param.put("type", "alibaba_sms_signName");
        //列表条件查询签名模板
        List<ModDict> alibabaSmsSignName = dictService.list(param);
        param.put("type", "alibaba_sms_template");
        //查询短信模板
        List<ModDict> alibabaSmsTemplate = dictService.list(param);
        alibabaSms.put("alibabaSmsSignName", alibabaSmsSignName);
        alibabaSms.put("alibabaSmsTemplate", alibabaSmsTemplate);
        //赋值前端页面
        model.addAttribute("alibabaSms", alibabaSms);
        return "modules/smsLog/sendSms";
    }




    @PostMapping("/sendMessage")
    @ResponseBody
    public ResultData sendAlibabaSms(String mobile, String signName, String templateCode, String[] keyword, String outId) throws InterruptedException {
        alibabaSms.setConfigureAlibaba();
        String moudle = Constant.FREE_SMS;
        int times=smsLogService.snedSmsMessage(moudle,mobile,signName,templateCode,keyword,outId);
        if (times>0) {
            return ResultData.result(true);
        }
        return ResultData.result(false, "系统错误");
    }

/**************************************************************************/

    /**
     * 前往短信记录页面
     * @return
     */
    @GetMapping("")
    @RequiresPermissions("modules:sms:smsLog")
    String smsLog(){
        return "modules/smsLog/smsLog";
    }

    /**
     * 分页列表
     */
    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("modules:sms:smsLog")
    public PageUtils list(@RequestParam Map<String, Object> params){
        //查询列表数据
        Map<String,Object> query = new Query(params);
        List<ModSmsLog> modSmsLogList = smsLogService.list(query);
        int total = smsLogService.count(query);
        PageUtils pageUtils = new PageUtils(modSmsLogList, total);
        return pageUtils;
    }

    /**
     * 前往添加页面
     * @return
     */
    @GetMapping("/add")
    @RequiresPermissions("modules:sms:add")
    String add(){
        return "modules/smsLog/add";
    }

    /**
     * 修改短信记录（启用
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/edit/{id}")
    @RequiresPermissions("modules:sms:edit")
    String edit(@PathVariable("id") Long id,Model model){
        ModSmsLog modSmsLog = smsLogService.get(id);
        model.addAttribute("smsLog", modSmsLog);
        return "modules/smsLog/edit";
    }

    /**
     * 保存
     */
    @ResponseBody
    @PostMapping("/save")
    @RequiresPermissions("modules:sms:add")
    public ResultData save(ModSmsLog modSmsLog){
        if(smsLogService.save(modSmsLog)>0){
            return ResultData.result(true);
        }
        return ResultData.result(false);
    }
    /**
     * 修改
     */
    @ResponseBody
    @RequestMapping("/update")
    @RequiresPermissions("modules:sms:edit")
    public ResultData update(ModSmsLog modSmsLog){
        smsLogService.update(modSmsLog);
        return ResultData.result(true);
    }

    /**
     * 删除
     */
    @PostMapping( "/remove")
    @ResponseBody
    @RequiresPermissions("modules:sms:remove")
    public ResultData remove(Long id){
        if(smsLogService.remove(id)>0){
            return ResultData.result(true);
        }
        return ResultData.result(false);
    }

    /**
     * 删除
     */
    @PostMapping( "/batchRemove")
    @ResponseBody
    @RequiresPermissions("modules:sms:batchRemove")
    public ResultData remove(@RequestParam("ids[]") Long[] ids){
        smsLogService.batchRemove(ids);
        return ResultData.result(true);
    }

}

