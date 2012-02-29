package org.jcouchdb.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipInputStream;

import org.jcouchdb.db.LocalDatabaseTestCase;
import org.jcouchdb.db.Server;
import org.jcouchdb.db.ServerImpl;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;


public class CouchDBDumperTestCase
{
    private static File file;
    private static Server server;

    @BeforeClass
    public static void init() throws IOException
    {
        file = File.createTempFile("jcouchdb_dump", ".zip");
        server = LocalDatabaseTestCase.createDatabaseForTest().getServer();

        if (server.listDatabases().contains("ffwde_copy"))
        {
            server.deleteDatabase("ffwde_copy");
        }
    }

    @Test
    @Ignore
    public void test() throws IOException
    {
        CouchDBDumper dumper = new CouchDBDumper();
        dumper.dumpDatabase(server, "test", new FileOutputStream("test/org/jcouchdb/document/test-files/result-assembler-db.zip"), true);
    }

    @Test
    @Ignore
    public void testLoad() throws FileNotFoundException, IOException
    {
        new CouchDBLoader().load(new ZipInputStream(new FileInputStream(file)), server, "ffwde_copy");
    }

}
