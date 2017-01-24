package com.es.test;

import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by wangji on 2017/1/8.
 */
public class ElasticSearch {
    private static TransportClient client = null;
    @Before
    public void getClient() throws UnknownHostException {
        client = new PreBuiltTransportClient(Settings.EMPTY);
        client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.116.138"), 9300));
    }

    @After
    public void closeClient() {
        client.close();
    }

    /**
     * 测试添加
     */
    @Test
    public void testPut() {
        String json = "{" +
                "\"user\":\"kimchy\"," +
                "\"postDate\":\"2013-01-30\"," +
                "\"message\":\"trying out Elasticsearch\"" +
                "}";

        IndexResponse response = client.prepareIndex("twitter","tweet")
                .setSource(json)
                .get();
    }

    /**
     * 测试获取根据id
     */
    @Test
    public void testGet() {
        GetResponse response = client.prepareGet("twitter", "tweet", "AVl-kJQdUAidN__ToszY").get();
        String s = response.toString();
        System.err.println("是否存在："+response.isExists());
        System.out.println(s);

    }

    /**
     * 测试删除
     */
    @Test
    public void testDelete() {
        DeleteResponse deleteResponse = client.prepareDelete("twitter", "tweet", "AVl-kJQdUAidN__ToszY")
                .get();
        System.err.println("删除文档的状态："+deleteResponse.status());
        System.out.println(deleteResponse);
    }

    /**
     * 测试Search API
     */
    @Test
    public void testSearch() {
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch("megacorp")
                .setTypes("employee")
                //.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                //.setQuery(QueryBuilders.termQuery("last_name", "S"))
                //.setPostFilter(QueryBuilders.rangeQuery("age").from(10).to(30))
                .setFrom(0).setSize(10).setExplain(true);
        SearchResponse response = searchRequestBuilder.get();
        System.out.println(searchRequestBuilder.toString());
        System.out.println(response);
    }

    @Test
    public void testSearchAll() {
        SearchResponse response = client.prepareSearch().get();
        System.out.println(response);
    }
}
