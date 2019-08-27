package com.zw.stm.controller;

import com.zw.stm.common.pojo.Tree;
import com.zw.stm.pojo.Menu;
import com.zw.stm.pojo.Role;
import com.zw.stm.service.MenuService;
import com.zw.stm.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
public class RoleController {
    @Autowired
    private RoleService roleService;
    @Autowired
    private MenuService menuService;

    @ResponseBody
    @RequestMapping("/role/getList")
    public List<Role> getList(){
        return roleService.getList();
    }

    @ResponseBody
    @RequestMapping("/role/updateRoleMenus")
    public int updateRoleMenus(long id,String checkedStr){
        //更新角色拥有的菜单
        try {
            roleService.updateRoleMenus(id,checkedStr);
            return 1;
        } catch (Exception e) {
            // TODO: handle exception
            return 0;
        }
    }


    @ResponseBody
    @RequestMapping("/role/readRoleMenus")
    public List<Tree> readRoleMenus(long id){
        //所有菜单
        Menu menu=menuService.get("-1");
        //角色拥有的菜单
        List<Menu> lMenus=roleService.readRoleMenus(id);

        List<Tree> lTrees=new ArrayList<Tree>();
        //一级菜单
        for(Menu m1: menu.getMenus()) {
            Tree tree1=new Tree();
            tree1.setText(m1.getMenuname());
            tree1.setId(m1.getMenuid());
            //二级菜单
            for(Menu m2:m1.getMenus()) {
                Tree tree2=new Tree();
                tree2.setId(m2.getMenuid());
                tree2.setText(m2.getMenuname());
                //用户包含当前菜单勾选上
                if(lMenus.contains(m2)) {
                    tree2.setChecked(true);
                }
                tree1.getChildren().add(tree2);
            }
            lTrees.add(tree1);
        }

        return lTrees;
    }
}
