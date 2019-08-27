package com.zw.stm.controller;

import com.zw.stm.common.pojo.TaotaoResult;
import com.zw.stm.common.util.CookieUtils;
import com.zw.stm.common.util.JsonUtils;
import com.zw.stm.pojo.Menu;
import com.zw.stm.pojo.TbUser;
import com.zw.stm.service.UserLoginService;
import com.zw.stm.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class UserLoginController {
    private final UserLoginService loginservice;
    private final HttpSession session;

    @Autowired
    public UserLoginController(UserLoginService loginservice, HttpSession session, UserService userService) {
        this.loginservice = loginservice;
        this.session = session;
    }

    /**
     * url:/user/login
     * 参数：username password
     * 返回值：json
     * 请求限定的方法：post
     */
    @RequestMapping(value="/user/login",method=RequestMethod.POST)
    @ResponseBody
    public TaotaoResult login(HttpServletRequest request, HttpServletResponse response, String username, String password){
        //1.引入服务
        //2.注入服务
        //3.调用服务
        TaotaoResult result = loginservice.login(username, password);
        //4.需要设置token到cookie中 可以使用 工具类  cookie需要跨域
        if(result.getStatus()==200){
            CookieUtils.setCookie(request, response,"TT_TOKEN", result.getData().toString());
            TbUser user= (TbUser) loginservice.getUserByToken(result.getData().toString()).getData();
            session.setAttribute("userinfo",user);

        }
        return result;
    }

    //显示名字
    @ResponseBody
    @RequestMapping("/login_showName")
    public Map<String, Object> showName(){
        Map<String, Object> rtn;
        //从session中取值
        TbUser user= (TbUser) session.getAttribute("userinfo");
        //session值 显示
        if(null != user){
            rtn=ajaxReturn(true, user.getUsername());
        }else{
            rtn=ajaxReturn(false, "");
        }
        return rtn;
    }

    //ajax返回
    public Map<String, Object> ajaxReturn(boolean success, String message){
        //
        Map<String, Object> rtn = new HashMap<String, Object>();
        rtn.put("success",success);
        rtn.put("message",message);
        return rtn;
    }

    @RequestMapping(value="/user/token/{token}",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String getUserByToken(@PathVariable String token){

//        //判断是否是Jsonp请求
//        if(StringUtils.isNotBlank(callback)){
//            //如果是jsonp 需要拼接 类似于fun({id:1});
//            TaotaoResult result = loginservice.getUserByToken(token);
//            String jsonpstr = callback+"("+JsonUtils.objectToJson(result)+")";
//            return jsonpstr;
//        }
//        //如果不是jsonp
        //1.调用服务
        TaotaoResult result = loginservice.getUserByToken(token);
        return JsonUtils.objectToJson(result);
    }

    @RequestMapping("/user/logout")
    public String loginOut(HttpServletRequest request){
        String token=CookieUtils.getCookieValue(request,"TT_TOKEN");
        loginservice.loginOut(token);
        session.invalidate();
        return "login";
    }

    @ResponseBody
    @RequestMapping("/admin/logout")
    public int admin_loginOut(HttpServletRequest request){
        String token=CookieUtils.getCookieValue(request,"TT_TOKEN");
        loginservice.loginOut(token);
        session.invalidate();
        return 0;
    }
}
