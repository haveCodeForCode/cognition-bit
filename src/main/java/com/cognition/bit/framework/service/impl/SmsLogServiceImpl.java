package com.cognition.bit.framework.service.impl;

import com.cognition.bit.framework.config.AlibabaSms;
import com.cognition.bit.framework.dao.SmsLogDao;
import com.cognition.bit.framework.entity.ModSmsLog;
import com.cognition.bit.framework.service.SmsLogService;
import com.cognition.bit.common.config.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;



/**
 *
 * @author taoya
 */
@Service
public class SmsLogServiceImpl implements SmsLogService {

	private static final Logger logger = LoggerFactory.getLogger(SmsLogService.class);

	private SmsLogDao smsLogDao;

	private AlibabaSms alibabaSms;

	@Autowired
	public void setSmsLogDao (SmsLogDao smsLogDao){
		this.smsLogDao=smsLogDao;
	}

	@Autowired
	public void setAlibabaSms(AlibabaSms alibabaSms) {
		this.alibabaSms = alibabaSms;
	}


	@Override
	public int snedSmsMessage(String moudle, String mobile, String signName, String templateCode, String[] keyword, String outId) {
		try {
			//通过接口发送短信回传短信记录
			alibabaSms.setConfigureAlibaba();
			ModSmsLog modSmsLog = alibabaSms.sendMesage(moudle, mobile, signName, templateCode, keyword, outId);
			if (modSmsLog != null) {
				modSmsLog.preInsert();
				//插入短信日志
				smsLogDao.insert(modSmsLog);
				if (Constant.OK.equals(modSmsLog.getSmsReturnCode())){
					return Constant.INT_ONE;
				}else{
					return Constant.INT_ZERO;
				}
			}
			return Constant.INT_ZERO;
		} catch (InterruptedException e) {
			//报错抛出异常
			e.printStackTrace();
			System.err.println(e.getMessage());
			//返回信息
			return Constant.INT_ZERO;
		}
	}


	@Override
	public ModSmsLog get(Long id){
		return smsLogDao.get(id);
	}
	
	@Override
	public List<ModSmsLog> list(Map<String, Object> map){
		return smsLogDao.findList(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return smsLogDao.count(map);
	}
	
	@Override
	public int save(ModSmsLog modSmsLog){
		modSmsLog.preInsert();
		return smsLogDao.insert(modSmsLog);
	}
	
	@Override
	public int update(ModSmsLog modSmsLog){
		return smsLogDao.update(modSmsLog);
	}
	
	@Override
	public int remove(Long id){
		return smsLogDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return smsLogDao.batchRemove(ids);
	}
	
}
