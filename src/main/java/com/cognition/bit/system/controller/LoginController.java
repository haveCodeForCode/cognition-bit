package com.cognition.bit.system.controller;


import com.alibaba.fastjson.JSONObject;
import com.cognition.bit.common.until.AjaxResult;
import com.cognition.bit.common.until.RandomValidateCodeUtil;
import com.cognition.bit.common.until.encrypt.Md5Utils;
import com.cognition.bit.system.config.redis.RedisService;
import com.cognition.bit.system.domain.SysUser;
import com.cognition.bit.system.persistence.BaseController;
import com.cognition.bit.system.service.UserService;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

//import com.cognition.bit.system.config.jwt.JwtUtil;

/**
 * 登陆控制器
 *
 * @author LineInkBook
 */
@Controller
public class LoginController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    private UserService userService;

    private RedisService redisService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setRedisService(RedisService redisService) {
        this.redisService = redisService;
    }

    /**
     * 登陆接口
     *
     * @return
     */
    @PostMapping(value = "/login")
    @ResponseBody
    public AjaxResult login(@RequestBody Map<String, String> params) {
        JSONObject jsonObect = new JSONObject();
        try {
            //从session中获取随机数
//            String random = (String) request.getSession().getAttribute(RandomValidateCodeUtil.RANDOMCODEKEY);
//            if (StringUtils.isBlank(verify)) {
//                return ResultData.result(false).setMsg("请输入验证码");
//            }
//            if (!random.equals(verify)) {
//                return ResultData.result(false).setMsg("请输入正确的验证码");
//            }
        } catch (Exception e) {
            logger.error("验证码校验失败", e);
            return AjaxResult.error("验证码校验失败");
        }
        //获取变量
        String loginInfo = params.get("username");
        String password = params.get("password");

        SysUser sysUser = userService.getWihtLogininfo(loginInfo);
        if (sysUser != null) {
            String encPassword = Md5Utils.encrypt(sysUser.getId().toString(), password);
            if (encPassword.equals(sysUser.getUserPassword())){
                //创建令牌
                UsernamePasswordToken token = new UsernamePasswordToken(sysUser.getId().toString(), password);
                //登录对象
                Subject subject = SecurityUtils.getSubject();
                subject.login(token);
                //返回sessionId
                return AjaxResult.success(subject.getSession().getId());
            }else {
                return AjaxResult.error("用户密码错误！");
            }
        } else {
            return AjaxResult.error("用户尚未注册");
        }
    }


    /**
     * 登陆验证接口
     *
     * @return
     */
    @GetMapping(value = "/userInfo")
    @ResponseBody
    public AjaxResult findUserInfo(@Param("token") String token) {
        System.err.println(token);
        Map<String, String> map = new HashMap<>();
        String redisGet = redisService.redisGet(token);
        map.put("name", "1234");
        map.put("avter", "23456");
        return AjaxResult.success(map);
    }

    @GetMapping(value = "/testRedis")
    @ResponseBody
    public AjaxResult testRedis(@Param("value") String value) {
        final double d = Math.random();
        final int i = (int) (d * 1000);
        redisService.redisSet(String.valueOf(i), value, 10);
        return AjaxResult.success("成功");
    }

    /**
     * 生成验证码
     */
    @GetMapping(value = "/getVerify")
    public void getVerify(HttpServletRequest request, HttpServletResponse response) {
        try {
            //设置相应类型,告诉浏览器输出的内容为图片
            response.setContentType("image/jpeg");
            //设置响应头信息，告诉浏览器不要缓存此内容
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expire", 0);
            RandomValidateCodeUtil randomValidateCode = new RandomValidateCodeUtil();
            //输出验证码图片方法
            randomValidateCode.getRandcode(request, response);
        } catch (Exception e) {
            logger.error("获取验证码失败>>>> ", e);
        }
    }

}
