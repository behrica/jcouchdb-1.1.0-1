package org.jcouchdb.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.File;

import org.junit.Test;


public class MediaTypeUtilTestCase
{
    @Test
    public void testMediaType()
    {
        MediaTypeUtil util = new MediaTypeUtil();
        
        assertThat(util.getMediaTypeForName("test.gif"), is("image/gif"));
        assertThat(util.getMediaTypeForName("test.png"), is("image/png"));
        assertThat(util.getMediaTypeForName("test.jpg"), is("image/jpeg"));
        assertThat(util.getMediaTypeForName("test.html"), is("text/html"));
        assertThat(util.getMediaTypeForName("test.css"), is("text/css"));
        assertThat(util.getMediaTypeForName("test.js"), is("application/x-javascript"));
    }
    
    @Test
    public void testWithFile()
    {
        File upFile = new File("/Users/kanemu/Desktop/tmpimage/2A082_E0100.jpg");
        MediaTypeUtil mutil = new MediaTypeUtil();
        assertThat(mutil.getMediaTypeForName(upFile.getName()), is("image/jpeg"));
    }
}