package com.zw.stm.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zw.stm.common.EasyUIDataGridResult;
import com.zw.stm.mapper.TbItemMapper;
import com.zw.stm.pojo.TbItem;
import com.zw.stm.pojo.TbItemExample;
import com.zw.stm.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private TbItemMapper tbItemMapper;

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
}
