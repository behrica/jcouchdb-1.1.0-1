package org.jcouchdb.db;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.jcouchdb.exception.DataAccessException;
import org.jcouchdb.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.svenson.JSONParser;
import org.svenson.tokenize.InputStreamSource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Encapsulates a couchdb server response with error code and received body.
 *
 * @author fforw at gmx dot de
 */
public class Response
{
    protected static Logger log = LoggerFactory.getLogger(Response.class);

    private int code;

    private JSONParser parser;

    private Header[] headers;

    private InputStream inputStream;

    private InputStreamSource inputStreamSource;

    private byte[] content;

    public Response(int code, String s)
    {
        this(code, new ByteArrayInputStream(s.getBytes()), null);
    }


    public Response(int code, InputStream stream, int length)
    {
        this(code, stream, null);
    }

    public Response(HttpResponse response) throws IOException
    {
        this(response.getStatusLine().getStatusCode(), response.getEntity().getContent(), response.getAllHeaders());
    }

    public Response(int code, InputStream stream, Header[] headers)
    {
        Assert.notNull(stream, "stream can't be null");

        this.inputStream = stream;
        this.code = code;
        this.headers = headers;

        log.trace("ctor {}", this);
    }

    public void setParser(JSONParser parser)
    {
        this.parser = parser;
    }


    private JSONParser getParser()
    {
        if (parser == null)
        {
            parser = new JSONParser();
        }
        return parser;
    }


    public int getCode()
    {
        return code;
    }


    public byte[] getContent()
    {
        if (content == null)
        {
            try
            {
                content = IOUtils.toByteArray(inputStream);
            }
            catch (IOException e)
            {
                throw new DataAccessException("error reading content from response", null);
            }
            finally
            {
                try
                {
                    inputStream.close();
                }
                catch (IOException e)
                {
                    throw new DataAccessException("error closing input stream of response", null);
                }
            }
        }
        return content;
    }


    public String getContentAsString()
    {
        if (log.isDebugEnabled())
        {
            log.debug("getContentAsString on " + this);
        }
        return new String(getContent());
    }


    /**
     * Returns the contents of the response as List
     *
     * @return
     */
    public List getContentAsList()
    {
        List list = getParser().parse(List.class, getCharacterSource());
        return list;
    }


    /**
     * Returns the contents of the response as Map
     *
     * @return
     */
    public Map getContentAsMap()
    {
        Map map = getParser().parse(Map.class, getCharacterSource());
        return map;
    }


    /**
     * Returns the contents of the response as bean of the given type.
     *
     * @return
     */
    public <T> T getContentAsBean(Class<T> cls)
    {
        T t = getParser().parse(cls, getCharacterSource());
        return t;
    }


    private InputStreamSource getCharacterSource()
    {
        if (inputStreamSource == null)
        {
            inputStreamSource = new InputStreamSource(inputStream, false);
        }
        return inputStreamSource;
    }


    public Header[] getResponseHeaders()
    {
        return headers;
    }


    public InputStream getInputStream()
    {
        return inputStream;
    }


    /**
     * Returns <code>true</code> if the response code is between 200 and 299
     *
     * @return
     */
    public boolean isOk()
    {
        return code >= 200 && code <= 299;
    }


    @Override
    public String toString()
    {
        return super.toString() + ": code = " + code + ", stream = " + inputStream;
    }


    public void destroy()
    {
        if (inputStream != null)
        {
            try
            {
                inputStream.close();
            }
            catch (IOException e)
            {
                log.warn("error trying to close the input stream", e);
            }
        }
    }
}
