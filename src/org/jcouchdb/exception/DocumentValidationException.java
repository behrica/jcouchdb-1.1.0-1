package org.jcouchdb.exception;

import java.util.Map;

import org.jcouchdb.db.Response;

/**
 * Is thrown when an update conflict happens
 *
 * @author fforw at gmx dot de
 *
 */
public class DocumentValidationException extends DataAccessException
{
    private static final long serialVersionUID = 6751156809306377676L;

    private String reason;

    private String error;
    
    public DocumentValidationException(Response response)
    {
        super(response);
        Map map = response.getContentAsMap();
        this.reason = (String)map.get("reason");
        this.error = (String)map.get("error");
    }
    
    public String getReason()
    {
        return reason;
    }
    
    public String getError()
    {
        return error;
    }
}
