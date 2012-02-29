package org.jcouchdb.db;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.jcouchdb.exception.CouchDBException;

/**
 * Represents a couchdb server
 *
 * @author fforw at gmx dot de
 */
public interface Server
{
    public final static int DEFAULT_PORT = 5984;

    /**
     * Returns a list with all database names.
     *
     * @return
     */
    List<String> listDatabases();

    /**
     * Creates the database with the given name
     * @param name
     * @return <code>true</code> if the database could be created, <code>false</code> if they already existed
     */
    boolean createDatabase(String name) throws CouchDBException;

    /**
     * Deletes the database with the given name
     * @param name
     */
    void deleteDatabase(String name) throws CouchDBException;

    /**
     * Send a GET request to the given URI
     * @param uri
     * @return
     */
    Response get(String uri) throws CouchDBException;

    /**
     * Send a PUT request to the given URI
     * @param uri
     * @return
     */
    Response put(String uri) throws CouchDBException;

    /**
     * Send a PUT request to the given URI with
     * the given body
     * @param uri
     * @return
     */
    Response put(String uri, String body) throws CouchDBException;

    /**
     * Send a PUT request to the given URI with
     * the given byte array body
     * @param uri
     * @param contentType   content type
     * @return
     */
    Response put(String uri, byte[] body, String contentType) throws CouchDBException;

    /**
     * Send a PUT request to the given URI with
     * the given body from the given InputStream
     * @param uri
     * @param contentType   content type
     * @return
     */
    Response put(String uri, InputStream inputStream, String contentType, long length) throws CouchDBException;

    /**
     * Send a POST request to the given URI with
     * the given body
     * @param uri
     * @return
     */
    Response post(String uri, String body) throws CouchDBException;


    /**
     * Send a DELETE request to the given URI
     *
     * @param uri
     * @return
     */
    Response delete(String uri) throws CouchDBException;

    /**
     * Sets the credentials for the given authentication scope.
     *
     * This method changes the state of the encapsulated commons http client which means
     * if you use this method, you must ensure that you use a different server instance
     * per autenticated user.
     *
     * @param authScope     authentication scope
     * @param credentials   credentials
     *
     */
    void setCredentials(AuthScope authScope, Credentials credentials);
    
    /**
     * Safely shuts down this server instance by closing all resources including HTTP connections.
     */
    void shutDown();

    /**
     * Returns <code>true</code> if {@link #shutDown()} has been called on this server.
     * 
     * @return
     */
    boolean isShutdown();
    
    /**
     * Get couchdb runtime statistics.
     * 
     * @param filter    filter for the stats (e.g. "/couchdb/request_time") or <code>null</code> in which case the output will be unfiltered.
     * @return  stats map
     */
    Map<String,Map<String,Object>> getStats(String filter);
    
    /**
     * Trigger replication from a database to another database. Both database can be either local names
     * (e.g. "exampleDB") or full URLs (e.g. "http://admin:password@example.org/exampleDB" ).
     * 
     * Replication is always a directed from source to target. if you want to replicate in both directions
     * you have to make two calls to {@link #replicate(String, String, boolean)}. 
     * 
     * @param source        source database name or URL
     * @param target        target database name or URL
     * @param continuous    if <code>true</code>, start continuous replication.
     * @param replication info
     */
    ReplicationInfo replicate(String source,String target, boolean continuous);


    /**
     * Requests a list of uuids from the CouchDB server
     * @param count     number of uuids to request
     * @return
     */
    List<String> getUUIDs(int count);
}

