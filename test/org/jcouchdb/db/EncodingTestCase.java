package org.jcouchdb.db;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.jcouchdb.document.BaseDocument;
import org.junit.Test;

public class EncodingTestCase
{
    @Test
    public void test()
    {
        Database db = LocalDatabaseTestCase.createDatabaseForTest();
        BaseDocument doc = new BaseDocument();
        doc.setProperty("value", "Hello \u00e4\u00f6\u00fc");
        
        db.createDocument(doc);
        
        BaseDocument doc2 = db.getDocument(BaseDocument.class, doc.getId());
        assertThat((String)doc2.getProperty("value"), is("Hello \u00e4\u00f6\u00fc"));
        
    }
    
    @Test
    public void testRealNonEscaped()
    {
        Database db = LocalDatabaseTestCase.createDatabaseForTest();
        BaseDocument doc = new BaseDocument();
        doc.setProperty("value", "Hello äöü♶");
        
        db.createDocument(doc);
        
        BaseDocument doc2 = db.getDocument(BaseDocument.class, doc.getId());
        assertThat((String)doc2.getProperty("value"), is("Hello äöü♶"));
        
    }

    
}
