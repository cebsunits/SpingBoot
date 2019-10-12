package com.tao.hai.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.tao.hai.bean.Log;
import com.tao.hai.bean.LoginUser;
import com.tao.hai.bean.User;
import com.tao.hai.facotry.LogFactory;
import com.tao.hai.service.LogService;
import com.tao.hai.service.LoginService;
import com.tao.hai.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

@Controller
public class LoginController {
    @Resource
    DefaultKaptcha defaultKaptcha;
    //验证码过期时间默认60秒
    @Value("${login.verifyTTL}")
    private long verifyTTL=60;
    @Autowired
    LoginService loginService;
    @Autowired
    LogService logService;
    @Autowired
    UserService userService;
    Logger logger= LoggerFactory.getLogger(LoginController.class);

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(){
        String loginName=loginService.getCurrentUserName();
        User user=userService.getUser(loginName);
        String userId=user.getUserId();


        return "index";
    }
    /**
     * 2、生成验证码
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/getVerifyCode")
    public void defaultKaptcha(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        byte[] bytesCaptchaImg = null;
        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
        try {
            // 生产验证码字符串并保存到session中
            String createText = defaultKaptcha.createText();
            request.getSession().setAttribute("verifyCode", createText);
            request.getSession().setAttribute("verifyCodeTTL", System.currentTimeMillis());
            // 使用生产的验证码字符串返回一个BufferedImage对象并转为byte写入到byte数组中
            BufferedImage bufferedImage = defaultKaptcha.createImage(createText);
            ImageIO.write(bufferedImage, "jpg", jpegOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // 定义response输出类型为image/jpeg类型，使用response输出流输出图片的byte数组
        bytesCaptchaImg = jpegOutputStream.toByteArray();
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        ServletOutputStream responseOutputStream = response.getOutputStream();
        responseOutputStream.write(bytesCaptchaImg);
        responseOutputStream.flush();
        responseOutputStream.close();
    }
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String toLogin() {
        Object principal = SecurityUtils.getSubject().getPrincipal();
        // 如果已经登录，则跳转到首页
        if(principal != null){
            return "redirect:/index";
        }
        return "login";
    }
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public Object login(LoginUser loginUser, HttpServletRequest request, Model model)throws Exception {
        // 登录名
        String loginName=loginUser.getLoginName();
        // 密码
        String encryptedPassword=loginUser.getPassword();
        String verifyCode =loginUser.getVerifyCode();
        // 验证码
        String rightCode = (String) request.getSession().getAttribute("verifyCode");
        Long verifyCodeTTL = (Long) request.getSession().getAttribute("verifyCodeTTL");
        Long currentMillis = System.currentTimeMillis();
        if (rightCode == null || verifyCodeTTL == null) {
            model.addAttribute("msg", "请刷新图片，输入验证码！");
            model.addAttribute("userName", loginName);
            model.addAttribute("success",false);
            model.addAttribute("url","/login");
            return model;
        }
        Long expiredTime = (currentMillis - verifyCodeTTL) / 1000;
        if (expiredTime > this.verifyTTL) {
            model.addAttribute("msg", "验证码过期，请刷新图片重新输入！");
            model.addAttribute("userName", loginName);
            model.addAttribute("success",false);
            model.addAttribute("url","/login");
            return model;
        }
        if (!verifyCode.equalsIgnoreCase(rightCode)) {
            model.addAttribute("msg", "验证码错误，请刷新图片重新输入！");
            model.addAttribute("userName", loginName);
            model.addAttribute("success",false);
            model.addAttribute("url","/login");
            return model;
        }

        loginUser = loginService.login(loginUser);
        if (loginUser.isLogin()) {
            model.addAttribute("userName", loginName);
            Log sysLog = LogFactory.createSysLog("登录","登录成功");
            logService.save(sysLog);
            model.addAttribute("success",true);
            model.addAttribute("url","/index");
            return model;
        } else {
            model.addAttribute("msg", loginUser.getResult());
            model.addAttribute("userName", loginName);
            model.addAttribute("success",false);
            model.addAttribute("url","/login");
            return model;
        }
    }
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(@RequestParam String loginName){
        Log sysLog = LogFactory.createSysLog("登出","登出成功");
        logService.save(sysLog);
        loginService.logout();
        return "redirect:/login";
    }
}
