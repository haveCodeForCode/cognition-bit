package com.root.cognition.system.controller;

import com.root.cognition.common.config.Constant;
import com.root.cognition.common.persistence.BaseController;
import com.root.cognition.common.redis.RedisService;
import com.root.cognition.common.until.*;
import com.root.cognition.common.until.encrypt.Md5Utils;
import com.root.cognition.modules.service.SmsLogService;
import com.root.cognition.system.entity.User;
import com.root.cognition.system.service.UserService;
import com.root.cognition.system.vo.UserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    Map<String, Object> register(UserVo userVo) {

        try {
            String loginInfo = null;
            if (StringUtils.isNotEmpty(userVo.getMobile())) {
                loginInfo = userVo.getMobile();
            } else if (StringUtils.isNotEmpty(userVo.getEmail())) {
                loginInfo = userVo.getEmail();
            }
            User userold = userService.getWihtLogininfo(loginInfo);

            if (userold != null) {
                return ResultMap.error("用户已存在");
            } else {
                User user = new User();
                user.preInsert();
                //存入手机
                user.setUserMobile(userVo.getMobile());
                //按时间时分秒存入
                SimpleDateFormat sim = new SimpleDateFormat("HHmmss");
                int hoursSeconds = Integer.parseInt(sim.format(new Date()));
                String defaultUser = Tools.createNumCode("UR", hoursSeconds);
                user.setLoginName(defaultUser);
                //密码加密
                String password = Md5Utils.encrypt(user.getId().toString(), user.getUserPassword());
                user.setUserPassword(password);
                userService.save(user);
                String randomkey = null;
                if (StringUtils.isNotEmpty(userVo.getMobile())) {
                    randomkey = userVo.getMobile() + "random";
                } else if (StringUtils.isNotEmpty(userVo.getEmail())) {
                    randomkey = userVo.getEmail() + "random";
                }
                redisService.redisDel(randomkey);
                return ResultMap.success("注册成功");
            }
        } catch (Exception e) {
            return ResultMap.error("注册失败");
        }

    }


    //@Log("访问注册")
    @GetMapping("/to")
    String to(Model model, String deptId) {
        model.addAttribute("deptId", deptId);
        return "regist";
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
    boolean verifyCode(@RequestParam String mobile, @RequestParam String code) {
        String randomkey = mobile + "random";
        String redisGet = redisService.redisGet(randomkey);
        if (redisGet.equals(code)) {
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
    @PostMapping("/sendCode")
    @ResponseBody
    ResultMap sendCode(@RequestParam String mobile) {
        if (StringUtils.isEmpty(mobile)) {
            return ResultMap.error("手机号码缺失");
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
                    return ResultMap.success("发送成功");
                } else {
                    return ResultMap.error("发送失败");
                }
            } else {
                return ResultMap.error("发送失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMap.error("发送失败");
        }
    }
}
