package com.zw.stm.service;

import com.zw.stm.common.EasyUITreeNode;

import java.util.List;

public interface ItemCatService {
    List<EasyUITreeNode> getItemCatListByParentId(Long parentId);
}
