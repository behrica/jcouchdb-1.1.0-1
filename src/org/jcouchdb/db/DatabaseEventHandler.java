package org.jcouchdb.db;

/**
 * Allows java-side interception of couchdb operations on documents
 * @author fforw at gmx dot de
 *
 */
public interface DatabaseEventHandler
{
    /**
     * Called when a document is about to be created. Any exception thrown
     * will prevent creation. 
     * 
     * @param db            database
     * @param document      Document
     * @throws Exception    
     */
    void creatingDocument(Database db, Object document) throws Exception;

    /**
     * Called when after a document is created. 
     * 
     * @param db            database
     * @param document      Document
     * @param response      response received from couchdb
     */
    void createdDocument(Database db, Object document, Response response);

    /**
     * Called when a document is about to be updated. Any exception thrown
     * will prevent the update. 
     * 
     * @param db            database
     * @param document      Document
     * @throws Exception    
     */    
    void updatingDocument(Database db, Object document) throws Exception;

    /**
     * Called when after a document is updated. 
     * 
     * @param db            database
     * @param document      Document
     * @param response      response received from couchdb
     */
    void updatedDocument(Database db, Object document, Response response);

    /**
     * Called when a document is about to be deleted. Any exception thrown
     * will prevent the deletion. 
     * 
     * @param db            database
     * @param document      Document
     * @throws Exception    
     */    
    void deletingDocument(Database db, String id, String rev) throws Exception;

    /**
     * Called when after a document is deleted. 
     * 
     * @param db            database
     * @param document      Document
     * @param response      response received from couchdb or <code>null</code> if the call was a bulk delete.
     */
    void deletedDocument(Database db, String id, String rev, Response response);
}
