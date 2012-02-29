package org.jcouchdb.db;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import org.apache.log4j.Logger;
import org.junit.Test;


public class DatabaseTestCase
{
    protected static Logger log = Logger.getLogger(DatabaseTestCase.class);

    @Test
    public void testStatus()
    {
        Server server = createMock(Server.class);

        Database db = new Database(server,"fforw_de");

        expect(server.get(eq("/fforw_de/"))).andReturn(new Response(200,"{\"db_name\": \"dj\", \"doc_count\":5, \"doc_del_count\":0, \"update_seq\":13, \"compact_running\":false, \"disk_size\":16845}"));

        replay(server);

        DatabaseStatus status = db.getStatus();

        assertThat(status, is(notNullValue()));
        assertThat(status.getName(), is("dj"));
        assertThat(status.getDocumentCount(), is(5l));
        assertThat(status.getDeletedDocumentCount(), is(0l));
        assertThat(status.getUpdateSequence(), is(13));
        assertThat(status.isCompactRunning(), is(false));
        assertThat(status.getDiskSize(), is(16845l));
        log.info("status = "+status);

        verify(server);
    }


    @Test
    public void testCompact()
    {
        Server server = createMock(Server.class);

        Database db = new Database(server,"fforw_de");

        expect(server.post(eq("/fforw_de/_compact"),eq(""))).andReturn(new Response(200,"{\"ok\":true}"));

        replay(server);

        db.compact();

        verify(server);
    }

    @Test
    public void realCompact()
    {
        Database db = LocalDatabaseTestCase.createDatabaseForTest();
        db.compact();
    }


}
