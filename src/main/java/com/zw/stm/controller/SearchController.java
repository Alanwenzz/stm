package com.zw.stm.controller;

import com.zw.stm.common.pojo.SearchResult;
import com.zw.stm.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SearchController {
    @Value("${ITEM_ROWS}")
    private Integer ITEM_ROWS;
    @Autowired
    private SearchService searchService;

    @RequestMapping("/solr/search")
    public String search(@RequestParam(defaultValue = "1") Integer page,@RequestParam(value = "q")String queryString, Model model) throws Exception{

        SearchResult result=searchService.searchSet(queryString,page,ITEM_ROWS);
        //4.设置数据传递到jsp中
        model.addAttribute("query", queryString);
        model.addAttribute("totalPages", result.getPageCount());//总页数
        model.addAttribute("itemList", result.getItemList());
        model.addAttribute("page", page);
        return "search";
    }
}
