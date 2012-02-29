package org.jcouchdb.db;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.HashMap;
import java.util.Map;

import org.jcouchdb.document.DesignDocument;
import org.jcouchdb.document.ValueRow;
import org.jcouchdb.document.View;
import org.jcouchdb.document.ViewResult;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class ConcurrentRequestTestCase
{
    private final static int NUM_DOCS = 1000;

    private static final int NUM_REQUESTS = 10;
    
    private Database db;
    
    @Before
    public void init()
    {
        db = LocalDatabaseTestCase.createRandomNamedDB("concurrent-");
        
        for ( int i = 0; i < NUM_DOCS; i++)
        {
            Map m = new HashMap();
            m.put("value", Math.random());
            db.createDocument(m);
        }
        
        DesignDocument doc = new DesignDocument("docs");
        
        View view = new View();
        view.setMap("function(doc) { emit(doc._id, doc.value); }");
        doc.addView("byValue", view);
        
        db.createDocument(doc);
    }

    
    @After
    public void destroy()
    {
        Server server = db.getServer();
        server.deleteDatabase(db.getName());
    }
    
    @Test
    @Ignore
    public void test() throws InterruptedException
    {
        Requestor r1 = new Requestor(String.class, NUM_REQUESTS);
        Requestor r2 = new Requestor(Double.class, NUM_REQUESTS);
     
        r1.start();
        r2.start();
        
        r1.join();
        r2.join();
    }
    
    
    public class Requestor extends Thread
    {
        private Class valueClass;
        private int count;
        public Requestor(Class valueClass, int count)
        {
            this.valueClass = valueClass;
            this.count = count;
        }
        
        @Override
        public void run()
        {
            for (int i = 0; i < count ; i++)
            {
                ViewResult<Object> res = db.queryView("docs/byValue", valueClass, null,null);
                for (ValueRow<Object> row : res.getRows())
                {
                    assertThat(row.getValue(), is(valueClass));
                }
            }
        }
    }
}
