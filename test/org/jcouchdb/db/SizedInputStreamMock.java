package org.jcouchdb.db;

import java.io.IOException;
import java.io.InputStream;

public class SizedInputStreamMock extends InputStream
{

    private long length;
    private byte contentByte;

    public SizedInputStreamMock(byte contentByte, long length)
    {
        this.contentByte = contentByte;
        this.length = length;
    }
    
    @Override
    public int read() throws IOException
    {
        if (--length > -1)
        {
            return contentByte & 0xff;
        }
        else
        {
            return -1;
        }
    }
}
