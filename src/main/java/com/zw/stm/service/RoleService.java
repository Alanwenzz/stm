package com.zw.stm.service;

import com.zw.stm.pojo.Menu;
import com.zw.stm.pojo.Role;

import java.util.List;

public interface RoleService {
    List<Role> getList();
    List<Menu> readRoleMenus(long uuid);
    void updateRoleMenus(long rid,String checkedStr);
    List<Role> readUserRoles(long uuid);
    void updateEmpRoles(long id, String checkedStr);
}
