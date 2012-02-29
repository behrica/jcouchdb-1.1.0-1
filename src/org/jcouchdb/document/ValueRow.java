package org.jcouchdb.document;

import org.svenson.AbstractDynamicProperties;

public class ValueRow<V>
    extends AbstractDynamicProperties
{

    private String id;

    private Object key;

    private V value;

    /**
     * Returns the id of the result object
     *
     * @return
     */
    public String getId()
    {
        return id;
    }

    /**
     * Sets the id of the result object
     *
     * @param id
     */
    public void setId(String id)
    {
        this.id = id;
    }

    /**
     * Returns the key this object was mapped to
     *
     * @return
     */
    public Object getKey()
    {
        return key;
    }

    public void setKey(Object key)
    {
        this.key = key;
    }

    /**
     * Returns the value mapped to this row.
     *
     * @return
     */
    public V getValue()
    {
        return value;
    }

    public void setValue(V value)
    {
        this.value = value;
    }

    @Override
    public String toString()
    {
        return super.toString() + ": id = " + id + ", key = " + key + ", value = " + value;
    }

}
