package org.jcouchdb.exception;

public class NoResponseException
    extends DataAccessException
{
    private static final long serialVersionUID = -8044281987441037823L;

    public NoResponseException()
    {
        super("No response exception", null);
    }

}
