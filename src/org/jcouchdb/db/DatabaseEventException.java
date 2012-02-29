package org.jcouchdb.db;

import org.jcouchdb.exception.CouchDBException;

public class DatabaseEventException
    extends CouchDBException
{
    private static final long serialVersionUID = -8326948362656077407L;

    public DatabaseEventException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public DatabaseEventException(String message)
    {
        super(message);
    }

    public DatabaseEventException(Throwable cause)
    {
        super(cause);
    }

}
