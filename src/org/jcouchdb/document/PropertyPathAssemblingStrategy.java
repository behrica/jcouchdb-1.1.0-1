package org.jcouchdb.document;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.svenson.JSONParseException;
import org.svenson.JSONParser;
import org.svenson.JSONProperty;
import org.svenson.tokenize.Token;
import org.svenson.tokenize.TokenType;
import org.svenson.util.JSONBeanUtil;
import org.svenson.util.JSONPathUtil;

public class PropertyPathAssemblingStrategy
    implements AssemblingStrategy
{
    private static Logger log = LoggerFactory.getLogger(PropertyPathAssemblingStrategy.class);
    
    private JSONPathUtil pathUtil = new JSONPathUtil();
    
    public void setPathUtil(JSONPathUtil pathUtil)
    {
        this.pathUtil = pathUtil;
    }
    
    public <D> void assemble(D doc, ValueAndDocumentRow<? extends Object, D> row) throws Exception
    {
        D child = row.getDocument();
        try
        {
            String path = getPathFromRow(row);
            pathUtil.setPropertyPath(doc, path, child);
        }
        catch(ClassCastException e)
        {
            log.error("value is {}", row.getValue());
            throw e;
        }
    }

    private <D> String getPathFromRow(ValueAndDocumentRow<? extends Object, D> row)
    {
        Map<String, String> map = (Map<String,String>)row.getValue();
        String path = map.get("path");
        return path;
    }
    
    public int compare(ValueAndDocumentRow o1, ValueAndDocumentRow o2)
    {
        return pathLength(getPathFromRow(o1)) - pathLength(getPathFromRow(o2));
    }

    private int pathLength(String pathFromRow)
    {
        // TODO Auto-generated method stub
        return 0;
    }
}
