package com.zw.stm.listen;

import com.zw.stm.common.pojo.TaotaoResult;
import com.zw.stm.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class ItemChangeMessageListener {
    @Autowired
    private SearchService service;

    @JmsListener(destination = "topicDestination", containerFactory = "jmsTopicListenerContainerFactory")
    public void onMessage(String msg) {
        try {
            Long itemId = Long.parseLong(msg);
            System.out.println(msg);
            //通过商品的id查询数据   需要开发mapper 通过id查询商品(搜索时)的数据
            //更新索引库
            TaotaoResult taotaoResult = service.updateSearchItemById(itemId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
