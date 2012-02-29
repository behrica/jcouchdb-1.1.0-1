package org.jcouchdb.document;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jcouchdb.exception.CouchDBException;

/**
 * Merges the objects of a {@link ViewAndDocumentsResult} where they have a 
 * @author shelmberger
 *
 */
public class ResultAssembler
{
    private AssemblingStrategy strategy;

    public ResultAssembler()
    {
        this(new PropertyPathAssemblingStrategy());
    }
    public ResultAssembler(AssemblingStrategy strategy)
    {
        this.strategy = strategy;
    }
    
    private DocumentPropertyHandler documentPropertyHandler = new DocumentHelper();
    
    public void setDocumentPropertyHandler(DocumentPropertyHandler documentPropertyHandler)
    {
        this.documentPropertyHandler = documentPropertyHandler;
    }
    
    public <D> List<D> assemble(ViewAndDocumentsResult<? extends Object, D> result)
    {
        try
        {
            Map<String, D> id2main = findMainObjects(result);
            
            for (ValueAndDocumentRow<? extends Object, D> row : result.getRows())
            {
                D main = id2main.get(row.getId());
                D rowDoc = row.getDocument();
                if (rowDoc != main)
                {                
                    strategy.assemble(main, row);
                }
            }

            return new ArrayList<D>( id2main.values());
        }
        catch (Exception e)
        {
            throw new CouchDBException(e);
        }
    }
    
    private <D> Map<String, D> findMainObjects(ViewAndDocumentsResult<? extends Object, D> result)
    {
        Map<String, D> map = new TreeMap<String, D>();
        
        for (ValueAndDocumentRow<? extends Object, D> row : result.getRows())
        {
            D document = row.getDocument();
            String id = documentPropertyHandler.getId(document);
            if (row.getId().equals( id))
            {
                map.put(id, row.getDocument());
            }
        }
        
        return map ;
    }
    
}
