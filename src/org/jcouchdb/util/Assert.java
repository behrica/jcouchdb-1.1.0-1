package org.jcouchdb.util;

public class Assert
{
    protected Assert()
    {

    }

    public static void notNull(Object o, String msg)
    {
        if ( o == null)
        {
            throw new IllegalArgumentException(msg);
        }
    }

    public static void isTrue(boolean value, String msg)
    {
        if (!value)
        {
            throw new IllegalArgumentException(msg);
        }
    }

}
