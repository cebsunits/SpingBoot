package com.tao.hai.config;

import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.util.StringUtils;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * 统一捕捉shiro的异常，返回给前台一个json信息，前台根据这个信息显示对应的提示，或者做页面的跳转。
 */
public class UnauthorizedExceptionResolver implements HandlerExceptionResolver {
    //不满足@RequiresGuest注解时抛出的异常信息
    private static final String GUEST_ONLY = "Attempting to perform a guest-only operation";

    public ModelAndView resolveException(HttpServletRequest request,
                                         HttpServletResponse response, Object handler, Exception ex) {
        // TODO Auto-generated method stub  
        System.out.println("==============异常开始=============");
        //如果是shiro无权操作，因为shiro 在操作auno等一部分不进行转发至无权限url  
        if (ex instanceof UnauthenticatedException) {
            ModelAndView mv = new ModelAndView("/401");
            String eMsg = ex.getMessage();
            if (StringUtils.startsWithIgnoreCase(eMsg,GUEST_ONLY)){
                mv.addObject("exception","只允许游客访问，若您已登录，请先退出登录");
            }else{
                mv.addObject("exception","用户未登录");
            }
            return mv;
        }else if (ex instanceof UnauthorizedException) {
            ModelAndView mv = new ModelAndView("/403");
            mv.addObject("exception","用户没有访问权限");
            return mv;
        }else{
            ex.printStackTrace();
            ModelAndView mv = new ModelAndView("/500");
            mv.addObject("exception", ex.toString().replaceAll("\n", "<br/>"));
            return mv;
        }
    }
}