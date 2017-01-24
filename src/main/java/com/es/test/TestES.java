package com.es.test;


import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.collect.HppcMaps;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.mapper.DynamicTemplate;
import org.elasticsearch.index.mapper.ObjectMapper;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangji on 2017/1/8.
 */
public class TestES {
    public static void main(String[] args) throws UnknownHostException {
        System.out.println("run...");
        //RestClient
        //Settings settings = Settings.settingsBuilder();
        TransportClient client = new PreBuiltTransportClient(Settings.EMPTY);
        client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));

        //indices

        //jackson
        Map<String, Object> json2 = new HashMap<String, Object>();
        json2.put("user","kimchy");
        json2.put("postDate",new Date());
        json2.put("message","trying out Elasticsearch");

        //ObjectMapper mapper = new ObjectMapper();

        //esHelper
        //XContentBuilder xcBuilder = jsonBuilder();

        //index doc


        client.close();
    }

    public void testPut(TransportClient client) {
        String json = "{" +
                "\"user\":\"kimchy\"," +
                "\"postDate\":\"2013-01-30\"," +
                "\"message\":\"trying out Elasticsearch\"" +
                "}";

        IndexResponse response = client.prepareIndex("twitter","tweet")
                .setSource(json)
                .get();
    }

    @Test
    public void testRun() {
        System.out.println("junit");
    }

}
