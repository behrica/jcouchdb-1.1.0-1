package org.jcouchdb.db;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

import java.util.ArrayList;
import java.util.List;

import org.jcouchdb.document.BaseDocument;
import org.jcouchdb.document.ChangeListener;
import org.jcouchdb.document.ChangeNotification;
import org.jcouchdb.document.HeartBeatListener;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ContinuousChangesDriverTestCase
{
    private static Database db;

    private static Logger log = LoggerFactory.getLogger(ContinuousChangesDriverTestCase.class);
    
    
    @BeforeClass
    public static void createDB()
    {
        db = LocalDatabaseTestCase.recreateDB("jcouchdb-continuous-changes");
    }


    @AfterClass
    public static void deleteDB()
    {
        db.getServer().deleteDatabase(db.getName());
    }


    @Test
    public void test() throws InterruptedException
    {
        TestListener listener = new TestListener();
        db.registerChangeListener(null, null, new Options().put("heartbeat", 100), listener);
        
        BaseDocument foo = newDoc("foo","123");
        db.createDocument( foo);
        BaseDocument bar = newDoc("bar","456");
        db.createDocument( bar);
        
        Thread.sleep(500);
        
        db.getServer().shutDown();
        
        List<ChangeNotification> changeNotifications = listener.getChangeNotifications();
        assertThat(changeNotifications.size(), is(2));
        ChangeNotification fooChange = changeNotifications.get(0);
        assertThat(fooChange.getId(), is(foo.getId()));
        assertThat(fooChange.getChanges().get(0).getRev(), is(foo.getRevision()));

        ChangeNotification barChange = changeNotifications.get(1);
        assertThat(barChange.getId(), is(bar.getId()));
        assertThat(barChange.getChanges().get(0).getRev(), is(bar.getRevision()));
        
        assertThat(listener.getHeartbeatCount(),is(greaterThan(2)));
    }
    
    static class TestListener implements HeartBeatListener
    {
        private List<ChangeNotification> changeNotifications = new ArrayList<ChangeNotification>();
        private int heartbeatCount;
        public void onChange(ChangeNotification changeNotification)
        {
            log.info("notification: {}", changeNotification);
            
            changeNotifications.add(changeNotification);
        }
        
        public List<ChangeNotification> getChangeNotifications()
        {
            return changeNotifications;
        }

        public void heartbeat()
        {
            heartbeatCount++;
        }
        
        public int getHeartbeatCount()
        {
            return heartbeatCount;
        }
    }
    

    public BaseDocument newDoc(String name, String data)
    {
        BaseDocument doc = new BaseDocument();
        doc.setProperty("_id", name);
        doc.setProperty("data", data);
        return doc;
    }
}
