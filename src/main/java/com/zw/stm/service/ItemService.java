package com.zw.stm.service;

import com.zw.stm.common.pojo.EasyUIDataGridResult;
import com.zw.stm.common.pojo.TaotaoResult;
import com.zw.stm.pojo.TbItem;
import com.zw.stm.pojo.TbItemDesc;

public interface ItemService {
    EasyUIDataGridResult getItemList(Integer page, Integer rows);
    TaotaoResult saveItem(TbItem item, String desc);
    TbItem getItemById(Long itemId);
    TbItemDesc getItemDescById(Long itemId);
}
