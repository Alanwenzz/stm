package com.zw.stm.controller;

import com.zw.stm.common.pojo.EasyUITreeNode;
import com.zw.stm.common.pojo.TaotaoResult;
import com.zw.stm.service.ContentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ContentCategoryController {
    private final ContentCategoryService contentCategoryService;

    @Autowired
    public ContentCategoryController(ContentCategoryService contentCategoryService) {
        this.contentCategoryService = contentCategoryService;
    }

    //获取分类列表
    @ResponseBody
    @RequestMapping(value="/content/category/list",method = RequestMethod.GET)
    public List<EasyUITreeNode> getContentCategoryList(@RequestParam(value = "id",defaultValue = "0")Long parentId){
        return contentCategoryService.getContentCategoryList(parentId);
    }

    //添加新的节点
    @RequestMapping(value="/content/category/create",method=RequestMethod.POST)
    @ResponseBody
    public TaotaoResult createContentCategory(Long parentId, String name){
        return contentCategoryService.createContentCategory(parentId, name);
    }

    //删除节点
    @RequestMapping(value="/content/category/delete",method=RequestMethod.POST)
    @ResponseBody
    public TaotaoResult deleteContentCategory(@RequestParam(value="id",defaultValue="0")Long id){
        return contentCategoryService.deleteContentCategory(id);
    }

    //更新节点
    @RequestMapping(value="/content/category/update",method=RequestMethod.POST)
    @ResponseBody
    public TaotaoResult updateContentCategory(Long id,String name){
        return contentCategoryService.updateContentCategory(id,name);
    }
}
