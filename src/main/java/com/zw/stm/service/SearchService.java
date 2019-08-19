package com.zw.stm.service;

import com.zw.stm.common.pojo.SearchResult;
import com.zw.stm.common.pojo.TaotaoResult;
import org.apache.solr.client.solrj.SolrQuery;

public interface SearchService {
    //导入所有商品到索引库中
    TaotaoResult importAllSearchItems() throws Exception;
    //搜索 设置条件
    SearchResult searchSet(String queryString, Integer page, Integer rows) throws Exception;
    //搜索 从solr中搜索
    SearchResult search(SolrQuery query) throws Exception;
    //根据id查询item
    TaotaoResult updateSearchItemById(Long itemId) throws Exception;
}
