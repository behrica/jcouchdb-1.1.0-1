package org.jcouchdb.document;

import java.util.ArrayList;
import java.util.List;

import org.svenson.JSONTypeHint;

/**
 * Encapsulates the result of a view query.
 *
 * @author fforw at gmx dot de
 *
 * @param <V>   type of the view result rows.
 */
public class ViewResult<V> extends AbstractViewResult<V>
{
    /**
     * 
     */
    private static final long serialVersionUID = 7445876405961695911L;
    List<ValueRow<V>> rows = new ArrayList<ValueRow<V>>();

    public List<ValueRow<V>> getRows()
    {
        return rows;
    }

    @JSONTypeHint(ValueRow.class)
    public void setRows(List<ValueRow<V>> rows)
    {
        this.rows = rows;
    }

    @Override
    public String toString()
    {
        return super.toString()+ ", value rows = " + rows;
    }
}
