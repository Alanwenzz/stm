package com.zw.stm.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zw.stm.common.pojo.EasyUIDataGridResult;
import com.zw.stm.mapper.MenuMapper;
import com.zw.stm.mapper.TbUserMapper;
import com.zw.stm.pojo.*;
import com.zw.stm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private TbUserMapper userMapper;
    @Autowired
    private MenuMapper menuMapper;

    @Override
    public Menu readMenusByEmpuuid(Long id) {
        //获取所用的菜单
        Menu root=menuMapper.get("-1");
        //用户下菜单集合
        List<Menu> lMenus=menuMapper.getMenuByEmpuuid(id);
        //根菜单
        Menu menu=cloneMenu(root);

        //循环匹配模板
        Menu _m1=null;
        Menu _m2=null;
        for(Menu m1 : root.getMenus()) {
            _m1=cloneMenu(m1);//m1下有无子菜单不知道
            //二级菜单循环
            for(Menu m2 :m1.getMenus()) {

                if(lMenus.contains(m2)){
                    //复制菜单
                    _m2 = cloneMenu(m2);
                    //加入到上级菜单下
                    _m1.getMenus().add(_m2);
                }

            }

            //有二级菜单加进去
            if(_m1.getMenus().size()>0) {
                menu.getMenus().add(_m1);
            }
        }

        return menu;
    }

    @Override
    public EasyUIDataGridResult getItemList(Integer page, Integer rows) {
        //设置分页信息
        if(page==null)page=1;
        if(rows==null)rows=30;
        PageHelper.startPage(page,rows);
        //注入mapper
        TbUserExample example=new TbUserExample();
        List<TbUser> list=userMapper.selectByExample(example);
        //获取分页信息
        PageInfo<TbUser> info=new PageInfo<>(list);
        //封装到EasyUIDataGridResult
        EasyUIDataGridResult result=new EasyUIDataGridResult();
        result.setTotal((int)info.getTotal());
        result.setRows(info.getList());
        return result;
    }

    private Menu cloneMenu(Menu src){
        Menu menu = new Menu();
        menu.setIcon(src.getIcon());
        menu.setMenuid(src.getMenuid());
        menu.setMenuname(src.getMenuname());
        menu.setUrl(src.getUrl());
        menu.setPid(src.getPid());
        menu.setMenus(new ArrayList<Menu>());
        return menu;
    }
}
