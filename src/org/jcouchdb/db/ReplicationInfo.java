package org.jcouchdb.db;

import java.util.List;
import java.util.Map;

import org.svenson.AbstractDynamicProperties;
import org.svenson.JSONProperty;
import org.svenson.JSONTypeHint;

public class ReplicationInfo extends AbstractDynamicProperties
{
    private String sourceUpdateSequencePosition, sessionId;
    private boolean ok;
    private List<Map<String,Object>> history;

    @JSONProperty("source_last_seq")
    public String getSourceUpdateSequencePosition()
    {
        return sourceUpdateSequencePosition;
    }
    
    public void setSourceUpdateSequencePosition(String sourceUpdateSequencePosition)
    {
        this.sourceUpdateSequencePosition = sourceUpdateSequencePosition;
    }

    @JSONProperty("session_id")
    public String getSessionId()
    {
        return sessionId;
    }


    public void setSessionId(String sessionId)
    {
        this.sessionId = sessionId;
    }

    public boolean isOk()
    {
        return ok;
    }

    public void setOk(boolean ok)
    {
        this.ok = ok;
    }
    
    @JSONTypeHint(Map.class)
    public List<Map<String, Object>> getHistory()
    {
        return history;
    }
    
    public void setHistory(List<Map<String, Object>> history)
    {
        this.history = history;
    }

    @Override
    public String toString()
    {
        return "ReplicationInfo@" + Integer.toHexString(hashCode()) + ": [history=" + history + ", ok=" + ok + ", sessionId=" + sessionId +
            ", sourceUpdateSequencePosition=" + sourceUpdateSequencePosition + "]";
    }
    
}
