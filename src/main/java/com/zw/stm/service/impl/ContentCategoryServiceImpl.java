package com.zw.stm.service.impl;

import com.zw.stm.common.pojo.EasyUITreeNode;
import com.zw.stm.common.pojo.TaotaoResult;
import com.zw.stm.mapper.TbContentCategoryMapper;
import com.zw.stm.pojo.TbContentCategory;
import com.zw.stm.pojo.TbContentCategoryExample;
import com.zw.stm.pojo.TbContentCategoryExample.*;
import com.zw.stm.service.ContentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {
    private final TbContentCategoryMapper tbContentCategoryMapper;

    @Autowired
    public ContentCategoryServiceImpl(TbContentCategoryMapper tbContentCategoryMapper) {
        this.tbContentCategoryMapper = tbContentCategoryMapper;
    }

    @Override
    public List<EasyUITreeNode> getContentCategoryList(Long parentId) {

        TbContentCategoryExample example=new TbContentCategoryExample();
        //设置条件
        Criteria criteria=example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        //执行查询
        List<TbContentCategory> list=tbContentCategoryMapper.selectByExample(example);
        //转成EasyUITreeNode
        List<EasyUITreeNode> nodes=new ArrayList<>();
        for (TbContentCategory tbContentCategory:list){
            EasyUITreeNode node=new EasyUITreeNode();
            node.setId(tbContentCategory.getId());
            node.setState(tbContentCategory.getIsParent()?"closed":"open");
            node.setText(tbContentCategory.getName());
            nodes.add(node);
        }
        return nodes;
    }

    @Override
    public TaotaoResult createContentCategory(Long parentId, String name) {
        //1.构建对象  补全其他的属性
        TbContentCategory category = new TbContentCategory();
        category.setCreated(new Date());
        category.setIsParent(false);//新增的节点都是叶子节点
        category.setName(name);
        category.setParentId(parentId);
        category.setSortOrder(1);
        category.setStatus(1);
        category.setUpdated(category.getCreated());
        //2.插入contentcategory表数据
        tbContentCategoryMapper.insertSelective(category);
        //3.返回taotaoresult 包含内容分类的id   需要主键返回

        //判断如果要添加的节点的父节点本身叶子节点  需要更新其为父节点
        TbContentCategory parent = tbContentCategoryMapper.selectByPrimaryKey(parentId);
        if(!parent.getIsParent()){//原本就是叶子节点
            parent.setIsParent(true);
            tbContentCategoryMapper.updateByPrimaryKeySelective(parent);//更新节点的is_parent属性为true
        }

        return TaotaoResult.ok(category);
    }

    @Override
    public TaotaoResult deleteContentCategory(Long id) {
        // TODO Auto-generated method stub
        tbContentCategoryMapper.deleteByPrimaryKey(id);
        return TaotaoResult.ok();
    }

    @Override
    public TaotaoResult updateContentCategory(Long id, String name) {
        TbContentCategory tbContentCategory=new TbContentCategory();
        tbContentCategory.setId(id);
        tbContentCategory.setName(name);
        tbContentCategory.setUpdated(new Date());
        tbContentCategoryMapper.updateByPrimaryKeySelective(tbContentCategory);
        return TaotaoResult.ok();
    }
}
