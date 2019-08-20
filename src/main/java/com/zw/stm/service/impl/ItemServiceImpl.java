package com.zw.stm.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zw.stm.common.pojo.EasyUIDataGridResult;
import com.zw.stm.common.pojo.TaotaoResult;
import com.zw.stm.common.util.IDUtils;
import com.zw.stm.mapper.TbItemDescMapper;
import com.zw.stm.mapper.TbItemMapper;
import com.zw.stm.pojo.TbItem;
import com.zw.stm.pojo.TbItemDesc;
import com.zw.stm.pojo.TbItemExample;
import com.zw.stm.service.ItemService;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.util.Date;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
    private final TbItemMapper tbItemMapper;
    private final TbItemDescMapper tbItemDescMapper;
    private final JmsMessagingTemplate jmsMessagingTemplate;

    @Autowired
    public ItemServiceImpl(TbItemMapper tbItemMapper, TbItemDescMapper tbItemDescMapper, JmsMessagingTemplate jmsMessagingTemplate) {
        this.tbItemMapper = tbItemMapper;
        this.tbItemDescMapper = tbItemDescMapper;
        this.jmsMessagingTemplate = jmsMessagingTemplate;
    }

    @Override
    public EasyUIDataGridResult getItemList(Integer page, Integer rows) {
        //设置分页信息
        if(page==null)page=1;
        if(rows==null)rows=30;
        PageHelper.startPage(page,rows);
        //注入mapper
        TbItemExample example=new TbItemExample();
        List<TbItem> list=tbItemMapper.selectByExample(example);
        //获取分页信息
        PageInfo<TbItem> info=new PageInfo<>(list);
        //封装到EasyUIDataGridResult
        EasyUIDataGridResult result=new EasyUIDataGridResult();
        result.setTotal((int)info.getTotal());
        result.setRows(info.getList());
        return result;
    }

    @Override
    @Caching(evict={@CacheEvict(value="itemInfoBase",key = "#item.id",condition="#item.id>0"),
            @CacheEvict(value="itemInfoDesc",key = "#item.id",condition="#item.id>0")})
    public TaotaoResult saveItem(TbItem item, String desc) {
        //生成商品的id
        final long itemId = IDUtils.genItemId();
        //1.补全item 的其他属性
        item.setId(itemId);
        item.setCreated(new Date());
        //1-正常，2-下架，3-删除',
        item.setStatus((byte) 1);
        item.setUpdated(item.getCreated());
        //2.插入到item表 商品的基本信息表
        tbItemMapper.insertSelective(item);
        //3.补全商品描述中的属性
        TbItemDesc desc2 = new TbItemDesc();
        desc2.setItemDesc(desc);
        desc2.setItemId(itemId);
        desc2.setCreated(item.getCreated());
        desc2.setUpdated(item.getCreated());
        //4.插入商品描述数据
        //注入tbitemdesc的mapper
        tbItemDescMapper.insertSelective(desc2);

        //添加发送业务的逻辑
        Destination destination = new ActiveMQTopic("topicDestination");
        jmsMessagingTemplate.convertAndSend(destination,itemId+"");

        //5.返回taotaoresult
        return TaotaoResult.ok();
    }

    @Override
    @Cacheable(value="itemInfoBase",key = "#itemId",condition="#itemId>0")
    public TbItem getItemById(Long itemId) {
        return tbItemMapper.selectByPrimaryKey(itemId);
    }

    @Override
    @Cacheable(value="itemInfoDesc",key = "#itemId",condition="#itemId>0")
    public TbItemDesc getItemDescById(Long itemId) {
        return tbItemDescMapper.selectByPrimaryKey(itemId);
    }

}
