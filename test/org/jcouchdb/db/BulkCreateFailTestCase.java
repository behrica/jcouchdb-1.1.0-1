package org.jcouchdb.db;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

import java.util.ArrayList;
import java.util.List;

import org.jcouchdb.document.Document;
import org.jcouchdb.document.DocumentInfo;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.svenson.JSON;

public class BulkCreateFailTestCase
{
    private static Logger log = LoggerFactory.getLogger(BulkCreateFailTestCase.class);
    
    @Test
    public void thatFailedBulkWorks()
    {
        Database db = LocalDatabaseTestCase.createDatabaseForTest();

        List<Document> docs = new ArrayList<Document>();
        docs.add(docWithIdAndValue("conflicting-id", "value 1"));
        docs.add(docWithIdAndValue("conflicting-id", "value 2"));
        docs.add(docWithIdAndValue("conflicting-id", "value 3"));
        List<DocumentInfo> infos = db.bulkCreateDocuments(docs, true);
        
        JSON generator = JSON.defaultJSON();
        log.info(generator.formatJSON(generator.forValue(infos)));
    }

    private FooDocument docWithIdAndValue(String id, String val)
    {
        FooDocument fooDocument = new FooDocument(val);
        fooDocument.setId(id);
        return fooDocument;
    }

}
