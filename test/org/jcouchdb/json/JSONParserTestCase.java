package org.jcouchdb.json;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.jcouchdb.document.ViewResult;
import org.jcouchdb.document.ValueRow;
import org.junit.Test;
import org.svenson.JSONParser;

public class JSONParserTestCase
{
    protected static Logger log = Logger.getLogger(JSONParserTestCase.class);

    private JSONParser parser = new JSONParser();

    @Test
    public void thatViewResultParsingWorks() throws IOException
    {
        String json = FileUtils.readFileToString(new File(
            "test/org/jcouchdb/json/test-files/view-result.json"));
        log.info("json = " + json);

        parser.addTypeHint(".rows[]", ValueRow.class);
        parser.addTypeHint(".rows[].value", ContentBean.class);
        ViewResult<ContentBean> viewResult = parser.parse(ViewResult.class, json);

        assertThat(viewResult, is(notNullValue()));
        assertThat(viewResult.getRows().size(), is(2));

        List<ValueRow<ContentBean>> rows = viewResult.getRows();
        ContentBean content = rows.get(0).getValue();
        assertThat(content, is(notNullValue()));
        assertThat(content.getId(),is("doc1"));
        assertThat(content.getRevision(),is("1"));
        assertThat(content, is(notNullValue()));
        assertThat(content.getValue(), is("foo"));

        content = rows.get(1).getValue();
        assertThat(content, is(notNullValue()));
        assertThat(content.getId(),is("doc2"));
        assertThat(content.getRevision(),is("1"));
        assertThat(content, is(notNullValue()));
        assertThat(content.getValue(), is("bar"));

    }

}
