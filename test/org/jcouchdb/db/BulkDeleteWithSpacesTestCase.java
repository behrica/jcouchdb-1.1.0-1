package org.jcouchdb.db;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jcouchdb.db.ConcurrentRequestTestCase.Requestor;
import org.jcouchdb.document.BaseDocument;
import org.jcouchdb.document.DesignDocument;
import org.jcouchdb.document.ValueAndDocumentRow;
import org.jcouchdb.document.ValueRow;
import org.jcouchdb.document.View;
import org.jcouchdb.document.ViewAndDocumentsResult;
import org.jcouchdb.document.ViewResult;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BulkDeleteWithSpacesTestCase
{
    private static Logger log = LoggerFactory.getLogger(BulkDeleteWithSpacesTestCase.class);
    
    private Database db;
    
    @Before
    public void init() throws UnsupportedEncodingException
    {
        db = LocalDatabaseTestCase.createRandomNamedDB("bulk-del-");
        
        log.info("{}", URLEncoder.encode("/ ", "UTF-8"));
    }
    
    
    @After
    public void destroy()
    {
        Server server = db.getServer();
        server.deleteDatabase(db.getName());
    }
    
    @Test
    public void test() throws InterruptedException
    {
        BaseDocument doc = new BaseDocument();
        doc.setId("doc 1");
        db.createDocument(doc);
        
        assertThat(doc.getRevision(), is(notNullValue()));
        
        doc = new BaseDocument();
        doc.setId("doc 2");
        db.createDocument(doc);
        assertThat(doc.getRevision(), is(notNullValue()));

        
        List<BaseDocument> docs = new ArrayList<BaseDocument>();
        
        doc = db.getDocument(BaseDocument.class, "doc 1");
        assertThat(doc, is(notNullValue()));
        docs.add(doc);
        doc = db.getDocument(BaseDocument.class, "doc 2");
        assertThat(doc, is(notNullValue()));
        docs.add(doc);
        
        
        db.bulkDeleteDocuments(docs);
        
        LocalDatabaseTestCase.assertNotExist(db, "doc 1");
        LocalDatabaseTestCase.assertNotExist(db, "doc 2");
    }
}
