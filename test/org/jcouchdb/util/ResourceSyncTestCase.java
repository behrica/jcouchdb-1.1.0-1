package org.jcouchdb.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.jcouchdb.db.Database;
import org.jcouchdb.db.LocalDatabaseTestCase;
import org.jcouchdb.document.Attachment;
import org.jcouchdb.document.BaseDocument;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ResourceSyncTestCase
{
    private static Logger log = LoggerFactory
        .getLogger(ResourceSyncTestCase.class);
    private File dir;
    
    @Before
    public void init() throws IOException
    {
        dir = new File( System.getProperty("java.io.tmpdir") + File.separator + UUID.randomUUID().toString() );
        dir.mkdirs();
        dir.deleteOnExit();
        
        File sub = new File(dir, "sub");
        sub.mkdir();
        
        FileUtils.writeStringToFile(new File(dir, "test.txt"), "123");
        FileUtils.writeStringToFile(new File(sub, "test.txt"), "456");
        
    }
    
    
    @Test
    public void test()
    {
        ResourceSync sync = new ResourceSync();
        Database db = LocalDatabaseTestCase.createDatabaseForTest();
        
        LocalDatabaseTestCase.deleteDocIfExists(db, sync.getResourceBaseDocId());
        
        sync.setDatabase(db);
        sync.setResourceBaseDir(dir);
        sync.syncResources();
        
        BaseDocument doc = db.getDocument(BaseDocument.class, sync.getResourceBaseDocId());
        Map<String, Attachment> map = doc.getAttachments();
        

        System.out.println(map);
        assertThat(map.size(), is(2));
        assertThat(map.get("test.txt").getLength(), is((3l)));
        assertThat(map.get("sub/test.txt").getLength(), is((3l)));
    }
}
