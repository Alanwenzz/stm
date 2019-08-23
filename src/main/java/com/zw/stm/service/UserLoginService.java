package com.zw.stm.service;

import com.zw.stm.common.pojo.TaotaoResult;

public interface UserLoginService {
    TaotaoResult login(String username,String password);
    TaotaoResult getUserByToken(String token);
    void loginOut(String token);
}
