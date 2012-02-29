package org.jcouchdb.document;

import org.svenson.JSONProperty;

public abstract class AbstractViewResult<V>
    extends BaseDocument
{

    /**
     * 
     */
    private static final long serialVersionUID = 7412381861539478968L;

    private int totalRows;

    private int offset;

    public int getTotalRows()
    {
        return totalRows;
    }

    @JSONProperty("total_rows")
    public void setTotalRows(int totalRows)
    {
        this.totalRows = totalRows;
    }

    public int getOffset()
    {
        return offset;
    }

    public void setOffset(int offset)
    {
        this.offset = offset;
    }

    @Override
    public String toString()
    {
        return super.toString() + ": totalRows = " + totalRows + ", offset = " + offset;
    }

}
