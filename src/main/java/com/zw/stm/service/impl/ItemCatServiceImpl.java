package com.zw.stm.service.impl;

import com.zw.stm.common.EasyUITreeNode;
import com.zw.stm.mapper.TbItemCatMapper;
import com.zw.stm.pojo.TbItemCatExample.Criteria;
import com.zw.stm.pojo.TbItemCat;
import com.zw.stm.pojo.TbItemCatExample;
import com.zw.stm.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemCatServiceImpl implements ItemCatService {
    private final TbItemCatMapper tbItemCatMapper;

    @Autowired
    public ItemCatServiceImpl(TbItemCatMapper tbItemCatMapper) {
        this.tbItemCatMapper = tbItemCatMapper;
    }

    @Override
    public List<EasyUITreeNode> getItemCatListByParentId(Long parentId) {
        //1.注入mapper
        //2.创建example
        TbItemCatExample example = new TbItemCatExample();
        //3.设置查询的条件
        Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        //4.执行查询  list<ibitemCat>
        List<TbItemCat> list = tbItemCatMapper.selectByExample(example);
        //5.转成需要的数据类型List<EasyUITreeNode>
        List<EasyUITreeNode> nodes = new ArrayList<>();
        for (TbItemCat cat : list) {
            EasyUITreeNode node = new EasyUITreeNode();
            node.setId(cat.getId());
            node.setText(cat.getName());
            node.setState(cat.getIsParent()?"closed":"open");//"open",closed
            nodes.add(node);
        }
        //6.返回
        return nodes;
    }
}
