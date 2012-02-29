package org.jcouchdb;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.util.Date;

import org.jcouchdb.db.Database;
import org.jcouchdb.db.LocalDatabaseTestCase;
import org.jcouchdb.document.BaseDocument;
import org.junit.Test;
import org.svenson.JSON;
import org.svenson.JSONParser;
import org.svenson.converter.DateConverter;
import org.svenson.converter.DefaultTypeConverterRepository;
import org.svenson.converter.JSONConverter;
import org.svenson.converter.TypeConverterRepository;

public class CouchDateTest
{
    @Test
    public void test()
    {
        Database db = LocalDatabaseTestCase.createDatabaseForTest();
        
        long now = System.currentTimeMillis();
        
        Date d = new Date(now);
        
        // cut off millis
        now = (now / 1000 ) * 1000;
        
        BeanWithDate doc = new BeanWithDate();
        doc.setDate(d);
        
        DefaultTypeConverterRepository typeConverterRepository = new DefaultTypeConverterRepository();
        typeConverterRepository.addTypeConverter(new DateConverter());
        JSON jsonGenerator = new JSON();
        jsonGenerator.setTypeConverterRepository(typeConverterRepository);
        db.setJsonGenerator(jsonGenerator );
        db.createDocument(doc);
        JSONParser parser = new JSONParser();
        parser.setTypeConverterRepository(typeConverterRepository);
        BeanWithDate doc2 = db.getDocument(BeanWithDate.class, doc.getId(), null, parser );
        
        assertThat(doc2, is(notNullValue()));
        assertThat(doc2.getDate(), is(notNullValue()));
        assertThat(doc2.getDate().getTime(), is(now));
        
    }

    public static class BeanWithDate extends BaseDocument
    {
        private Date date;
        
        public Date getDate()
        {
            return date;
        }
        
        @JSONConverter(type = DateConverter.class)
        public void setDate(Date date)
        {
            this.date = date;
        }
    }
}
