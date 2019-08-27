package com.zw.stm.controller;

import com.zw.stm.common.pojo.EasyUIDataGridResult;
import com.zw.stm.common.pojo.Tree;
import com.zw.stm.pojo.Role;
import com.zw.stm.service.RoleService;
import com.zw.stm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;


@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    //分页查询
    @ResponseBody
    @RequestMapping("/user/getListByPage")
    public EasyUIDataGridResult getItemList(Integer page, Integer rows){
        return userService.getItemList(page,rows);
    }

    //查询用户对应的角色
    @ResponseBody
    @RequestMapping("/user/readEmpRoles")
    public List<Tree> readEmpRoles(long id){
        List<Role> allRoles=roleService.getList();
        List<Role> lRoles=roleService.readUserRoles(id);
        List<Tree> lTrees=new ArrayList<Tree>();
        for(Role r:allRoles) {
            Tree tree=new Tree();
            tree.setId(r.getRid().toString());
            tree.setText(r.getRolename());
            if(lRoles.contains(r)) {
                tree.setChecked(true);
            }
            lTrees.add(tree);
        }
        return lTrees;
    }

    //更新员工角色
    @ResponseBody
    @RequestMapping("/user/updateEmpRoles")
    public int updateEmpRoles(long id,String checkedStr) {
        //更新员工角色
        try {
            roleService.updateEmpRoles(id,checkedStr);
            return 1;
        } catch (Exception e) {
            // TODO: handle exception
            return 0;
        }
    }
}
