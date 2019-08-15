package com.zw.stm.controller;

import com.zw.stm.common.pojo.TaotaoResult;
import com.zw.stm.pojo.TbContent;
import com.zw.stm.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ContentController {
    @Autowired
    private ContentService contentservcie;

    //	$.post("/content/save",$("#contentAddForm").serialize(), function(data){
    //返回值是JSON
    @RequestMapping(value="/content/save",method=RequestMethod.POST)
    @ResponseBody
    public TaotaoResult saveContent(TbContent tContent){
        //1.引入服务
        //2.注入服务
        //3.调用
        contentservcie.saveContent(tContent);
        return TaotaoResult.ok();
    }

    @RequestMapping(value="/content/query/list",method=RequestMethod.GET)
    @ResponseBody
    public List<TbContent> listContent(@RequestParam(value="categoryId",defaultValue="0") Long id, Integer page, Integer rows){
        //1.引入服务
        //2.注入服务
        //3.调用
        return contentservcie.getContentListByCatId(id);
    }

    @RequestMapping(value="/content/delete")
    @ResponseBody
    public TaotaoResult deleteContent(Long ids){
        //1.引入服务
        //2.注入服务
        //3.调用
        TbContent content=contentservcie.getContentById(ids);
        contentservcie.deleteContent(ids,content.getCategoryId());
        return TaotaoResult.ok();
    }
}
