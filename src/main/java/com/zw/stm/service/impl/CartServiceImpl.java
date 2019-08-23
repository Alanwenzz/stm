package com.zw.stm.service.impl;

import com.zw.stm.common.pojo.TaotaoResult;
import com.zw.stm.common.util.JsonUtils;
import com.zw.stm.pojo.TbItem;
import com.zw.stm.service.CartService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public TaotaoResult addItemCart(TbItem item, Integer num, Long userId) {
        // 1.查询  可以根据 key 和field获取某一个商品
        TbItem tbItem = queryItemByItemIdAndUserId(item.getId(), userId);
        // 2.判断要添加的商品是否存在于列表中
        if(tbItem!=null){
            // 3.如果存在，直接数量相加
            tbItem.setNum(tbItem.getNum()+num);
            //图片只取一张
            //设置到redis
            redisTemplate.opsForHash().put("cartInfo"+":"+userId,item.getId().toString(), JsonUtils.objectToJson(tbItem));
        }else{
            // 4.如果不存在，直接添加到redis中
            //查询商品的数据 （商品的名称商品的价格，商品的图片。。） 调用商品的服务  直接从controller中传递
            //.设置商品的数量
            item.setNum(num);
            //.设置商品的图片为一张
            if(item.getImage()!=null){
                item.setImage(item.getImage().split(",")[0]);
            }
            //.设置到redis中
            redisTemplate.opsForHash().put("cartInfo"+":"+userId,item.getId().toString(), JsonUtils.objectToJson(item));
        }
        return TaotaoResult.ok();
    }

    private TbItem queryItemByItemIdAndUserId(Long itemId, Long userId) {
        String string = (String) redisTemplate.opsForHash().get("cartInfo"+":"+userId,itemId.toString());
        if(StringUtils.isNoneBlank(string)){
            return JsonUtils.jsonToPojo(string, TbItem.class);
        }
        return null;
    }

    @Override
    public List<TbItem> getCartList(Long userId) {
        String key="cartInfo"+":"+userId;
        Map<String, String> map = (Map<String, String>) redisTemplate.execute((RedisCallback<Map<String, String>>) con -> {
            Map<byte[], byte[]> result = con.hGetAll(key.getBytes());
            if (CollectionUtils.isEmpty(result)) {
                return new HashMap<>(0);
            }

            Map<String, String> ans = new HashMap<>(result.size());
            for (Map.Entry<byte[], byte[]> entry : result.entrySet()) {
                ans.put(new String(entry.getKey()), new String(entry.getValue()));
            }
            return ans;
        });

        //
        List<TbItem> list = new ArrayList<>();
        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String value = entry.getValue();// 商品的jSON数据
                // 转成POJO
                TbItem item = JsonUtils.jsonToPojo(value, TbItem.class);
                list.add(item);
            }
        }
        return list;
    }

    @Override
    public TaotaoResult updateItemCartByItemId(Long userId, Long itemId, Integer num) {
        //1.根据用户id和商品的id获取商品的对象
        TbItem tbItem = queryItemByItemIdAndUserId(itemId,userId);
        //判断是否存在
        if(tbItem!=null){
            //2.更新数量
            tbItem.setNum(num);
            //设置回redis中
            redisTemplate.opsForHash().put("cartInfo"+":"+userId, itemId.toString(), JsonUtils.objectToJson(tbItem));
        }//不管啦

        return TaotaoResult.ok();
    }

    @Override
    public TaotaoResult deleteItemCartByItemId(Long userId, Long itemId) {
        redisTemplate.opsForHash().delete("cartInfo"+":"+userId, itemId);
        return TaotaoResult.ok();
    }
}
