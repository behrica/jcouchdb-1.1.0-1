package org.jcouchdb.db;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import org.jcouchdb.document.BaseDocument;
import org.jcouchdb.document.ChangeNotification;
import org.jcouchdb.document.PollingResults;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChangesTestCase
{
    private static final String LONGPOLL_STOPPER_ID = "longpoll-stopper";

    // XXX: How many milliseconds do we really have to wait to be safe?
    private static final long LONG_POLLING_TEST_SLEEP_MS = 500L;

    private static Logger log = LoggerFactory.getLogger(ChangesTestCase.class);

    private static Database db;


    @BeforeClass
    public static void createDB()
    {
        db = LocalDatabaseTestCase.createRandomNamedDB("changes");
    }


    @AfterClass
    public static void deleteDB()
    {
        db.getServer().deleteDatabase(db.getName());
    }


    @Test
    public void test()
    {
        BaseDocument doc = newDoc("doc-1", "foo");
        db.createDocument(doc);
        BaseDocument doc2 = newDoc("doc-2", "bar");
        db.createDocument(doc2);
        doc.setProperty("data", "baz");
        db.updateDocument(doc);

        PollingResults results = db.pollChanges(null, null, false, null);

        assertThat(results, is(notNullValue()));

        assertThat(results.getResults().size(), is(2));

        ChangeNotification result = results.getResults().get(0);

        assertThat(result.getId(), is(doc2.getId()));
        assertThat(result.getSequence(), is(2L));
        assertThat(result.getChanges().size(), is(1));
        assertThat(result.getChanges().get(0).getRev(), is(doc2.getRevision()));

        result = results.getResults().get(1);

        assertThat(result.getId(), is(doc.getId()));
        assertThat(result.getSequence(), is(3L));
        assertThat(result.getChanges().size(), is(1));
        assertThat(result.getChanges().get(0).getRev(), is(doc.getRevision()));

        log.info("results = {}", results);

        results = db.pollChanges(3l, null, false, null);
        assertThat(results.getResults().size(), is(0));
    }

    @Test
    public void testLongPolling() throws InterruptedException
    {
        // test strategy:
        // start longpolling in another request, wait x milliseconds
        // assert that the time between longpolling and next statement is x millis or more
        
        LongPollThread t = new LongPollThread();
        t.start();
                
        long start;
        synchronized(t)
        {
            t.wait();
            start = System.currentTimeMillis();
        }
        
        Thread.sleep(LONG_POLLING_TEST_SLEEP_MS);
        
        db.createDocument(newDoc(LONGPOLL_STOPPER_ID, "none"));
        
        t.join();
        
        long diff = t.end - start;
        
        assertThat(diff, is(greaterThanOrEqualTo(LONG_POLLING_TEST_SLEEP_MS)));
        log.info("diff = {}",  diff);
        
        assertThat( t.id, is(LONGPOLL_STOPPER_ID));
    }

    public BaseDocument newDoc(String name, String data)
    {
        BaseDocument doc = new BaseDocument();
        doc.setProperty("_id", name);
        doc.setProperty("data", data);
        return doc;
    }
    
    
    private static class LongPollThread extends Thread
    {
        private volatile long end;
        private volatile String id;
        
        LongPollThread()
        {
            super("LongPollThread");
        }
     
        @Override
        public void run()
        {
            synchronized(this)
            {
                this.notifyAll();
            }
            PollingResults results = db.pollChanges(3l, null, true, null);
            end = System.currentTimeMillis();
            id = results.getResults().get(0).getId();
            
        }
    }
}
