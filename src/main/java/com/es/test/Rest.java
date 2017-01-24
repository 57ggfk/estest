package com.es.test;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.ResponseListener;
import org.elasticsearch.client.RestClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;

/**
 * Created by wangji on 2017/1/8.
 */
public class Rest {
    private static RestClient restClient = null;
    @Before
    public void getRestClient() {
        HttpHost httpHost = new HttpHost("localhost", 9200, "http");
        restClient = RestClient.builder(httpHost).build();

    }

    @After
    public void closeRestClient() throws IOException {
        restClient.close();
    }

    @Test
    public void testPut() throws IOException {
        Response response = restClient.performRequest("GET", "/", Collections.singletonMap("pretty", "true"));
        System.out.println(EntityUtils.toString(response.getEntity()));
        //index a dodumnet
        HttpEntity entiry = new NStringEntity(
                "{\n" +
                        "    \"user\" : \"kimchy\",\n" +
                        "    \"post_date\" : \"2009-11-15T14:12:12\",\n" +
                        "    \"message\" : \"trying out Elasticsearch\"\n" +
                        "}", ContentType.APPLICATION_JSON
        );

        Response put = restClient.performRequest("PUT", "/twitter/tweet/1", Collections.<String, String>emptyMap(), entiry);
        System.out.println(put);

    }

    /**
     * 异步请求设置监听方法
     */
    @Test
    public void testLitener()  {
        int numRequests = 10;
        final CountDownLatch latch = new CountDownLatch(numRequests);

        for (int i = 1; i < numRequests; i++) {
            //设置entity
            String json = "{\n" +
                    "    \"user\" : \"kimchy"+i+"\",\n" +
                    "    \"post_date\" : \"2009-11-15T14:12:12\",\n" +
                    "    \"message\" : \"trying out Elasticsearch\"\n" +
                    "}";
            HttpEntity entity = new NStringEntity(
                    json
                    , ContentType.APPLICATION_JSON);

            System.out.println(json);
            restClient.performRequestAsync(
                    "PUT",
                    "/twitter/tweet/" + i,
                    Collections.<String, String>emptyMap(),
                    entity,
                    new ResponseListener() {
                        public void onSuccess(Response response) {
                            System.out.println(response);
                            //latch.countDown();
                        }

                        public void onFailure(Exception e) {
                            System.err.println(e.getMessage());
                            latch.countDown();
                        }
                    });
            try {
                latch.await();
            } catch (Exception e2) {
                System.err.println(e2.getMessage());
            }

        }
    }

    /**
     * 测试删除
     */
    @Test
    public void testDelete() throws IOException {
        int numDelete = 10;
        for (int i = 0; i < numDelete; i++) {
            Response delete = restClient.performRequest("DELETE", "/twitter/tweet/"+i);
//            restClient.performRequestAsync("DELETE", "/twitter/tweet/" + i, new ResponseListener() {
//                public void onSuccess(Response response) {
//                    System.out.println("删除成功"+response.toString());
//                }
//
//                public void onFailure(Exception e) {
//                    System.err.println("删除失败"+e.getMessage());
//                }
//            });
           System.err.println("删除结果"+delete.toString());
        }

    }
}
