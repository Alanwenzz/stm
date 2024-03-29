package com.zw.stm.controller;

import com.zw.stm.common.pojo.Ad1Node;
import com.zw.stm.common.util.JsonUtils;
import com.zw.stm.pojo.Menu;
import com.zw.stm.pojo.TbContent;
import com.zw.stm.pojo.TbUser;
import com.zw.stm.service.ContentService;
import com.zw.stm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class PageController {
    @Value("${AD1_CATEGORY_ID}")
    private long categoryId;
    @Value("${AD1_HEIGHT_B}")
    private String AD1_HEIGHT_B;

    @Value("${AD1_HEIGHT}")
    private String AD1_HEIGHT;

    @Value("${AD1_WIDTH}")
    private String AD1_WIDTH;

    @Value("${AD1_WIDTH_B}")
    private String AD1_WIDTH_B;

    private final ContentService contentService;
    private final UserService userService;
    private final HttpSession session;
    @Autowired
    public PageController(ContentService contentService, UserService userService, HttpSession session) {
        this.contentService = contentService;
        this.userService = userService;
        this.session = session;
    }

    //前台主页面
    @RequestMapping("main")
    public String main(Model model){
        List<TbContent> list=contentService.getContentListByCatId(categoryId);
        List<Ad1Node> nodes = new ArrayList<>();
        for (TbContent tbContent : list) {
            Ad1Node node = new Ad1Node();
            node.setAlt(tbContent.getSubTitle());
            node.setHeight(AD1_HEIGHT);
            node.setHeightB(AD1_HEIGHT_B);
            node.setHref(tbContent.getUrl());
            node.setSrc(tbContent.getPic());
            node.setSrcB(tbContent.getPic2());
            node.setWidth(AD1_WIDTH);
            node.setWidthB(AD1_WIDTH_B);
            nodes.add(node);
        }
        model.addAttribute("ad1",JsonUtils.objectToJson(nodes));
        return "main";
    }

    //后台主页面
    @RequestMapping("index")
    public String admin(Model model){
        //菜单
        //从0读取整个表
        TbUser user= (TbUser) session.getAttribute("userinfo");
        Menu menu = userService.readMenusByEmpuuid(user.getId());
        List<Menu> m=menu.getMenus();
        model.addAttribute("menu",m);
        return "index";
    }

    @RequestMapping("/")
    public String default_main(){
        return "redirect:/main";
    }

    @RequestMapping("/{page}")
    public String page(@PathVariable("page")String page){
        return page;
    }

    @RequestMapping("/page/{page}")
    public String showPage(@PathVariable("page")String page){
        return page;
    }

    @RequestMapping("/admin/{page}")
    public String adminPage(@PathVariable("page")String page){
        return "/admin/"+page;
    }

    @RequestMapping("/admin/login")
    public String admin_login(){
        return "admin/login";
    }

}
