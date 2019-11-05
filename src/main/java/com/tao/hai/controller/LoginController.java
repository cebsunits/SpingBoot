package com.tao.hai.controller;

import com.github.pagehelper.util.StringUtil;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.tao.hai.bean.Log;
import com.tao.hai.bean.LoginUser;
import com.tao.hai.bean.Menu;
import com.tao.hai.bean.User;
import com.tao.hai.facotry.LogFactory;
import com.tao.hai.json.AjaxError;
import com.tao.hai.json.AjaxJson;
import com.tao.hai.json.AjaxSuccess;
import com.tao.hai.service.LogService;
import com.tao.hai.service.LoginService;
import com.tao.hai.service.MenuService;
import com.tao.hai.service.UserService;
import org.apache.commons.lang3.StringUtils;
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
import java.util.Calendar;
import java.util.List;

@Controller
public class LoginController {
    @Resource
    DefaultKaptcha defaultKaptcha;
    //验证码过期时间默认60秒
    @Value("${login.verifyTTL}")
    private long verifyTTL=60;
    //密码过期时间天
    @Value("${login.password.expireDate}")
    private int passwordExpireDate=90;
    @Autowired
    LoginService loginService;
    @Autowired
    LogService logService;
    @Autowired
    UserService userService;
    @Autowired
    MenuService menuService;
    Logger logger= LoggerFactory.getLogger(LoginController.class);

    @RequestMapping(value = {"/","/index"}, method = RequestMethod.GET)
    public String index(Model model){
        /**验证是否登录*/
        User user = loginService.getCurrentUser();
        if (user==null) {
            return "login";
        }
        /**1、验证密码是否过期*/
        Calendar resetDate=Calendar.getInstance();
        if(user.getExpiredDate()!=null){
            resetDate.setTime(user.getExpiredDate());
        }
        resetDate.add(Calendar.DAY_OF_YEAR,passwordExpireDate);
        Calendar nowDate=Calendar.getInstance();
        if(resetDate.before(nowDate)){
            model.addAttribute("message", "您的密码已过期，请重新设置密码");
            return "user/userPasswordReset";
        }
        /**用户ID*/
        String userId=user.getUserId();
        List<Menu> menuList=menuService.getUserList(userId);
        model.addAttribute("user", user);
        model.addAttribute("menuList", menuList);

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
        AjaxJson ajaxJson;
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
            ajaxJson=new AjaxError();
            ajaxJson.setMessage("请刷新图片，输入验证码！");
            return ajaxJson;
        }
        Long expiredTime = (currentMillis - verifyCodeTTL) / 1000;
        if (expiredTime > this.verifyTTL) {
            ajaxJson=new AjaxError();
            ajaxJson.setMessage("验证码过期，请刷新图片重新输入！");
            return ajaxJson;
        }
        if (!verifyCode.equalsIgnoreCase(rightCode)) {
            ajaxJson=new AjaxError();
            ajaxJson.setMessage("验证码错误，请刷新图片重新输入！");
            return ajaxJson;
        }

        loginUser = loginService.login(loginUser);
        if (loginUser.isLogin()) {
            Log sysLog = LogFactory.createSysLog("登录","登录成功");
            logService.save(sysLog);
            ajaxJson=new AjaxSuccess();
            ajaxJson.setMessage("登录成功！");
            return ajaxJson;
        } else {
            ajaxJson=new AjaxError();
            ajaxJson.setMessage(loginUser.getResult());
            return ajaxJson;
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
