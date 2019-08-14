package com.zw.stm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {
    //前台主页面
    @RequestMapping("main")
    public String main(){
        return "main";
    }

    //后台主页面
    @RequestMapping("index")
    public String admin(){
        return "index";
    }

    @RequestMapping("/")
    public String default_main(){
        return "main";
    }

    @RequestMapping("/{page}")
    public String page(@PathVariable("page")String page){
        return page;
    }

}
