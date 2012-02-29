package org.jcouchdb;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.util.ArrayList;
import java.util.List;

import org.jcouchdb.db.Database;
import org.jcouchdb.db.LocalDatabaseTestCase;
import org.jcouchdb.document.BaseDocument;
import org.jcouchdb.document.DocumentInfo;
import org.junit.Ignore;
import org.junit.Test;

public class BulkWriteBugTestCase
{
    @Test
    @Ignore
    public void test()
    {
        Database db = LocalDatabaseTestCase.recreateDB("jcouchdb-bulk-bug");
        
        final int num_docs = 1000;
        List<BaseDocument> documents = new ArrayList<BaseDocument>(num_docs);
        for (int i=0; i < num_docs; i++)
        {
            BaseDocument doc = new BaseDocument();
            doc.setProperty("data", "The quick brown fox jumps over the lazy dog.");
            doc.setId(null);
            doc.setRevision(null);
            documents.add(doc);
        }
        
        List<DocumentInfo> infos = db.bulkCreateDocuments(documents);
        assertThat(infos, is(notNullValue()));
        assertThat(infos.size(), is(num_docs));
        
    }

}
