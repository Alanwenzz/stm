package com.zw.stm.service.impl;

import com.zw.stm.mapper.MenuMapper;
import com.zw.stm.pojo.Menu;
import com.zw.stm.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MenuServiceImpl implements MenuService {
    @Autowired
    private MenuMapper menuMapper;

    @Override
    public Menu get(String mid) {
        return menuMapper.get(mid);
    }
}
