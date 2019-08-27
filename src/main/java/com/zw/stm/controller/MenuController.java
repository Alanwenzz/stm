package com.zw.stm.controller;

import com.zw.stm.pojo.Menu;
import com.zw.stm.pojo.TbUser;
import com.zw.stm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class MenuController {
    @Autowired
    private HttpSession session;
    @Autowired
    private UserService userService;

    @ResponseBody
    @RequestMapping("menu_getMenuTree")
    public Menu getMenuTree(){
        //从0读取整个表
        TbUser user =(TbUser) session.getAttribute("userinfo");
        Menu menu = userService.readMenusByEmpuuid(user.getId());
        return menu;
    }
}
