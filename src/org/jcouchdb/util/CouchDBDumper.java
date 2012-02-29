package org.jcouchdb.util;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.jcouchdb.db.Database;
import org.jcouchdb.db.Server;
import org.jcouchdb.document.Attachment;
import org.jcouchdb.document.BaseDocument;
import org.jcouchdb.document.ValueRow;
import org.jcouchdb.document.ViewResult;
import org.svenson.JSON;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class CouchDBDumper
{
    private static Logger log = LoggerFactory.getLogger(CouchDBDumper.class);

    public void dumpDatabase(Server server, String name, OutputStream os, boolean inlineAttachments) throws IOException
    {
        ZipOutputStream zos = null;
        try
        {
            zos = new ZipOutputStream(os);
            Database database = new Database(server, name);

            ViewResult<Map> result = database.listDocuments(null, null);

            for (ValueRow<Map> row : result.getRows())
            {
                String id = row.getId();
                Map value = row.getValue();
                String revision = (String) value.get("rev");
                dumpDocumentAndAttachments(zos, database, id, revision, inlineAttachments);
            }
        }
        catch(Exception e)
        {
            log.error("",e);
        }
        finally
        {
            if (zos != null)
            {
                zos.close();
            }
        }
    }

    private void dumpDocumentAndAttachments(ZipOutputStream zos, Database database, String id, String revision, boolean inlineAttachments) throws IOException
    {
        BaseDocument doc = database.getDocument(BaseDocument.class, id, revision, null);

        String path = idToRelPath(id);

        if (inlineAttachments)
        {
            Map<String, Attachment> attachments = doc.getAttachments();
            if (attachments != null && attachments.size() > 0)
            {
                for (Map.Entry<String,Attachment> e : attachments.entrySet())
                {
                    String name = e.getKey();
                    Attachment attachment = e.getValue();

                    byte[] content = database.getAttachment(doc.getId(), name);
                    attachment.setStub(false);
                    String encodedData = Base64Util.encodeBase64(content);
                    attachment.setData(encodedData);
                }
            }
        }

        byte[] data = JSON.defaultJSON().forValue(doc).getBytes("UTF-8");

        ZipEntry entry = new ZipEntry(path+".json");
        entry.setSize(data.length);
        zos.putNextEntry(entry);

        zos.write( data, 0, data.length);
        zos.flush();
        zos.closeEntry();

        if (!inlineAttachments)
        {
            Map<String, Attachment> attachments = doc.getAttachments();
            if (attachments != null && attachments.size() > 0)
            {
                String attachmentPath = path+"_attachments/";
                for (String name : attachments.keySet())
                {
                    byte[] content = database.getAttachment(doc.getId(), name);

                    zos.putNextEntry(new ZipEntry(attachmentPath + idToRelPath(name)));
                    zos.write(content);
                    zos.closeEntry();
                }
            }
        }
    }

    private String idToRelPath(String id)
    {
        return id;
    }

}
