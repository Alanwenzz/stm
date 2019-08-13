package com.zw.stm.controller;

import com.zw.stm.common.pojo.EasyUIDataGridResult;
import com.zw.stm.common.pojo.TaotaoResult;
import com.zw.stm.pojo.TbItem;
import com.zw.stm.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ItemController {
    @Autowired
    private ItemService itemService;

    @RequestMapping("/item/list")
    @ResponseBody
    public EasyUIDataGridResult getItemList(Integer page,Integer rows){
        return itemService.getItemList(page,rows);
    }

    @RequestMapping(value="/item/save",method=RequestMethod.POST)
    @ResponseBody
    public TaotaoResult saveItem(TbItem item, String desc){
		//1.引入服务
        //2.注入服务
        //3.调用
        return itemService.saveItem(item, desc);
    }
}
