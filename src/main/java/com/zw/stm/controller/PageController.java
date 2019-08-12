package com.zw.stm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {
    @RequestMapping("index")
    public String main(){
        return "index";
    }

    @RequestMapping("/{page}")
    public String page(@PathVariable("page")String page){
        return page;
    }
}
