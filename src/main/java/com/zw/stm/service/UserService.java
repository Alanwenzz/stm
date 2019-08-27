package com.zw.stm.service;

import com.zw.stm.common.pojo.EasyUIDataGridResult;
import com.zw.stm.pojo.Menu;

public interface UserService {
    Menu readMenusByEmpuuid(Long id);
    EasyUIDataGridResult getItemList(Integer page, Integer rows);
}
