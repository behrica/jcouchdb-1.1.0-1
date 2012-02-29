package org.jcouchdb.exception;

import org.jcouchdb.db.Response;

/**
 * Is thrown when an update conflict happens
 *
 * @author fforw at gmx dot de
 *
 */
public class UpdateConflictException extends DataAccessException
{
    private static final long serialVersionUID = 2138858909017501051L;

    public UpdateConflictException(Response resp)
    {
        super(resp);
    }

    public UpdateConflictException(String message, Response resp)
    {
        super(message, resp);
    }
}
