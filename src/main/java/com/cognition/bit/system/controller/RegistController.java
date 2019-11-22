package com.cognition.bit.system.controller;

import com.cognition.bit.common.until.*;
import com.cognition.bit.system.entity.SysUser;
import com.cognition.bit.system.service.UserService;
import com.cognition.bit.common.config.Constant;
import com.cognition.bit.system.persistence.BaseController;
import com.cognition.bit.system.config.redis.RedisService;
import com.cognition.bit.common.until.encrypt.Md5Utils;
import com.cognition.bit.modules.service.SmsLogService;
import com.cognition.bit.system.vo.SysUserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 注册用户控制层
 *
 * @author taoya
 */
@RequestMapping("/register")
@Controller
public class RegistController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /*** 用户服务*/
    private UserService userService;
    /*** 短信与短信日志服务*/
    private SmsLogService smsLogService;
    /*** redis缓存*/
    private RedisService redisService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setSmsLogService(SmsLogService smsLogService) {
        this.smsLogService = smsLogService;
    }

    @Autowired
    public void setRedisService(RedisService redisService) {
        this.redisService = redisService;
    }

    //@Log("注册用户")
    @PostMapping("/registerUser")
    @ResponseBody
    ResultData register(SysUserVo sysUserVo) {
        try {
            //新用户
            SysUser sysUser = new SysUser();
            //验证用户是否已存在
            String loginInfo = null;
            if (StringUtils.isNotEmpty(sysUserVo.getMobile())) {
                loginInfo = sysUserVo.getMobile();
                //存入手机
                sysUser.setUserMobile(sysUserVo.getMobile());
            } else if (StringUtils.isNotEmpty(sysUserVo.getEmail())) {
                loginInfo = sysUserVo.getEmail();
                sysUser.setUserEmail(sysUserVo.getEmail());
            }
            //存在用户
            SysUser userold = userService.getWihtLogininfo(loginInfo);

            if (userold != null) {
                return ResultData.result(false).setMsg("用户已存在");
            } else {
                //插入很用户
                sysUser.preInsert();
                SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
                //按时间时分秒存入
                int hoursSeconds = Integer.parseInt(sdf.format(new Date()));
                //默认用户名
                String defaultUser = Tools.createNumCode("UR", hoursSeconds);
                sysUser.setLoginName(defaultUser);
                //密码加密
                String password = Md5Utils.encrypt(sysUser.getId().toString(), sysUserVo.getPassword());
                sysUser.setUserPassword(password);
                userService.save(sysUser);
                //删除用户随机验证码
                String randomKey = null;
                if (StringUtils.isNotEmpty(sysUserVo.getMobile())) {
                    randomKey = sysUserVo.getMobile() + "random";
                } else if (StringUtils.isNotEmpty(sysUserVo.getEmail())) {
                    randomKey = sysUserVo.getEmail() + "random";
                }
                redisService.redisDel(randomKey);
                return ResultData.result(true).setMsg("注册成功");
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResultData.result(false).setMsg("注册失败");
        }

    }

    /**
     * 验证用户信息是否存在
     *
     * @param params
     * @return
     */
    @PostMapping("/checkExistence")
    @ResponseBody
    boolean checkExistence(@RequestParam Map<String, Object> params) {
        // 存在，不通过，false/
        if (!params.isEmpty()) {
            params = Query.withDelFlag(params);
            return !userService.exit(params);
        } else {
            return false;
        }
    }

    /**
     * 验证短信验证码
     *
     * @param mobile
     * @return
     */
    @PostMapping("/checkCode")
    @ResponseBody
    boolean verifyCode(@RequestParam String mobile, @RequestParam(defaultValue = "") String email, @RequestParam String code) {
        String randomkey = null;
        if (StringUtils.isNotEmpty(mobile)) {
            randomkey = mobile + "random";
        } else if (StringUtils.isNotEmpty(email)) {
            randomkey = email + "random";
        }
        String redisGet = redisService.redisGet(randomkey);
        if (redisGet != null && redisGet.equals(code)) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 验证随机数
     *
     * @param verify
     * @param request
     * @return
     */
    @PostMapping("/checkVerify")
    @ResponseBody
    boolean checkVerify(String verify, HttpServletRequest request) {
        //从session中获取随机数
        boolean verifyState = true;
        String random = (String) request.getSession().getAttribute(RandomValidateCodeUtil.RANDOMCODEKEY);
        if (StringUtils.isBlank(verify)) {
            verifyState = false;
        }
        if (!random.equals(verify)) {
            verifyState = false;
        }
        return verifyState;
    }


    /***
     * 发送验证码
     * @param mobile
     * @return
     */
//	@Log("发送短信验证码")
    @PostMapping("/sendMobileCode")
    @ResponseBody
    ResultData sendMobileCode(@RequestParam String mobile) {
        if (StringUtils.isEmpty(mobile)) {
            return ResultData.result(false).setMsg("手机号码缺失");
        }

        String randomNumber = Tools.getRandomNumber(6);

        try {
            //发送的模块
            String moudle = Constant.REGISTER_SMS;
            //签名模板
            String signName = "知域知识网";
            //短信验证模板
            String templateCode = "SMS_170836933";
            //组装数组
            List<String> stringList = new ArrayList<>();
            stringList.add("code");
            stringList.add(randomNumber);
            String[] keyword = new String[stringList.size()];
            stringList.toArray(keyword);

            int times = smsLogService.snedSmsMessage(moudle, mobile, signName, templateCode, keyword, null);
            if (times > 0) {
                String randomkey = mobile + "random";
                String reidsState = redisService.redisSet(randomkey, randomNumber, 0);
                if (StringUtils.isNotEmpty(reidsState)) {
                    return ResultData.result(true,"发送成功");
                } else {
                    return ResultData.result(false,"发送失败");
                }
            } else {
                return ResultData.result(false,"发送失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultData.result(false,"发送失败");
        }
    }


//    public
}
