package com.zw.stm.service;

import com.zw.stm.common.pojo.TaotaoResult;
import com.zw.stm.pojo.TbItem;

import java.util.List;

public interface CartService {
    //添加购物车
    TaotaoResult addItemCart(TbItem item, Integer num, Long userId);
    //根据用户的ID 查询用户的购物车的列表
    List<TbItem> getCartList(Long userId);
    //根据商品的ID 更新数量
    TaotaoResult updateItemCartByItemId(Long userId,Long itemId,Integer num);
    //根据商品的ID 删除商品
    TaotaoResult deleteItemCartByItemId(Long userId,Long itemId);
}
