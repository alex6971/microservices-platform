package com.central.log.service.impl;

import com.alibaba.fastjson.JSON;
import com.central.log.model.Audit;
import com.central.log.service.IAuditService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.scheduling.annotation.Async;

/**
 * 审计日志实现类-ES
 *
 * @author zlt
 * @date 2020/2/8
 * <p>
 * Blog: https://zlt2000.gitee.io
 * Github: https://github.com/zlt2000
 */
@Slf4j
@ConditionalOnProperty(name = "zlt.audit-log.log-type", havingValue = "es")
public class ESAuditServiceImpl implements IAuditService {

    
    private final ElasticsearchRestTemplate elasticsearchRestTemplate;

    public ESAuditServiceImpl(ElasticsearchRestTemplate elasticsearchRestTemplate) {
        this.elasticsearchRestTemplate = elasticsearchRestTemplate;
    }

    @Async
    @Override
    public void save(Audit audit) {
        // 记录全量日志，同时去除异常数据,入es库
        try {

            RestHighLevelClient client = elasticsearchRestTemplate.getClient();

            //创建索引对象请求
            IndexRequest request = new IndexRequest("audit-log-");
            // request.id("422");//设置id
            request.timeout("1s");//设置超时时间
            //利用fastJSON将user对象转换为json格式，然后放到索引中，参数XContentType.JSON是告诉es为json数据
            request.source(JSON.toJSONString(audit), XContentType.JSON);
            //发送请求
            IndexResponse indexResponse = client.index(request, RequestOptions.DEFAULT);
            System.out.println(indexResponse.status());//CREATED，说明添加成功

        } catch (Exception e) {
            log.error("uuid: {}新增数据入es库异常", audit.getClientId());
            log.error(e.getMessage(), e);
        }
    }
}
