package org.jcouchdb.db;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jcouchdb.exception.CouchDBException;
import org.svenson.JSON;
import org.svenson.JSONParser;

/**
 * Used to pass query options to view queries.
 * For example:
 * <pre>
 * database.queryView("company/all", Map.class, 
 *     new Options().count(1).descending(true));
 * </pre>
 * 
 * <p>
 * In contrast to earlier versions of this class it became clear that some options
 * needs to be JSON encoded and some options musn't be JSON encoded. There is no way around that,
 * that's just the way CouchDB works.
 * <p>
 * Internally, this class keeps a list of options that need JSON encoding:
 * <ul>
 * <li>key</li>
 * <li>startkey</li>
 * <li>endkey</li>
 * </ul>
 * 
 * It will automatically encode those option names. If you need to have non-supported options encoded
 * you have to subclass options and then access {@link #putEncoded(String, Object)}.
 *
 * @see Database#getDocument(Class, String)
 * @see Database#queryView(String, Class, Options, JSONParser)
 * @see Database#queryAdHocView(Class, String, Options, JSONParser)
 * @author fforw at gmx dot de
 *
 */
public class Options
{
    private static final long serialVersionUID = -4025495141211906568L;

    private JSON optionsJSON = new JSON();

    private Map<String, Object> content = new HashMap<String, Object>();
    
    final static Set<String> JSON_ENCODED_OPTIONS = Collections.unmodifiableSet(new HashSet<String>(Arrays.asList(
            "key",
            "startkey",
            "endkey"
        )));
    
    public Options()
    {

    }

    public Options(Map<String,Object> map)
    {
        for (Map.Entry<String, Object> e : map.entrySet())
        {
            put(e.getKey(), e.getValue());
        }
    }

    /**
     * Copies the options of the given Options object if it is not <code>null</code>.
     *
     * @param options   Options to be copied, can be <code>null</code>.
     */
    public Options(Options options)
    {
        if (options != null)
        {
            // options values are allready encoded thus need all to be added unencoded
            for (String key : options.keys())
            {
                putUnencoded(key,options.get(key));
            }
        }
    }

    public Options(String key, Object value)
    {
        putUnencoded(key, value);
    }

    public Options put(String key, Object value)
    {
        if (JSON_ENCODED_OPTIONS.contains(key))
        {
            return putEncoded(key, value);
        }
        else
        {
            return putUnencoded(key, value);
        }
    }

    protected Options putEncoded(String key, Object value)
    {
        String json = optionsJSON.forValue(value);
        content.put(key, json);
        return this;
    }

    protected Options putUnencoded(String key, Object value)
    {
        content.put(key, value);
        return this;
    }

    public Options key(Object key)
    {
        return putEncoded("key",key);
    }

    public Options startKey(Object key)
    {
        return putEncoded("startkey",key);
    }

    public Options startKeyDocId(String docId)
    {
        return putUnencoded("startkey_docid", docId);
    }

    public Options endKey(Object key)
    {
        return putEncoded("endkey",key);
    }

    public Options endKeyDocId(String docId)
    {
        return putUnencoded("endkey_docid", docId);
    }
    
    public Options limit(int limit)
    {
        return putUnencoded("limit", limit);
    }

    public Options update(boolean update)
    {
        return putUnencoded("update",update);
    }

    public Options descending(boolean update)
    {
        return putUnencoded("descending",update);
    }

    public Options skip(int skip)
    {
        return putUnencoded("skip",skip);
    }

    public Options group(boolean group)
    {
        return putUnencoded("group",group);
    }
    
    public Options stale()
    {
        return putUnencoded("stale","ok");
    }

    public Options reduce(boolean reduce)
    {
        return putUnencoded("reduce",reduce);
    }

    public Options includeDocs(boolean includeDocs)
    {
        return putUnencoded("include_docs",includeDocs);
    }

    public Options groupLevel(int level)
    {
        return putUnencoded("group_level",level);
    }
    
    public String toQuery()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("?");

        boolean first = true;
        try
        {
            for (String key : keys())
            {
                if (!first)
                {
                    sb.append("&");
                }
                sb.append(key).append("=");
                sb.append(URLEncoder.encode(get(key).toString(), "UTF-8"));
                first = false;
            }
            if (sb.length() <= 1)
            {
                return "";
            }
            else
            {
                return sb.toString();
            }
        }
        catch (UnsupportedEncodingException e)
        {
            throw new CouchDBException("error converting option value", e);
        }
    }

    public Object get(String key)
    {
        return content.get(key);
    }

    /**
     * Can be imported statically to have a syntax a la <code>option().count(1);</code>.
     * @return new Option instance
     */
    public static Options option()
    {
        return new Options();
    }
    
    public Set<String> keys()
    {
        return content.keySet();
    }
    
}
