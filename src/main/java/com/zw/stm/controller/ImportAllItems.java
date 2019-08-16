package com.zw.stm.controller;

import com.zw.stm.common.pojo.TaotaoResult;
import com.zw.stm.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ImportAllItems {
    @Autowired
    private SearchService service;
    // 导入所有的商品的数据到索引库中
    @RequestMapping("/index/importAll")
    @ResponseBody
    public TaotaoResult importAll() throws Exception{
        //1.引入服务
        //2.注入服务
        //3.调用方法
        return service.importAllSearchItems();
    }
}
