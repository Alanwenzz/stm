package com.zw.stm.controller;

import com.zw.stm.common.pojo.TaotaoResult;
import com.zw.stm.common.util.CookieUtils;
import com.zw.stm.common.util.JsonUtils;
import com.zw.stm.service.UserLoginService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class UserLoginController {
    @Autowired
    private UserLoginService loginservice;
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
        }
        return result;
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
}
