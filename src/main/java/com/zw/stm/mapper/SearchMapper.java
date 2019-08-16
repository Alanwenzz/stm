package com.zw.stm.mapper;

import com.zw.stm.common.pojo.SearchItem;

import java.util.List;

public interface SearchMapper {
    //查询所有数据
    List<SearchItem> getSearchItemList();
}
