package com.zw.stm.service;

import com.zw.stm.common.pojo.EasyUIDataGridResult;
import com.zw.stm.common.pojo.TaotaoResult;
import com.zw.stm.pojo.TbItem;

public interface ItemService {
    public EasyUIDataGridResult getItemList(Integer page, Integer rows);
    public TaotaoResult saveItem(TbItem item, String desc);
}
