package com.niuma.questionnaire.utils;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.xcontent.XContentType;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EsClient {
    private static final RestHighLevelClient client = new RestHighLevelClient(RestClient.
            builder(HttpHost.create("http://127.0.0.1:9200")));

    /**
     * 创建索引库
     *
     * @param indexName     索引库名称
     * @param sourceMapping 条件 JSON格式
     */
    public static void createIndex(String indexName, String sourceMapping) throws IOException {
        CreateIndexRequest request = new CreateIndexRequest(indexName);
        request.source(sourceMapping, XContentType.JSON);
        client.indices().create(request, RequestOptions.DEFAULT);
    }

    /**
     * 添加单个文档
     *
     * @param indexName
     * @param indexID
     * @param document
     */
    public static void addOneDocument(String indexName, String indexID, String document) throws IOException {
        IndexRequest request = new IndexRequest(indexName).id(indexID);
        request.source(document);
        client.index(request, RequestOptions.DEFAULT);
    }

    /**
     * 添加多条文档
     *
     * @param indexName 索引名
     * @param lists     需添加的文档集合
     * @param idMethod  类的主键的方法
     * @param <T>       类型       泛型类
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static <T> void addDocument(String indexName, List<T> lists, String idMethod) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        BulkRequest request = new BulkRequest();
        for (T t : lists) {
            Method method = t.getClass().getMethod(idMethod);
            request.add(new IndexRequest(indexName).
                    id(method.invoke(t).toString()).
                    source(JSON.toJSONString(t), XContentType.JSON));
        }
        client.bulk(request, RequestOptions.DEFAULT);
    }

    /**
     * 获得单条文档信息
     *
     * @param indexName
     * @param indexID
     * @param <T>       类型
     * @return
     */
    public static <T> T getDocument(String indexName, String indexID, Class<T> type) throws IOException {
        GetRequest request = new GetRequest(indexName).id(indexID);
        GetResponse response = client.get(request, RequestOptions.DEFAULT);
        String jsonStr = response.getSourceAsString();
        T t = JSON.parseObject(jsonStr, type);
        return t;
    }

    /**
     * 修改文档
     *
     * @param indexName     索引名
     * @param indexID       索引ID
     * @param updateContent 修改的内容
     */
    public static void updateDocument(String indexName, String indexID, String updateContent) throws IOException {
        UpdateRequest request = new UpdateRequest(indexName, indexID);
        request.doc(updateContent);
        client.update(request, RequestOptions.DEFAULT);
    }

    /**
     * 删除文档
     *
     * @param indexName 索引名
     * @param indexID   索引ID
     */
    public static void deleteDocument(String indexName, String indexID) throws IOException {
        DeleteRequest request = new DeleteRequest(indexName, indexID);
        client.delete(request, RequestOptions.DEFAULT);
    }

    /**
     * 查询所有数据
     *
     * @param indexName 索引名
     * @param type      返回的值类型
     * @param <T>       类型
     * @return
     */
    public static <T> List<T> matchAll(String indexName, Class<T> type) throws IOException {
        SearchRequest request = new SearchRequest(indexName);
        request.source().query(QueryBuilders.matchAllQuery());
        return searchList(request, type);
    }

    /**
     * 单条件查询数据
     *
     * @param indexName 索引名
     * @param name      字段名
     * @param text      字段内容
     * @param type      返回类型
     * @param <T>       类型
     * @return
     */
    public static <T> List<T> match(String indexName, String name, String text, Class<T> type) throws IOException {
        SearchRequest request = new SearchRequest(indexName);
        request.source().query(QueryBuilders.matchQuery(name, text));
        return searchList(request, type);
    }

    /**
     * 多条件查询数据
     *
     * @param indexName  索引名
     * @param name       字段名
     * @param filedNames 字段内容,字符串数组
     * @param type       返回类型
     * @param <T>        类型
     * @return
     */
    public static <T> List<T> multiMatch(String indexName, String name, String[] filedNames, Class<T> type) throws IOException {
        SearchRequest request = new SearchRequest(indexName);
        request.source().query(QueryBuilders.multiMatchQuery(name, filedNames));
        return searchList(request, type);
    }

    /**
     * 词条精准匹配查询
     *
     * @param indexName 索引名
     * @param name      字段名
     * @param text      字段内容
     * @param type      返回类型
     * @param <T>       类型
     * @return
     */
    public static <T> List<T> termMatch(String indexName, String name, String text, Class<T> type) throws IOException {
        SearchRequest request = new SearchRequest(indexName);
        request.source().query(QueryBuilders.termQuery(name, text));
        return searchList(request, type);
    }

    /**
     * @param indexName 索引名
     * @param name      字段名
     * @param little    最小值
     * @param big       最大值
     * @param type      返回值类型
     * @param <T>       类型       类型
     * @return
     */
    public static <T> List<T> rangeMatch(String indexName, String name, Object little, Object big, Class<T> type) throws IOException {
        SearchRequest request = new SearchRequest(indexName);
        request.source().query(QueryBuilders.rangeQuery(name).gte(little).lte(big));
        return searchList(request, type);
    }

    /**
     * 查询指定字段并分页
     *
     * @param indexName 索引名
     * @param name      字段名
     * @param text      字段值
     * @param page      页数
     * @param pageSize  每页大小
     * @param type      类型
     * @param <T>       类型       类型
     * @return 结果集
     */
    public static <T> List<T> pageMatchName(String indexName, String name, String text, Integer page, Integer pageSize, Class<T> type) throws IOException {
        SearchRequest request = new SearchRequest(indexName);
        request.source().query(QueryBuilders.matchQuery(name, text))
                .from(page - 1)
                .size(pageSize);
        return searchList(request, type);
    }

    /**
     * 查询所有并分页
     *
     * @param indexName 索引名
     * @param page      页数
     * @param pageSize  每页大小
     * @param type      类型
     * @param <T>       类型       类型
     * @return 分页结果
     */
    public static <T> List<T> pageMatch(String indexName, Integer page, Integer pageSize, Class<T> type) throws IOException {
        SearchRequest request = new SearchRequest(indexName);
        request.source().query(QueryBuilders.matchAllQuery())
                .from(page - 1)
                .size(pageSize);
        return searchList(request, type);
    }

    /**
     * 判断索引库是否存在
     *
     * @param indexName 索引名
     * @return 存在？
     */
    public static boolean existIndex(String indexName) throws IOException {
        GetIndexRequest request = new GetIndexRequest(indexName);
        boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
        System.out.println(exists ? "索引库已存在" : "索引库不存在");
        return exists;
    }

    public static <T> List<T> searchList(SearchRequest request, Class<T> type) throws IOException {
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        SearchHits searchHits = response.getHits();
        long total = searchHits.getTotalHits().value;
        SearchHit[] hits = searchHits.getHits();
        System.out.println("response的结果为：" + response);
        System.out.println("searchHits的结果为：" + searchHits);
        System.out.println("共查询到" + total + "条");
        System.out.println("hits的值为：" + Arrays.toString(hits));
        List<T> list = new ArrayList<>();
        for (SearchHit hit : hits) {
            String json = hit.getSourceAsString();
            T t = JSON.parseObject(json, type);
            list.add(t);
            System.out.println("添加集合里的类为：" + t.toString());
        }
        return list;
    }
}
