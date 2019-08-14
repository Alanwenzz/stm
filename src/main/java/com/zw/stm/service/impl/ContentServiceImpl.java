package com.zw.stm.service.impl;

import com.zw.stm.common.pojo.TaotaoResult;
import com.zw.stm.mapper.TbContentMapper;
import com.zw.stm.pojo.TbContent;
import com.zw.stm.pojo.TbContentExample;
import com.zw.stm.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {
    @Autowired
    private TbContentMapper mapper;
    @Override
    public List<TbContent> getContentListByCatId(Long id) {

        TbContentExample example=new TbContentExample();
        example.createCriteria().andCategoryIdEqualTo(id);
        List<TbContent> list=mapper.selectByExample(example);
        return list;
    }

    @Override
    public TaotaoResult saveContent(TbContent content) {
        //1.注入mapper

        //2.补全其他的属性
        content.setCreated(new Date());
        content.setUpdated(content.getCreated());
        //3.插入内容表中
        mapper.insertSelective(content);

        return TaotaoResult.ok();
    }
}
