package org.jcouchdb.db;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.util.Date;

import org.jcouchdb.CouchDateTest.BeanWithDate;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.svenson.JSONConfig;
import org.svenson.JSONParser;
import org.svenson.converter.DateConverter;

public class TypeConverterPropagationTestCase
{
    private static Logger log = LoggerFactory.getLogger(TypeConverterPropagationTestCase.class);
    
    @Test
    public void test()
    {
        // 1. Create the Database object
        Database couchdb = LocalDatabaseTestCase.createDatabaseForTest();
        // 2. Get the configuration from my Database 
        JSONConfig jsonConfig = couchdb.getJsonConfig();
        // 3. get the parser
        JSONParser jsonParser = jsonConfig.getJsonParser();
        // 4. create a new parser
        jsonParser = new JSONParser();
        // 5. and register the converter
        jsonParser.registerTypeConversion(java.util.Date.class, new DateConverter());
        jsonConfig.getJsonGenerator().registerTypeConversion(java.util.Date.class, new DateConverter());
        // 6. finally, submit the new configuration to the Database
        couchdb.setJsonConfig(new JSONConfig(jsonConfig.getJsonGenerator(), jsonParser));

        BeanWithDate bean = new BeanWithDate();
        bean.setDate(new Date(0));
     
        couchdb.createDocument(bean);
        
        assertThat(bean.getId(),is(notNullValue()));
        assertThat(bean.getRevision(),is(notNullValue()));
        
        BeanWithDate bean2 = couchdb.getDocument(BeanWithDate.class, bean.getId());
        
        assertThat(bean2.getDate(),is(notNullValue()));
        log.info("{}", bean2.getDate());
        
    }

}
