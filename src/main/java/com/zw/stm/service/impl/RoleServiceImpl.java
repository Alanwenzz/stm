package com.zw.stm.service.impl;

import com.zw.stm.mapper.MenuMapper;
import com.zw.stm.mapper.RoleMapper;
import com.zw.stm.pojo.Menu;
import com.zw.stm.pojo.Role;
import com.zw.stm.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private MenuMapper menuMapper;

    @Override
    public List<Role> getList() {
        return roleMapper.getList();
    }

    @Override
    public List<Menu> readRoleMenus(long uuid) {
        return roleMapper.readRoleMenus(uuid);
    }

    @Override
    public void updateRoleMenus(long rid, String checkedStr) {
        //清空当前用户所有菜单
        menuMapper.deleteAllUmenu(rid);
        //把mid分割出来
        String[] ids = checkedStr.split(",");
        //循环插入
        for(String mid:ids) {
            menuMapper.insertUmenu(rid, mid);
        }
    }

    @Override
    public List<Role> readUserRoles(long uuid) {
        return roleMapper.readUserRoles(uuid);
    }

    @Override
    public void updateEmpRoles(long id, String checkedStr) {
        //删除当前用户所有角色
        roleMapper.deleteAllErole(id);
        //把rid分割出来
        String[] ids = checkedStr.split(",");
        //循环插入
        for(String rid:ids) {
            roleMapper.insertErole(id,Long.parseLong(rid));
        }
    }
}
