package com.zw.stm.service.impl;

import com.zw.stm.common.pojo.TaotaoResult;
import com.zw.stm.common.util.JsonUtils;
import com.zw.stm.mapper.TbUserMapper;
import com.zw.stm.pojo.TbUser;
import com.zw.stm.pojo.TbUserExample;
import com.zw.stm.service.UserLoginService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class UserLoginServiceImpl implements UserLoginService {
    private final StringRedisTemplate stringRedisTemplate;
    private final TbUserMapper tbUserMapper;

    @Autowired
    public UserLoginServiceImpl(StringRedisTemplate stringRedisTemplate, TbUserMapper tbUserMapper) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.tbUserMapper = tbUserMapper;
    }

    @Override
    public TaotaoResult login(String username, String password) {
        //1.注入mapper
        //2.校验用户名和密码是否为空
        if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password)){
            return TaotaoResult.build(400, "用户名或密码错误");
        }
        //3.先校验用户名
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);
        List<TbUser> list = tbUserMapper.selectByExample(example);
        if(list==null || list.size()==0){
            return TaotaoResult.build(400, "用户名或密码错误");
        }
        //4.再校验密码
        TbUser user = list.get(0);
        //先加密密码再比较
        String md5DigestAsHex = DigestUtils.md5DigestAsHex(password.getBytes());
        if(!md5DigestAsHex.equals(user.getPassword())){//表示用户的密码不正确
            return TaotaoResult.build(400, "用户名或密码错误");
        }
        //5.如果校验成功
        //6.生成token : uuid生成    ，还需要设置token的有效性期来模拟session  用户的数据存放在redis  (key:token,value:用户的数据JSON)
        String token = UUID.randomUUID().toString();
        //存放用户数据到redis中，使用jedis的客户端,为了管理方便加一个前缀"kkk:token"
        //设置密码为空
        user.setPassword(null);
        ValueOperations<String,String> operation=stringRedisTemplate.opsForValue();
        //设置过期时间 来模拟session
        operation.set("userinfo:"+token, JsonUtils.objectToJson(user),30,TimeUnit.MINUTES);
        //7.把token设置cookie当中    在表现层设置
        return TaotaoResult.ok(token);
    }

    @Override
    public TaotaoResult getUserByToken(String token) {
        //2.调用根据token查询 用户信息（JSON）的方法   get方法
        String strjson = stringRedisTemplate.opsForValue().get("userinfo:"+token);
        //3.判断是否查询到
        if(StringUtils.isNotBlank(strjson)){
            //5.如果查询到  需要返回200  包含用户的信息  用户信息转成对象
            TbUser user = JsonUtils.jsonToPojo(strjson, TbUser.class);
            //重新设置过期时间
            stringRedisTemplate.expire("userinfo:"+token, 30,TimeUnit.MINUTES);
            return TaotaoResult.ok(user);
        }
        //4.如果查询不到 返回400
        return TaotaoResult.build(400, "用户已过期");
    }
}
