package org.jcouchdb.document;

import java.util.List;

import org.svenson.JSONProperty;
import org.svenson.JSONTypeHint;

public class PollingResults
{
    private long lastSequence;
    
    private List<ChangeNotification> results;

    public long getLastSequence()
    {
        return lastSequence;
    }

    @JSONProperty("last_seq")
    public void setLastSequence(long lastSequence)
    {
        this.lastSequence = lastSequence;
    }

    @JSONTypeHint(ChangeNotification.class)
    public List<ChangeNotification> getResults()
    {
        return results;
    }

    public void setResults(List<ChangeNotification> results)
    {
        this.results = results;
    }

    @Override
    public String toString()
    {
        return super.toString() + "[lastSequence=" + lastSequence + ", results=" + results + "]";
    }
}
