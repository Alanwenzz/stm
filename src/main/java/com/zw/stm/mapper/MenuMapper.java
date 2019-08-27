package com.zw.stm.mapper;

import com.zw.stm.pojo.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MenuMapper {
    List<Menu> getMenuByEmpuuid(Long id);
    Menu get(String id);

    void deleteAllUmenu(long rid);

    void insertUmenu(@Param("rid") long rid,@Param("mid") String mid);
}
