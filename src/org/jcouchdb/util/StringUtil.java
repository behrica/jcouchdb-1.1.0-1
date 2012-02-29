package org.jcouchdb.util;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class StringUtil
{
    protected StringUtil()
    {
    }

    public static List<String> split(String s, String delim)
    {
        StringTokenizer tokenizer = new StringTokenizer(s, delim);
        List<String> l = new ArrayList<String>();
        while (tokenizer.hasMoreTokens())
        {
            l.add(tokenizer.nextToken().trim());
        }

        return l;
    }

    public static String join(List<?> l, String separator)
    {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Object o : l)
        {
            String s = o.toString();
            if (!first)
            {
                sb.append(separator);
            }
            sb.append(s);
            first = false;
        }
        return sb.toString();
    }

}
