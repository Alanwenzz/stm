package com.zw.stm.service;

import com.zw.stm.common.pojo.TaotaoResult;
import com.zw.stm.pojo.TbContent;

import java.util.List;

public interface ContentService {
    public List<TbContent> getContentListByCatId(Long id);

    public TaotaoResult saveContent(TbContent tContent);
}
