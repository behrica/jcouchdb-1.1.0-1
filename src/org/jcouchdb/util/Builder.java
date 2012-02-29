package org.jcouchdb.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Builder
{
    public static Map object(Object... objects)
    {
        if ((objects.length & 1) != 0)
        {
            throw new IllegalArgumentException("objects needs to be an even number of objects given as key followed by a value.");
        }

        Map m = new HashMap();
        for (int i=0; i < objects.length; i+=2)
        {
            m.put(objects[i], objects[i+1]);
        }
        return m;
    }


    public static List array(Object... objects)
    {
        List l = new ArrayList();
        for (int i=0; i < objects.length; i++)
        {
            l.add(i);
        }
        return l;
    }
}
