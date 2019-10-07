package com.root.cognition.system.controller;


import com.root.cognition.common.until.ResultData;
import com.root.cognition.system.config.jwt.JwtUtil;
import com.root.cognition.system.persistence.BaseController;
import com.root.cognition.common.until.RandomValidateCodeUtil;
import com.root.cognition.common.until.StringUtils;
import com.root.cognition.common.until.encrypt.Md5Utils;
import com.root.cognition.system.entity.User;
import com.root.cognition.system.service.MenuService;
import com.root.cognition.system.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登陆控制器
 *
 * @author LineInkBook
 */
@Controller
public class LoginController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private UserService userService;

    private MenuService menuService;


    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setMenuService(MenuService menuService) {
        this.menuService = menuService;
    }


    /**
     * 登陆接口
     * @param loginInfo
     * @param password
     * @param verify
     * @param request
     * @return
     */
    @PostMapping(value = "/login")
    @ResponseBody
    public ResultData login(String loginInfo, String password, String verify, HttpServletRequest request) {
        try {
            //从session中获取随机数
            String random = (String) request.getSession().getAttribute(RandomValidateCodeUtil.RANDOMCODEKEY);
            if (StringUtils.isBlank(verify)) {
                return ResultData.result(false).setMsg("请输入验证码");
            }
            if (!random.equals(verify)) {
                return ResultData.result(false).setMsg("请输入正确的验证码");
            }
        } catch (Exception e) {
            logger.error("验证码校验失败", e);
            return ResultData.result(false).setMsg("验证码校验失败");
        }

        User user = userService.getWihtLogininfo(loginInfo);
        Long userId;
        if (user != null) {
            userId = user.getId();
        } else {
            return ResultData.result(false).setMsg("用户尚未注册~！");
        }

        password = Md5Utils.encrypt(user.getId().toString(), password);

        //创建令牌
        String jwtToken=JwtUtil.sign(userId, password);

//        UsernamePasswordToken token = new UsernamePasswordToken(loginInfo, password);
//        Subject subject = SecurityUtils.getSubject();

        try {
            return ResultData.result(true).setData(jwtToken);
        } catch (AuthenticationException e) {
            logger.info(e.getMessage());
            return ResultData.result(false).setMsg(e.getMessage());
        }
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
