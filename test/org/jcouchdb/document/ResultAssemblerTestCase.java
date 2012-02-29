package org.jcouchdb.document;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.startsWith;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import org.jcouchdb.db.Database;
import org.jcouchdb.db.LocalDatabaseTestCase;
import org.jcouchdb.document.test.Post;
import org.jcouchdb.document.test.TestBase;
import org.jcouchdb.util.CouchDBLoader;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.svenson.ClassNameBasedTypeMapper;
import org.svenson.JSON;
import org.svenson.JSONConfig;
import org.svenson.JSONParser;
import org.svenson.matcher.SubtypeMatcher;


public class ResultAssemblerTestCase
{
    private static Logger log = LoggerFactory.getLogger(ResultAssemblerTestCase.class);
    
    private static Database db;

    @Test
    public void init() throws FileNotFoundException, IOException
    {
        db = LocalDatabaseTestCase.recreateDB("jcouchdb-result-assembler");
        db.setJsonConfig(createConfig());
        new CouchDBLoader().load(new ZipInputStream(new FileInputStream("test/org/jcouchdb/document/test-files/result-assembler-db.zip")), db.getServer(), db.getName());
    }

    private static JSONConfig createConfig()
    {
        ClassNameBasedTypeMapper typeMapper = new ClassNameBasedTypeMapper();
        typeMapper.setBasePackage(TestBase.class.getPackage().getName());
        typeMapper.setEnforcedBaseType(TestBase.class);
        typeMapper.setDiscriminatorField("type");
        typeMapper.setPathMatcher(new SubtypeMatcher(TestBase.class));
        
        JSONParser parser = new JSONParser();
        parser.setTypeMapper(typeMapper);

        JSON json = new JSON();

        return new JSONConfig(json, parser);
    }
    
    @Test
    public void test()
    {
        ViewAndDocumentsResult<Map, TestBase> result = db.queryViewAndDocuments("posts/list", Map.class, TestBase.class, null, null);
        log.info("{}", result);
        
        ResultAssembler assembler = new ResultAssembler();
        List<TestBase> docs = assembler.assemble(result);
        
        System.out.println(docs);
        
        assertThat(docs.size(), is(2));
        
        Post post = (Post)docs.get(0);
        
        assertThat(post.getText(), is("This is a post text."));

        assertThat(post.getOwner(), is(notNullValue()));
        assertThat(post.getComments().size(), is(2));

        assertThat(post.getComments().get(0).getId(), is(notNullValue()));
        assertThat(post.getComments().get(0).getText(), is(notNullValue()));
        assertThat(post.getComments().get(1).getId(), is(notNullValue()));
        assertThat(post.getComments().get(1).getText(), is(notNullValue()));
        
        
    }
}
