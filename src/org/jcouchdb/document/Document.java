package org.jcouchdb.document;

import java.util.Map;

import org.svenson.JSONProperty;
import org.svenson.JSONTypeHint;
/**
 * Interface for Documents used with jcouchdb.
 *
 * You don't actually have to implement Document, but your class needs
 * to be able to be fed both "_id" and "_rev" properties -- without
 * those properties you cannot work with CouchDB anyway.
 *
 * @author fforw at gmx dot de
 *
 */
public interface Document
{

    @JSONProperty( value = "_id", ignoreIfNull = true)
    String getId();

    void setId(String id);

    @JSONProperty( value = "_rev", ignoreIfNull = true)
    String getRevision();

    void setRevision(String revision);

    @JSONProperty( value = "_attachments", ignoreIfNull = true)
    @JSONTypeHint(Attachment.class)
    Map<String,Attachment> getAttachments();

    void setAttachments(Map<String,Attachment> attachments);

}
