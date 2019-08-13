package com.zw.stm.controller;

import com.zw.stm.common.pojo.EasyUITreeNode;
import com.zw.stm.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ItemCatController {
    private final ItemCatService itemCatService;

    @Autowired
    public ItemCatController(ItemCatService itemCatService) {
        this.itemCatService = itemCatService;
    }

    @RequestMapping("/item/cat/list")
    @ResponseBody
    public List<EasyUITreeNode> getItemCatList(@RequestParam(value="id",defaultValue="0") Long parentId){
        //1.引入服务
        //2.注入服务
        //3.调用方法
        return itemCatService.getItemCatListByParentId(parentId);
    }
}
