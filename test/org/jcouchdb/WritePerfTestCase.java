package org.jcouchdb;

import org.jcouchdb.db.Database;
import org.jcouchdb.db.LocalDatabaseTestCase;
import org.jcouchdb.document.BaseDocument;
import org.junit.Ignore;
import org.junit.Test;

public class WritePerfTestCase
{
    @Test
    @Ignore
    public void test()
    {
        BaseDocument doc = new BaseDocument();

        Database db = LocalDatabaseTestCase.createDatabaseForTest();

        doc.setProperty("data", "The quick brown fox jumps over the lazy dog.");

        long start = System.currentTimeMillis();
        final int num_docs = 1000;
        for (int i=0; i < num_docs; i++)
        {
            doc.setId(null);
            doc.setRevision(null);
            db.createDocument(doc);
        }
        long dur = System.currentTimeMillis()-start;
        double seconds = dur/1000.0;
        System.out.println("total: "+ seconds + ", docs/sec = " + num_docs/seconds);
    }
}
