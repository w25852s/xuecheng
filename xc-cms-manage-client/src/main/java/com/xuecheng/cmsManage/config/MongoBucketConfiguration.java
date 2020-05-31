package com.xuecheng.cmsManage.config;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoBucketConfiguration {


    @Value("${spring.data.mongodb.database}")
    private String db;
    @Bean
    public GridFSBucket getBucket(MongoClient client) {
        MongoDatabase database = client.getDatabase(db);
        return GridFSBuckets.create(database);
    }
}
