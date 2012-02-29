package org.jcouchdb.document.test;

import org.jcouchdb.document.BaseDocument;
import org.svenson.JSONProperty;

public abstract class TestBase extends BaseDocument
{
    @JSONProperty(readOnly = true)
    public String getType()
    {
        return this.getClass().getSimpleName();
    }

}
