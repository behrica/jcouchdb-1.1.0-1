package org.jcouchdb.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.jcouchdb.db.Database;
import org.jcouchdb.document.BaseDocument;
import org.jcouchdb.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceSync
{
    private static Logger log = LoggerFactory.getLogger(ResourceSync.class);
    
    private Database database;

    private File resourceBaseDir;

    private String resourceBaseDocId = "res";
    
    private MediaTypeUtil mediaTypeUtil = new MediaTypeUtil();
    
    
    public void setResourceBaseDocId(String resourceBaseDocId)
    {
        this.resourceBaseDocId = resourceBaseDocId;
    }
    
    public String getResourceBaseDocId()
    {
        return resourceBaseDocId;
    }


    public void setDatabase(Database database)
    {
        this.database = database;
    }


    public void setResourceBaseDir(File resourceBaseDir)
    {
        Assert.notNull(resourceBaseDir, "resourceBaseDir can't be null");

        if (resourceBaseDir.exists())
        {
            Assert.isTrue(resourceBaseDir.isDirectory(), resourceBaseDir + " is not a directory");
        }
        else
        {
            resourceBaseDir.mkdirs();
        }

        this.resourceBaseDir = resourceBaseDir;
    }


    public void syncResources()
    {
        String revision = null; 
        try
        {
            BaseDocument doc = database.getDocument(BaseDocument.class, resourceBaseDocId);
            revision = doc.getRevision();
            
        }
        catch(NotFoundException e)
        {
            // revision stays null for "no document";
        }

        String basePath = resourceBaseDir.getPath();
        
        for (File f : (Collection<File>)FileUtils.listFiles(resourceBaseDir, TrueFileFilter.INSTANCE, new IgnoreSVNDirFilter()))
        {
            String path = f.getPath();
            if (!path.startsWith(basePath))
            {
                throw new IllegalStateException(f + "'s path does not start with '" + basePath + "'");
            }
            
            String attachmentId = path.substring(basePath.length() + 1);
            
            log.debug("found file {} => attachment id = {}", f,attachmentId);
            
            String mediaType = mediaTypeUtil.getMediaTypeForName(attachmentId);
            try
            {
                revision = database.createAttachment(resourceBaseDocId, revision, attachmentId, mediaType, new FileInputStream(f), f.length());
            }
            catch (FileNotFoundException e)
            {
                throw ExceptionWrapper.wrap(e);
            }
        }
    }
    
    private final static String SVN_INFIX = File.separator + ".svn";
    
    public static class IgnoreSVNDirFilter implements IOFileFilter
    {

        public boolean accept(File arg0)
        {
            return arg0.getPath().indexOf(SVN_INFIX) < 0;
        }

        public boolean accept(File arg0, String arg1)
        {
            String path = arg0.getPath() + File.separator + arg1;
            return path.indexOf(SVN_INFIX) < 0;
        }
        
    }
}