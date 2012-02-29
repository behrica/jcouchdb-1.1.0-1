/**
 * 
 */
package org.jcouchdb.db;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.io.IOUtils;
import org.jcouchdb.document.ChangeListener;
import org.jcouchdb.document.ChangeNotification;
import org.jcouchdb.document.HeartBeatListener;
import org.jcouchdb.util.ExceptionWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.svenson.JSONParser;

class ContinuousChangesDriver
    extends Thread
{
    private final static AtomicInteger driverCount = new AtomicInteger();

    private static Logger log = LoggerFactory.getLogger(ContinuousChangesDriver.class);

    private String filter;

    private Long since;

    private Options options;

    private ChangeListener listener;

    private Database db;


    public ContinuousChangesDriver(Database db, String filter, Long since, Options options,
        ChangeListener listener)
    {
        super("ContinuousChangesDriver-" + driverCount.incrementAndGet());

        this.db = db;
        this.filter = filter;
        this.since = since;
        this.options = options;
        this.listener = listener;
    }


    @Override
    public void run()
    {
        synchronized(this)
        {
            this.notifyAll();
        }

        log.info("ContinuousChangesDriver started.");
        
        Response response = null;

        options = db.getCommonChangesOptions(filter, since, options);
        options.putUnencoded("feed", "continuous");

        InputStream ir = null;

        while (!db.getServer().isShutdown())
        {
            log.debug("driver loop");
            try
            {
                log.info(
                    "Sending continuous change request. filter = {}, since = {}, options = {}",
                    new Object[] { filter, since, options });
                response = db.getServer().get("/" + db.getName() + "/_changes" + options.toQuery());

                ir = response.getInputStream();
                log.debug("input stream = {}", ir);

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                
                while (!db.getServer().isShutdown())
                {
                    int c = ir.read();
                    if (c == -1)
                    {
                        throw new IllegalStateException("Unexpected EOF");
                    }

                    if (c == '\n')
                    {
                        convertRawData(bos.toByteArray());
                        bos.reset();
                    }
                    else
                    {
                        bos.write(c);
                    }
                }
            }
            catch(SocketException e)
            {
                log.debug("SocketException listening to continuous changes");
            }
            catch (Exception e)
            {
                log.info("Error listening to continuous changes", e);
            }
            finally
            {
                if (ir != null)
                {
                    IOUtils.closeQuietly(ir);
                    ir = null;
                }

                if (response != null)
                {
                    response.destroy();
                }
            }
        }
    }


    private void convertRawData(byte[] byteArray)
    {
        if (byteArray.length > 0)
        {
            try
            {
                String json = new String(byteArray, "UTF-8");
                log.debug("received notification JSON = {}", json);
                ChangeNotification changeNotification = JSONParser.defaultJSONParser().parse(ChangeNotification.class, json);
                listener.onChange(changeNotification);
            }
            catch (UnsupportedEncodingException e)
            {
                throw ExceptionWrapper.wrap(e);
            }
        }
        else
        {
            log.debug("received heartbeat");
            if (listener instanceof HeartBeatListener)
            {
                ((HeartBeatListener)listener).heartbeat();
            }
        }
    }
}
