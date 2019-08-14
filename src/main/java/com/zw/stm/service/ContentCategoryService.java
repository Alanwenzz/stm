package com.zw.stm.service;

import com.zw.stm.common.pojo.EasyUITreeNode;
import com.zw.stm.common.pojo.TaotaoResult;

import java.util.List;

public interface ContentCategoryService {
    //通过节点ip查询该节点的叶子节点
    public List<EasyUITreeNode> getContentCategoryList(Long parentId);
    //添加节点
    public TaotaoResult createContentCategory(Long parentId,String name);
    //删除节点
    public TaotaoResult deleteContentCategory(Long id);
    //
    public TaotaoResult updateContentCategory(Long id,String name);
}
