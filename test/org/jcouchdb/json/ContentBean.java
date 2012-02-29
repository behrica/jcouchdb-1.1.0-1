/**
 *
 */
package org.jcouchdb.json;

import org.jcouchdb.document.BaseDocument;

public class ContentBean extends BaseDocument
{
    /**
     * 
     */
    private static final long serialVersionUID = 6773915142590875257L;
    private String value;

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }
}