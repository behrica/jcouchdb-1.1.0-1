package org.jcouchdb.document;

import java.util.HashMap;
import java.util.Map;

import org.jcouchdb.util.Util;
import org.svenson.AbstractDynamicProperties;
import org.svenson.JSONProperty;
import org.svenson.JSONTypeHint;

/**
 * Convenience base class for documents. Not abstract so it can be used fully dynamically.
 *
 * @author fforw at gmx dot de
 *
 */
public class BaseDocument
    extends AbstractDynamicProperties
    implements Document
{
    private String id;
    private String revision;
    private Map<String, Attachment> attachments;

    /* (non-Javadoc)
     * @see org.couchblog.db.Document#getId()
     */
    @JSONProperty(value = "_id", ignoreIfNull = true)
    public String getId()
    {
        return id;
    }

    /* (non-Javadoc)
     * @see org.couchblog.db.Document#setId(java.lang.String)
     */
    public void setId(String id)
    {
        this.id = id;
    }

    /* (non-Javadoc)
     * @see org.couchblog.db.Document#getRevision()
     */
    @JSONProperty(value="_rev",ignoreIfNull = true)
    public String getRevision()
    {
        return revision;
    }

    /* (non-Javadoc)
     * @see org.couchblog.db.Document#setRevision(java.lang.String)
     */
    public void setRevision(String revision)
    {
        this.revision = revision;
    }

    /**
     * Two documents are equal if they have the same id and the same revision.
     *
     */
    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Document)
        {
            Document that = (Document)obj;
            return Util.equals(this.getId(), that.getId()) && Util.equals(this.getRevision(),that.getRevision());
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return 17 + Util.safeHashcode(getId()) * 37 + Util.safeHashcode(getRevision()) * 37;
    }

    @JSONProperty(value="_attachments",ignoreIfNull = true)
    @JSONTypeHint(Attachment.class)
    public Map<String, Attachment> getAttachments()
    {
        return attachments;
    }

    public void setAttachments(Map<String, Attachment> attachments)
    {
        this.attachments = attachments;
    }

    public void addAttachment(String name, Attachment attachment)
    {
        if (attachments == null)
        {
            attachments = new HashMap<String, Attachment>();
        }
        attachments.put(name, attachment);
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString())
          .append("[ _id = ")
          .append( id)
          .append(", _rev = ")
          .append( revision)
          .append(", dynprops = {");

        for (String name : propertyNames())
        {
            sb.append(name).append(" = ").append(getProperty(name)).append(" ");
        }
        
        sb.append("}]");
        return sb.toString();
    }
}
