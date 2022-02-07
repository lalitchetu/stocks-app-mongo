package com.stocksapp.changeLogs;

import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import com.mongodb.BasicDBList;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@ChangeLog
public class ChangeLogs {
    private static final Logger LOG = LoggerFactory.getLogger(ChangeLogs.class);

    @ChangeSet(order = "001", id = "someChangeWithDb_1", author = "Lalit Singh")
    public void insertTestData(DB db) throws IOException {
        LOG.info("In insertTestData");
        DBCollection mycollection = db.getCollection("stockitems");

        BasicDBList listOfObj = (BasicDBList) JSON.parse(getJsonString("classpath:stock-test-data.json"));
        List<DBObject> objectList = new ArrayList<>();
        for (Object obj : listOfObj) {
            LOG.info("insertTestData obj: {}", obj);
            objectList.add((DBObject) obj);
        }

        mycollection.insert(objectList);
        LOG.info("Out insertTestData");
    }

    private String getJsonString(String fileName) throws IOException {
        File file = ResourceUtils.getFile(fileName);
        return new String(Files.readAllBytes(file.toPath()));
    }
}
