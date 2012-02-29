package org.jcouchdb.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.junit.Test;



public class Base64UtilTestCase
{
    protected static Logger log = Logger.getLogger(Base64UtilTestCase.class);

    @Test
    public void thatEncodingWorks()
    {
        String base64 = Base64Util.encodeBase64(new byte[]{65});
        assertThat(base64, is("QQ=="));
        base64 = Base64Util.encodeBase64(new byte[]{65,66});
        assertThat(base64, is("QUI="));
        base64 = Base64Util.encodeBase64(new byte[]{65,66,67});
        assertThat(base64, is("QUJD"));
    }

    @Test
    public void thatEncodingASamplePNGWorks() throws IOException
    {
        String base64 = Base64Util.encodeBase64( getTestData());

        assertThat(base64, is("AAECAwQFBgcICQoLDA0ODxAREhMUFRYXGBkaGxwdHh8gISIjJCUmJygpKissLS4vMDEyMzQ1Njc4OTo7PD0+P0BBQkNERUZHSElKS0xNTk9QUVJTVFVWV1hZWltcXV5fYGFiY2RlZmdoaWprbG1ub3BxcnN0dXZ3eHl6e3x9fn+AgYKDhIWGh4iJiouMjY6PkJGSk5SVlpeYmZqbnJ2en6ChoqOkpaanqKmqq6ytrq+wsbKztLW2t7i5uru8vb6/wMHCw8TFxsfIycrLzM3Oz9DR0tPU1dbX2Nna29zd3t/g4eLj5OXm5+jp6uvs7e7v8PHy8/T19vf4+fr7/P3+/w=="));
    }

    private byte[] getTestData()
    {
        byte[] data = new byte[256];
        for (int i = 0; i < 256; i++ )
        {
            data[i] = (byte)i;
        }
        return data;
    }

//    @Test
//    public void writeTestData() throws IOException
//    {
//        FileUtils.writeByteArrayToFile(new File("/tmp/test.data"), getTestData());
//    }

}
