package com.zw.stm.service;

import com.zw.stm.common.EasyUIDataGridResult;

public interface ItemService {
    public EasyUIDataGridResult getItemList(Integer page, Integer rows);
}
