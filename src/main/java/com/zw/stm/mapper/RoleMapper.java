package com.zw.stm.mapper;

import com.zw.stm.pojo.Menu;
import com.zw.stm.pojo.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleMapper {
    List<Role> getList();
    List<Menu> readRoleMenus(long uuid);
    List<Role> readUserRoles(long uuid);
    void insertErole(@Param("id") long id,@Param("rid") long rid);
    void deleteAllErole(long id);
}
