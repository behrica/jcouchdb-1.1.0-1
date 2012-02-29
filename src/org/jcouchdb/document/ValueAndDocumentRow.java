package org.jcouchdb.document;

import org.svenson.JSONProperty;

/**
 * One row of a view result containing a view value and a document.
 *
 * @author fforw at gmx dot de
 *
 * @param <V> type of the contained value
 * @param <D> type of the contained document
 */
public class ValueAndDocumentRow<V,D>
    extends ValueRow<V>
{
    private D document;

    @JSONProperty("doc")
    public D getDocument()
    {
        return document;
    }

    public void setDocument(D document)
    {
        this.document = document;
    }
}
