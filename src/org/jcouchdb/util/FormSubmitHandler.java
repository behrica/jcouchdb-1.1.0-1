package org.jcouchdb.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.svenson.JSON;

/**
 * A simple HTTP form submit to couchdb bridge 
 * 
 * @author fforw at gmx dot de
 *
 */
public class FormSubmitHandler
{
    private static Logger log = LoggerFactory.getLogger(FormSubmitHandler.class);
    
    public static void main(String[] args)
    {
        BufferedReader br = null;
        BufferedWriter bw = null;
        try
        {
            br = new BufferedReader(new InputStreamReader(System.in));
            bw = new BufferedWriter(new OutputStreamWriter(System.out));
            
            String line;
            
            log.debug("Starting FormSubmitHandler");
            
            while ((line = br.readLine()) != null)
            {
                log.debug("IN: {}", JSON.formatJSON(line));
                bw.write("{\"code\":200}\n");
                bw.flush();
            }
            
            log.info("Ending FormSubmitHandler");
        }
        catch(Exception e)
        {
            log.error("Error in FormSubmitHandler, aborting", e);
        }
        finally
        {
            if (br != null)
            {
                IOUtils.closeQuietly(br);
            }
            if (bw != null)
            {
                IOUtils.closeQuietly(bw);
            }
        }
    }
}
