package com.cognition.bit.modules.config;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.cognition.bit.common.until.JsonUntil;
import com.cognition.bit.common.until.Query;
import com.cognition.bit.common.until.StringUtils;
import com.cognition.bit.modules.entity.ModDict;
import com.cognition.bit.modules.entity.ModSmsLog;
import com.cognition.bit.modules.service.DictService;
import com.cognition.bit.system.config.ApplicationContextRegister;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 阿里大于短信
 *
 * @author Worry
 * @version 2019/6/14
 */
@Service
public class AlibabaSms {
    /**
     * 云通信产品-短信API服务产品域名（接口地址固定，无需修改）
     */
    private static final String DOMAIN = "dysmsapi.aliyuncs.com";

    private static final String REGIONID = "cn-hangzhou";

    private static String ACCESS_SERCRET = null;

    private static String ACCESS_KEYID = null;

    /**
     * 云通信产品-短信API服务产品名称（短信产品名固定，无需修改）
     */
    private static final String PRODUCT = "Dysmsapi";


    /**
     * 获取短信相关设定值
     */
    @Cacheable(value = "zero")
    public void setConfigureAlibaba() {
        Map<String, Object> query = Query.withDelFlag();
        query.put("type", "alibaba_sms_data");
        DictService dictService = ApplicationContextRegister.getBean(DictService.class);
        List<ModDict> modDictList = dictService.list(query);
        for (ModDict modDict : modDictList) {
            if ("accessSecret".equals(modDict.getName())) {
                ACCESS_SERCRET = modDict.getValue();
            }

            if ("accessKeyId".equals(modDict.getName())) {
                ACCESS_KEYID = modDict.getValue();
            }

        }
    }

    /**
     * 短信发送接口
     *
     * @param moudle       模块：哪个模块发送的短信
     * @param mobile       手机号
     * @param signName     签名
     * @param templateCode 模板编号
     * @param keyword      发送信息串
     * @param outId
     * @return
     */
    public ModSmsLog sendMesage(String moudle, String mobile, String signName, String templateCode, String[] keyword, String outId) throws InterruptedException {

        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //组装发送对象
        CommonRequest commonRequest = new CommonRequest();
        commonRequest.setMethod(MethodType.POST);
        commonRequest.setDomain(DOMAIN);
        commonRequest.setVersion("2017-05-25");
        commonRequest.setAction("SendSms");

        commonRequest.putQueryParameter("SignName", signName);
        commonRequest.putQueryParameter("PhoneNumbers", mobile);
        commonRequest.putQueryParameter("TemplateCode", templateCode);

        //组装信息
        StringBuilder sendSmsContent = new StringBuilder("{");
        for (int i = 0; i < keyword.length; i = i + 2) {
            sendSmsContent.append("\"").append(keyword[i]).append("\":\"").append(keyword[i + 1]).append("\"").append(",");
        }
        //删除最后一个逗号
        sendSmsContent.deleteCharAt(sendSmsContent.length()-1);
        sendSmsContent.append("}");
        commonRequest.putQueryParameter("TemplateParam",sendSmsContent.toString());
        try {
            //请求短信阿里巴巴服务
            DefaultProfile profile = DefaultProfile.getProfile(signName, ACCESS_KEYID, ACCESS_SERCRET);
            IAcsClient client = new DefaultAcsClient(profile);
            CommonResponse response = client.getCommonResponse(commonRequest);
            //获取返回数值
            String responseData = response.getData();
            //判断返回值是否是空
            if (StringUtils.isNotEmpty(responseData)) {

                //将返回回来的字符串转换成bean
                ResponseValue responseValue = JsonUntil.getJsonToBean(responseData, ResponseValue.class);
                //创建短信日志
                ModSmsLog modSmsLog = new ModSmsLog();
                modSmsLog.setSmsReturnCode(responseValue.code);
                modSmsLog.setSmsReturnMessage(responseValue.getMessage());
                modSmsLog.setTemplateCode(templateCode);
                modSmsLog.setMobile(mobile);
                modSmsLog.setModule(moudle);
                modSmsLog.setCreateTime(new Date());
                //成功则回调短信查询记录，回调成功则记录短信
                if (responseValue.getCode() != null && "OK".equals(responseValue.getCode())) {
                    Thread.sleep(3000L);
                    return formSmslogs(responseValue,mobile, modSmsLog);
                }else {
                    return modSmsLog;
                }
            }
            return null;
        } catch (ClientException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 接受回传对象
     * @param mobile
     * @param responseValue
     * @return
     * @throws ClientException
     */
    private ModSmsLog formSmslogs(ResponseValue responseValue, String mobile, ModSmsLog modSmsLog) throws ClientException {
        System.out.println("短信接口返回数据----------------");

        //初始化ascClient
        IClientProfile iClientProfile = DefaultProfile.getProfile(REGIONID, ACCESS_KEYID, ACCESS_SERCRET);
        DefaultProfile.addEndpoint(REGIONID, REGIONID, PRODUCT, DOMAIN);
        IAcsClient acsClient = new DefaultAcsClient(iClientProfile);
        //组装请求对象
        QuerySendDetailsRequest request = new QuerySendDetailsRequest();
        //必填-号码
        request.setPhoneNumber(mobile);
        //可选-调用发送短信接口时返回的BizId
        request.setBizId(responseValue.getBizId());
        //必填-短信发送的日期 支持30天内记录查询（可查其中一天的发送数据），格式yyyyMMdd
        SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd");
        request.setSendDate(ft.format(new Date()));
        //必填-页大小
        request.setPageSize(10L);
        //必填-当前页码从1开始计数
        request.setCurrentPage(1L);
        //hint 此处可能会抛出异常，注意catch
        QuerySendDetailsResponse querySendDetailsResponse = acsClient.getAcsResponse(request);
        System.out.println("Code=" + querySendDetailsResponse.getCode());
        System.out.println("Message=" + querySendDetailsResponse.getMessage());
        if (querySendDetailsResponse.getCode() != null && "OK".equals(querySendDetailsResponse.getCode())) {
            String sendStatus = "";
            //获取返回结果
            for (QuerySendDetailsResponse.SmsSendDetailDTO smsSendDetailDTO : querySendDetailsResponse.getSmsSendDetailDTOs()) {
                modSmsLog.setContent(smsSendDetailDTO.getContent());
                sendStatus = Long.toString(smsSendDetailDTO.getSendStatus());
                System.out.println("SendStatus=" + smsSendDetailDTO.getSendStatus());
            }
            modSmsLog.setIspush(sendStatus);

            System.out.println("TotalCount=" + querySendDetailsResponse.getTotalCount());
            System.out.println("RequestId=" + querySendDetailsResponse.getRequestId());
            return modSmsLog;
        }
        return null;
    }

    /**
     * 短信相应对象（局部对象）
     */
    private static class ResponseValue {
        private String message;
        private String requestId;
        private String bizId;
        private String code;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getRequestId() {
            return requestId;
        }

        public void setRequestId(String requestId) {
            this.requestId = requestId;
        }

        public String getBizId() {
            return bizId;
        }

        public void setBizId(String bizId) {
            this.bizId = bizId;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }


    //        public static void main(String[] args) {
//        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAImACndFVWhD0r", "JqngT3hwiOB16gQbfxIBdnZPGgokrB");
//        IAcsClient client = new DefaultAcsClient(profile);
//
//        CommonRequest request = new CommonRequest();
//        request.setMethod(MethodType.POST);
//        request.setDomain("dysmsapi.aliyuncs.com");
//        request.setVersion("2017-05-25");
//        request.setAction("SendSms");
//        request.putQueryParameter("RegionId", "cn-hangzhou");
//        request.putQueryParameter("PhoneNumbers", "17631012827");
//        request.putQueryParameter("SignName", "知域服务");
//        request.putQueryParameter("TemplateCode", "SMS_167974939");
//        request.putQueryParameter("TemplateParam", "{\"code\":\"Its0a0werewolf\"}");
//        try {
//            CommonResponse response = client.getCommonResponse(request);
//            System.out.println(response.toString());
//            System.out.println(response.getData());
//        } catch (ServerException e) {
//            e.printStackTrace();
//        } catch (ClientException e) {
//            e.printStackTrace();
//        }
//    }


}
