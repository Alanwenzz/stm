package com.zw.stm.service.impl;

import com.zw.stm.common.pojo.TaotaoResult;
import com.zw.stm.mapper.TbContentMapper;
import com.zw.stm.pojo.TbContent;
import com.zw.stm.pojo.TbContentExample;
import com.zw.stm.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {
    private final TbContentMapper mapper;

    @Autowired
    public ContentServiceImpl(TbContentMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    @Cacheable(value="content",key = "#id",condition="#id>0")
    public List<TbContent> getContentListByCatId(Long id) {

        TbContentExample example=new TbContentExample();
        example.createCriteria().andCategoryIdEqualTo(id);
        return mapper.selectByExample(example);
    }

    @Override
    @CacheEvict(value = "content",key="#content.categoryId")
    public TbContent saveContent(TbContent content) {
        //1.注入mapper

        //2.补全其他的属性
        content.setCreated(new Date());
        content.setUpdated(content.getCreated());
        //3.插入内容表中
        mapper.insertSelective(content);
        return content;
    }

    @Override
    @CacheEvict(value = "content",key="#categoryId")
    public void deleteContent(Long ids,Long categoryId) {
        mapper.deleteByPrimaryKey(ids);
    }

    @Override
    public TbContent getContentById(Long id) {
        return mapper.selectByPrimaryKey(id);
    }
}
