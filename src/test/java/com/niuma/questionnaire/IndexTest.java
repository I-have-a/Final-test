package com.niuma.questionnaire;

import org.apache.http.HttpHost;
import org.elasticsearch.client.*;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.xcontent.XContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class IndexTest {

    private RestHighLevelClient restHighLevelClient;

    @BeforeEach
    void setup() {
        this.restHighLevelClient = new RestHighLevelClient(RestClient.builder(HttpHost.create("http://127.0.0.1:9200")));
    }
    @AfterEach
    void setDown(){
        try {
            this.restHighLevelClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test() throws IOException {
        CreateIndexRequest request = new CreateIndexRequest("test");
        request.source("", XContentType.JSON);
        CreateIndexResponse response = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
        restHighLevelClient.asyncSearch();
        response.index();
    }
}
