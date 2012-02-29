package org.jcouchdb.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;

public class MediaTypeUtil
{
    private static final String UNKNOWN_MEDIA_TYPE = "application/octet-stream";

    private Map<String, String> suffixToMediaType = new HashMap<String, String>();


    public MediaTypeUtil()
    {
        init(this.getClass().getClassLoader().getResourceAsStream("org/jcouchdb/util/mime.types"));
    }

    public MediaTypeUtil(InputStream is)
    {
        init(is);
    }

    private void init(InputStream is)
    {
        try
        {
            for (String s : (List<String>) IOUtils.readLines(is))
            {
                if (!s.startsWith("#") && !Util.hasText(s))
                {
                    continue;
                }
                String[] parts = s.split("\\s+");

                if (parts.length > 1)
                {
                    for (int i = 1; i < parts.length; i++)
                    {
                        suffixToMediaType.put(parts[i], parts[0]);
                    }
                }
            }
        }
        catch (IOException e)
        {
            throw ExceptionWrapper.wrap(e);
        }

    }




    public String getMediaTypeForName(String name)
    {
        String mediaType = null;

        int pos = name.lastIndexOf('.');
        if (pos >= 0)
        {
            String ext = name.substring(pos + 1).toLowerCase();
            mediaType = suffixToMediaType.get(ext);
        }

        if (mediaType == null)
        {
            return UNKNOWN_MEDIA_TYPE;
        }
        else
        {
            return mediaType;
        }
    }
}
