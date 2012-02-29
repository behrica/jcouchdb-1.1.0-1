package org.jcouchdb.document;

import java.util.ArrayList;
import java.util.List;

import org.svenson.JSONTypeHint;

public class ViewAndDocumentsResult<V,D> extends AbstractViewResult
{
    /**
     * 
     */
    private static final long serialVersionUID = 5825957642465235667L;
    List<ValueAndDocumentRow<V,D>> rows = new ArrayList<ValueAndDocumentRow<V,D>>();

    public List<ValueAndDocumentRow<V,D>> getRows()
    {
        return rows;
    }

    @JSONTypeHint(ValueAndDocumentRow.class)
    public void setRows(List<ValueAndDocumentRow<V,D>> rows)
    {
        this.rows = rows;
    }

    @Override
    public String toString()
    {
        return super.toString()+ ", value rows = " + rows;
    }

}
