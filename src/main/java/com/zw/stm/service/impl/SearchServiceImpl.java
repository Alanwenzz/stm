package com.zw.stm.service.impl;

import com.zw.stm.common.pojo.SearchItem;
import com.zw.stm.common.pojo.SearchResult;
import com.zw.stm.common.pojo.TaotaoResult;
import com.zw.stm.mapper.SearchMapper;
import com.zw.stm.service.SearchService;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SearchServiceImpl implements SearchService {
    private final SearchMapper mapper;
    private final SolrClient solrClient;

    @Autowired
    public SearchServiceImpl(SearchMapper mapper, SolrClient solrClient) {
        this.mapper = mapper;
        this.solrClient = solrClient;
    }

    @Override
    public TaotaoResult importAllSearchItems() throws Exception{
        List<SearchItem> list= mapper.getSearchItemList();
        //通过solrj 将数据写入到索引库中
        //创建solrinputdocument  将 列表中的元素一个个放到索引库中
        for (SearchItem searchItem : list) {
            SolrInputDocument document = new SolrInputDocument();
            document.addField("id", searchItem.getId().toString());//这里是字符串需要转换
            document.addField("item_title", searchItem.getTitle());
            document.addField("item_sell_point", searchItem.getSell_point());
            document.addField("item_price", searchItem.getPrice());
            document.addField("item_image", searchItem.getImage());
            document.addField("item_category_name", searchItem.getCategory_name());
            document.addField("item_desc", searchItem.getItem_desc());
            //添加到索引库
            solrClient.add(document);
        }
        //提交
        solrClient.commit();
        return TaotaoResult.ok();
    }

    @Override
    public SearchResult searchSet(String queryString, Integer page, Integer rows) throws Exception {
        //1.创建solrquery对象
        SolrQuery query = new SolrQuery();
        //2.设置主查询条件
        if(StringUtils.isNotBlank(queryString)){
            query.setQuery(queryString);
        }else{
            query.setQuery("*:*");
        }
        //2.1设置过滤条件 设置分页
        if(page==null)page=1;
        if(rows==null)rows=60;
        query.setStart((page-1)*rows);//page-1 * rows
        query.setRows(rows);
        //2.2.设置默认的搜索域
        query.set("df", "item_keywords");
        //2.3设置高亮
        query.setHighlight(true);
        query.setHighlightSimplePre("<em style=\"color:red\">");
        query.setHighlightSimplePost("</em>");
        query.addHighlightField("item_title");//设置高亮显示的域

        //3.调用dao的方法 返回的是SearchResult 只包含了总记录数和商品的列表
        SearchResult search = search(query);
        //4.设置SearchResult 的总页数
        long pageCount = 0L;
        pageCount = search.getRecordCount()/rows;
        if(search.getRecordCount()%rows>0){
            pageCount++;
        }
        search.setPageCount(pageCount);
        //5.返回
        return search;
    }

    @Override
    public SearchResult search(SolrQuery query) throws Exception {
        SearchResult searchResult = new SearchResult();
        //1.创建solrclient对象
        //2.直接执行查询
        QueryResponse response = solrClient.query(query);
        //3.获取结果集
        SolrDocumentList results = response.getResults();
        //设置searchresult的总记录数
        searchResult.setRecordCount(results.getNumFound());
        //4.遍历结果集
        //定义一个集合
        List<SearchItem> itemlist = new ArrayList<>();

        //取高亮
        Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();

        for (SolrDocument solrDocument : results) {
            //讲solrdocument中的属性 一个个的设置到 searchItem中
            SearchItem item = new SearchItem();
            item.setCategory_name(solrDocument.get("item_category_name").toString());
            item.setId(Long.parseLong(solrDocument.get("id").toString()));
            item.setImage(solrDocument.get("item_image").toString());
            //item.setItem_desc(item_desc);
            item.setPrice((Long)solrDocument.get("item_price"));
            item.setSell_point(solrDocument.get("item_sell_point").toString());
            //取高亮
            List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
            //判断list是否为空
            String gaoliangstr = "";
            if(list!=null && list.size()>0){
                //有高亮
                gaoliangstr=list.get(0);
            }else{
                gaoliangstr = solrDocument.get("item_title").toString();
            }

            item.setTitle(gaoliangstr);
            //searchItem  封装到SearchResult中的itemList 属性中
            itemlist.add(item);
        }
        //5.设置SearchResult 的属性
        searchResult.setItemList(itemlist);
        return searchResult;
    }

    //更新索引库
    public TaotaoResult updateSearchItemById(Long itemId) throws Exception{
        //注入mapper
        //查询到记录
        SearchItem item = mapper.getSearchItemById(itemId);
        //把记录更新到索引库
        //创建solrserver 注入进来
        //创建solrinputdocument对象
        SolrInputDocument document = new SolrInputDocument();
        //向文档对象中添加域
        document.addField("id", item.getId().toString());//这里是字符串需要转换
        document.addField("item_title", item.getTitle());
        document.addField("item_sell_point", item.getSell_point());
        document.addField("item_price", item.getPrice());
        document.addField("item_image", item.getImage());
        document.addField("item_category_name", item.getCategory_name());
        document.addField("item_desc", item.getItem_desc());
        //向索引库中添加文档
        solrClient.add(document);
        //提交
        solrClient.commit();
        return TaotaoResult.ok();
    }
}
