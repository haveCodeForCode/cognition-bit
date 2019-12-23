package com.cognition.bit.framework.service;

import com.cognition.bit.framework.entity.ModSmsLog;

import java.util.List;
import java.util.Map;

/**
 * 短息日志表
 *
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2019-06-15 15:54:43
 */
public interface SmsLogService {


    /**
     * 发送短信接口
     * @param moudle 模块
     * @param mobile 手机
     * @param signName 签名
     * @param templateCode 模板编号
     * @param keyword 组装数据
     * @param outId
     * @return
     */
    int snedSmsMessage(String moudle, String mobile, String signName, String templateCode, String[] keyword, String outId);

 //***********************************************//

    ModSmsLog get(Long id);

    List<ModSmsLog> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int save(ModSmsLog modSmsLog);

    int update(ModSmsLog modSmsLog);

    int remove(Long id);

    int batchRemove(Long[] ids);
}
