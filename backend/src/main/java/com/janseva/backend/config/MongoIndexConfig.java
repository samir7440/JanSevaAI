package com.janseva.backend.config;

import com.janseva.backend.model.Complaint;

import jakarta.annotation.PostConstruct;

import org.springframework.context.annotation.Configuration;

import org.springframework.data.domain.Sort;

import org.springframework.data.mongodb.core.MongoTemplate;

import org.springframework.data.mongodb.core.index.Index;

@Configuration
public class MongoIndexConfig {

    private final MongoTemplate mongoTemplate;

    public MongoIndexConfig(
            MongoTemplate mongoTemplate
    ) {

        this.mongoTemplate =
                mongoTemplate;
    }

    @PostConstruct
    public void initIndexes() {

        createIndex(
                "complaintId",
                true
        );

        createIndex(
                "status",
                false
        );

        createIndex(
                "priority",
                false
        );

        createIndex(
                "currentLevel",
                false
        );
    }

    private void createIndex(

            String field,

            boolean unique

    ) {

        Index index =
                new Index()
                        .on(
                                field,
                                Sort.Direction.ASC
                        );

        if (unique) {

            index.unique();
        }

        mongoTemplate
                .indexOps(Complaint.class)
                .createIndex(index);
    }
}