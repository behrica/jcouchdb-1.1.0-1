package org.jcouchdb.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.jcouchdb.db.Database;
import org.jcouchdb.db.Server;
import org.jcouchdb.document.Attachment;
import org.jcouchdb.document.BaseDocument;
import org.jcouchdb.exception.NotFoundException;
import org.svenson.JSONParser;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class CouchDBLoader
{
    private static final String JSON_EXTENSION = ".json";

    private static Logger log = LoggerFactory.getLogger(CouchDBLoader.class);

    public void load(ZipInputStream zis, Server server, String name) throws IOException
    {
        if (!server.listDatabases().contains(name))
        {
            server.createDatabase(name);
        }

        Database database = new Database(server, name);

        new LoadJob().mergeDatabase(database, zis);

    }

    private class LoadJob
    {
        private static final String CONFLICTS_PROPERTY_NAME = "jcl_conflicts";

        private static final String ATTACHMENTS_SUFFIX = "_attachments";

        private Map<String, BaseDocument> documentsWithAttachments = new HashMap<String, BaseDocument>();

        private Set<AttachmentEntry> attachmentEntries = new HashSet<AttachmentEntry>();

        private void mergeDatabase(Database database, ZipInputStream zis) throws IOException
        {

            Assert.notNull(database, "database can't be null");
            Assert.notNull(zis, "ZIP input stream can't be null");

            try
            {
                ZipEntry entry;
                while ((entry = zis.getNextEntry()) != null)
                {
                    String name = entry.getName().replace('\\', '/');
                    boolean isAttachment = (name.indexOf(ATTACHMENTS_SUFFIX) >= 0);
                    boolean isJSON = name.endsWith(JSON_EXTENSION);

                    if ((!isAttachment && !isJSON) || (isJSON && isAttachment))
                    {
                        log.error("ignoreing zip entry " + entry);
                    }
                    else
                    {
                        if (isJSON)
                        {
                            BaseDocument doc = readDocument(database, zis, entry);

                            if (doc.getAttachments() == null || doc.getAttachments().size() == 0)
                            {
                                createDocument(database, doc);
                            }
                            else
                            {
                                documentsWithAttachments.put(doc.getId(), doc);
                            }
                        }
                        else
                        {
                            createAttachmentEntry(database, zis, entry);
                        }
                    }
                }

                for (AttachmentEntry attachmentEntry : attachmentEntries)
                {
                    if (!findDocAndAddAttachment(attachmentEntry))
                    {
                        log.warn("Ignoring attachment "+attachmentEntry);
                    }
                }

                for (BaseDocument doc : documentsWithAttachments.values())
                {
                    createDocument(database, doc);
                }
            }
            catch(RuntimeException e)
            {
                throw e;
            }
            finally
            {
                zis.close();
            }
        }

        private BaseDocument readDocument(Database database, ZipInputStream zis, ZipEntry entry)
            throws IOException
        {
            String json = new String(readZipEntryData(zis), "UTF-8");
            return new JSONParser().parse(BaseDocument.class, json);
        }

        private byte[] readZipEntryData(ZipInputStream zis) throws IOException
        {
            byte[] buf = new byte[4096];

            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            int count;
            while ( ( count = zis.read(buf,0,4096)) != -1)
            {
                bos.write(buf, 0, count);
            }

            return bos.toByteArray();
        }

        private void createDocument(Database database, BaseDocument doc)
        {
            String docId = doc.getId();

            BaseDocument existingDoc = loadDocumentIfExists(database, docId);

            if (existingDoc != null)
            {
                if (doc.getRevision() != null &&
                    doc.getRevision().equals(existingDoc.getRevision()))
                {
                    // ignore document with same revision
                }
                else
                {
                    // add as conflict to existing

                    if (log.isInfoEnabled())
                    {
                        log.info("adding entry " + doc + " to '" + CONFLICTS_PROPERTY_NAME + "' in "+existingDoc);
                    }
                    List<Object> conflicts = (List<Object>) existingDoc.getProperty(CONFLICTS_PROPERTY_NAME);
                    if (conflicts == null)
                    {
                        conflicts = new ArrayList<Object>();
                        existingDoc.setProperty(CONFLICTS_PROPERTY_NAME, conflicts);
                    }
                    conflicts.add(doc);

                    database.updateDocument(existingDoc);
                }
            }
            else
            {
                doc.setRevision(null);
                database.createDocument(doc);
            }
        }

        private BaseDocument loadDocumentIfExists(Database database, String docId)
        {
            BaseDocument existingDoc;
            try
            {
                existingDoc = database.getDocument(BaseDocument.class, docId);
            }
            catch (NotFoundException nfe)
            {
                existingDoc = null;
            }
            return existingDoc;
        }

        private void createAttachmentEntry(Database database, ZipInputStream zis, ZipEntry entry) throws IOException
        {
            byte[] data = readZipEntryData(zis);

            String name = entry.getName();

            int pos = name.indexOf(ATTACHMENTS_SUFFIX);

            String docId = name.substring(0, pos).replace("/", "%2F");

            String attachmentId = name.substring(pos + ATTACHMENTS_SUFFIX.length() + 1);

            AttachmentEntry attachmentEntry = new AttachmentEntry(docId, attachmentId, data);
            if (!findDocAndAddAttachment(attachmentEntry))
            {
                attachmentEntries.add(attachmentEntry);
            }
        }

        private boolean findDocAndAddAttachment(AttachmentEntry attachmentEntry)
        {
            BaseDocument doc = documentsWithAttachments.get(attachmentEntry.getDocumentId());
            if (doc != null)
            {
                Attachment attachment = doc.getAttachments().get(attachmentEntry.getAttachmentId());
                if (attachment != null)
                {
                    attachment.setStub(false);
                    String encodedData = Base64Util.encodeBase64(attachmentEntry.getData());

                    Assert.notNull(encodedData, "encodedData shouldn't be null");

                    attachment.setData(encodedData);
                }
                return true;
            }
            else
            {
                return false;
            }
        }

    }

    private static class AttachmentEntry
    {
        private byte[] data;
        private String documentId;
        private String attachmentId;
        public AttachmentEntry(String docId, String attachmentId, byte[] data)
        {
            Assert.notNull(docId, "docId can't be null");
            Assert.notNull(attachmentId, "attachmentId can't be null");
            Assert.notNull(data, "data can't be null");

            this.documentId = docId;
            this.attachmentId = attachmentId;
            this.data = data;
        }

        public String getDocumentId()
        {
            return documentId;
        }

        public String getAttachmentId()
        {
            return attachmentId;
        }

        public byte[] getData()
        {
            return data;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (obj instanceof AttachmentEntry)
            {
                AttachmentEntry that = (AttachmentEntry)obj;
                return this.documentId.equals(that.documentId) && this.attachmentId.equals(that.attachmentId);
            }
            return false;
        }

        @Override
        public int hashCode()
        {
            return 17 + 37 * documentId.hashCode() + 37 * attachmentId.hashCode();
        }

        @Override
        public String toString()
        {
            return super.toString()+": documentId = "+documentId+", attachmentId = "+attachmentId;
        }
    }
}
