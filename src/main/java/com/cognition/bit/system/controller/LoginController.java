package com.cognition.bit.system.controller;


import com.cognition.bit.system.domain.SysUser;
import com.cognition.bit.system.persistence.BaseController;
import com.cognition.bit.system.service.MenuService;
import com.cognition.bit.system.service.UserService;
import com.cognition.bit.common.until.RandomValidateCodeUtil;
import com.cognition.bit.common.until.ResultData;
import com.cognition.bit.common.until.encrypt.Md5Utils;
import com.cognition.bit.system.config.jwt.JwtUtil;
import com.cognition.bit.system.vo.SysUserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

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
    public ResultData login(@RequestParam(value = "loginInfo", required = false) String loginInfo,
                            @RequestParam(value = "password", required = false) String password, String verify, HttpServletRequest request) {
        try {
            //从session中获取随机数
            String random = (String) request.getSession().getAttribute(RandomValidateCodeUtil.RANDOMCODEKEY);
//            if (StringUtils.isBlank(verify)) {
//                return ResultData.result(false).setMsg("请输入验证码");
//            }
//            if (!random.equals(verify)) {
//                return ResultData.result(false).setMsg("请输入正确的验证码");
//            }
        } catch (Exception e) {
            logger.error("验证码校验失败", e);
            return ResultData.result(false).setMsg("验证码校验失败");
        }

        SysUser sysUser = userService.getWihtLogininfo(loginInfo);
        if (sysUser != null) {
            String encPassword = Md5Utils.encrypt(sysUser.getId().toString(), password);
            if (encPassword.equals(sysUser.getUserPassword())){
                Map<String, Object> dataMap = new HashMap<>();
                Long userId = sysUser.getId();
                //创建令牌
                String jwtToken = JwtUtil.sign(userId, encPassword);
                SysUserVo sysUserVo = userService.getbyUserId(userId);
                dataMap.put("token", jwtToken);
                dataMap.put("user", sysUserVo);
                return ResultData.result(true).setData(dataMap);
            }else {
                return ResultData.result(false).setMsg("用户密码错误！");
            }

        } else {
            return ResultData.result(false).setMsg("用户尚未注册~！");
        }

//        EncodeUtils.hexDecode()
//        byte[] bytePassword = DigestUtils.sha1(password.getBytes(), , Constants.PASSWORD_HASH_INTERATIONS);
//        String encodePassword = EncodeUtils.hexEncode(bytePassword);

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
