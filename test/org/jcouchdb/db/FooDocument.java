package org.jcouchdb.db;

import org.jcouchdb.document.BaseDocument;

public class FooDocument extends BaseDocument
{
    /**
     * 
     */
    private static final long serialVersionUID = 5171600814795175855L;
    private String type="foo",value;

    public FooDocument()
    {

    }

    public FooDocument(String value)
    {
        this.value = value;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }
}
