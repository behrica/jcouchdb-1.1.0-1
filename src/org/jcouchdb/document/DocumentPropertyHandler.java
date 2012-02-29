package org.jcouchdb.document;

public interface DocumentPropertyHandler
{
    String getId(Object document);


    String getRevision(Object document);


    void setRevision(Object document, String revision);


    void setId(Object document, String id);

}
