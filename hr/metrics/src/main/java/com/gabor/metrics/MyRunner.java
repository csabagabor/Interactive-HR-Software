package com.gabor.metrics;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MyRunner implements CommandLineRunner {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void run(String... args) {
        if (args.length > 0) {
            if ("mongodb-update".equals(args[0])) {
                //clear mongodb, because it will be populated again
                mongoTemplate.getDb().drop();
            }
        }
    }
}