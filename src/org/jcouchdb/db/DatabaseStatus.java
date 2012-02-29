package org.jcouchdb.db;

import org.svenson.AbstractDynamicProperties;
import org.svenson.JSONProperty;

/**
 * Provides information about the current couchdb server status
 *
 * @author fforw at gmx dot de
 *
 */
public class DatabaseStatus
    extends AbstractDynamicProperties
{
    private String name;

    private long documentCount;

    private long deletedDocumentCount;

    private int updateSequence;

    private boolean compactRunning;

    private long diskSize;

    @JSONProperty("db_name")
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @JSONProperty("doc_count")
    public long getDocumentCount()
    {
        return documentCount;
    }

    public void setDocumentCount(long documentCount)
    {
        this.documentCount = documentCount;
    }

    @JSONProperty("doc_del_count")
    public long getDeletedDocumentCount()
    {
        return deletedDocumentCount;
    }

    public void setDeletedDocumentCount(long deletedDocumentCount)
    {
        this.deletedDocumentCount = deletedDocumentCount;
    }

    @JSONProperty("update_seq")
    public int getUpdateSequence()
    {
        return updateSequence;
    }

    public void setUpdateSequence(int updateSequence)
    {
        this.updateSequence = updateSequence;
    }

    @JSONProperty("compact_running")
    public boolean isCompactRunning()
    {
        return compactRunning;
    }

    public void setCompactRunning(boolean compactRunning)
    {
        this.compactRunning = compactRunning;
    }

    @JSONProperty("disk_size")
    public long getDiskSize()
    {
        return diskSize;
    }

    public void setDiskSize(long diskSize)
    {
        this.diskSize = diskSize;
    }

    @Override
    public String toString()
    {
        return super.toString()+"name = "+name+", documentCount = "+documentCount+", deletedDocumentCount = "+deletedDocumentCount+
            ", updateSequence = "+updateSequence+", compactRunning = "+compactRunning+", diskSize = "+diskSize;
    }
}
