package org.jcouchdb.document;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.jcouchdb.document.BaseDocument;
import org.jcouchdb.document.DocumentHelper;
import org.junit.BeforeClass;
import org.junit.Test;


public class DocumentHelperTestCase
{
    private static DocumentPropertyHandler documentHelper = new DocumentHelper();
    

    @Test
    public void thatGetIdWorks()
    {
        Bean b = new Bean();
        b.set_id("foo");

        assertThat(documentHelper.getId(b), is(("foo")));

        BaseDocument doc = new BaseDocument();
        doc.setId("foo");
        assertThat(documentHelper.getId(doc), is(("foo")));
    }


    @Test
    public void thatSetIdWorks()
    {
        Bean b = new Bean();
        b.set_id("foo");
        documentHelper.setId(b, "bar");
        assertThat(b.get_id(), is(("bar")));

        BaseDocument doc = new BaseDocument();
        doc.setId("foo");
        documentHelper.setId(doc, "bar");
        assertThat(doc.getId(), is(("bar")));
    }

    @Test
    public void thatGetRevWorks()
    {
        Bean b = new Bean();
        b.set_rev("foo");
        assertThat(documentHelper.getRevision(b), is(("foo")));

        BaseDocument doc = new BaseDocument();
        doc.setRevision("foo");
        assertThat(documentHelper.getRevision(doc), is(("foo")));
    }


    @Test
    public void thatSetRevWorks()
    {
        Bean b = new Bean();
        b.set_rev("foo");
        documentHelper.setRevision(b, "bar");
        assertThat(b.get_rev(), is(("bar")));

        BaseDocument doc = new BaseDocument();
        doc.setRevision("foo");
        documentHelper.setRevision(doc, "bar");
        assertThat(doc.getRevision(), is(("bar")));
    }

    public static class Bean
    {
        private String _id, _rev;

        public String get_id()
        {
            return _id;
        }

        public void set_id(String _id)
        {
            this._id = _id;
        }

        public String get_rev()
        {
            return _rev;
        }

        public void set_rev(String _rev)
        {
            this._rev = _rev;
        }

    }
}
