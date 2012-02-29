package org.jcouchdb.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jcouchdb.db.LocalDatabaseTestCase;
import org.jcouchdb.document.DesignDocument;
import org.jcouchdb.document.View;
import org.junit.Test;


public class CouchDBUpdaterTestCase
{
    private static Logger log = Logger.getLogger(CouchDBUpdaterTestCase.class);

    @Test

    public void thatDesignDocumentReadingWorks() throws IOException
    {

        
        CouchDBUpdater couchDBUpdater = new CouchDBUpdater();
        couchDBUpdater.setDesignDocumentDir(new File("test/org/jcouchdb/util/test-views/"));
        couchDBUpdater.setDatabase(LocalDatabaseTestCase.createDatabaseForTest());


        List<DesignDocument> docs = couchDBUpdater.updateDesignDocuments();
        testDocsIntegrity( docs);
    }

    void testDocsIntegrity( List<DesignDocument> docs)
    {
        final String mapFn = 
            "function()\n" + 
            "{\n" + 
            "\tif (doc.type == \"foo\")\n" + 
            "\t{\n" + 
            "\t\temit(doc.id,null);\n" + 
            "\t}\n" + 
            "}\n"; 

        assertThat(docs,is(notNullValue()));
        assertThat(docs.size(), is(2));
        
        for (DesignDocument doc : docs)
        {
            Map<String, View> views = doc.getViews();
            assertThat(views.size(), is(1));
            
            View view = views.get("foo");
            
            if (doc.getId().equals("_design/view1"))
            {
                assertThat(view.getMap(),is(mapFn));
            }
            else if (doc.getId().equals("_design/view2/sub"))
            {
                assertThat(view.getMap(),is(mapFn));
                assertThat(view.getReduce(),is("function()\n" + 
                    "{\n" + 
                    "\treturn 0;\n" + 
                "}\n"));
            }
            else
            {
                assertThat("invalid id " + doc.getId(), false, is(true));
            }
        }
        
        assertThat(docs.get(0).getId(),anyOf( is("_design/view1"), is("_design/view2/sub")));
        assertThat(docs.get(1).getId(),anyOf( is("_design/view1"), is("_design/view2/sub")));
    }
}
