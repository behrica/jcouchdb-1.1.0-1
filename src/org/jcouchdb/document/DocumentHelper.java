package org.jcouchdb.document;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.beanutils.PropertyUtils;
import org.jcouchdb.util.ExceptionWrapper;
import org.svenson.JSONProperty;

/**
 * Helper class that reflectively gets and sets the "_id" and "_rev" properties of documents,
 * obeying {@link JSONProperty} annotations.
 *
 * @author fforw at gmx dot de
 *
 */
public class DocumentHelper implements DocumentPropertyHandler
{
    public String getId(Object document)
    {
        try
        {
            if (document instanceof Document)
            {
                return ((Document) document).getId();
            }
            String name = getPropertyNameFromAnnotation(document, "_id");
            return (String) PropertyUtils.getProperty(document, name);
        }
        catch (IllegalAccessException e)
        {
            throw ExceptionWrapper.wrap(e);
        }
        catch (InvocationTargetException e)
        {
            throw ExceptionWrapper.wrap(e);
        }
        catch (NoSuchMethodException e)
        {
            throw ExceptionWrapper.wrap(e);
        }
    }

    public String getRevision(Object document)
    {
        try
        {
            if (document instanceof Document)
            {
                return ((Document) document).getRevision();
            }
            String name = getPropertyNameFromAnnotation(document, "_rev");
            return (String) PropertyUtils.getProperty(document, name);
        }
        catch (IllegalAccessException e)
        {
            throw ExceptionWrapper.wrap(e);
        }
        catch (InvocationTargetException e)
        {
            throw ExceptionWrapper.wrap(e);
        }
        catch (NoSuchMethodException e)
        {
            throw ExceptionWrapper.wrap(e);
        }
    }

    public void setId(Object document, String id)
    {
        try
        {
            if (document instanceof Document)
            {
                ((Document) document).setId(id);
            }
            else
            {
                String name = getPropertyNameFromAnnotation(document, "_id");
                PropertyUtils.setProperty(document, name, id);
            }
        }
        catch (IllegalAccessException e)
        {
            throw ExceptionWrapper.wrap(e);
        }
        catch (InvocationTargetException e)
        {
            throw ExceptionWrapper.wrap(e);
        }
        catch (NoSuchMethodException e)
        {
            throw ExceptionWrapper.wrap(e);
        }
    }

    public void setRevision(Object document, String revision)
    {
        try
        {
            if (document instanceof Document)
            {
                ((Document) document).setRevision(revision);
            }
            else
            {
                String name = getPropertyNameFromAnnotation(document, "_rev");
                PropertyUtils.setProperty(document, name, revision);
            }
        }
        catch (IllegalAccessException e)
        {
            throw ExceptionWrapper.wrap(e);
        }
        catch (InvocationTargetException e)
        {
            throw ExceptionWrapper.wrap(e);
        }
        catch (NoSuchMethodException e)
        {
            throw ExceptionWrapper.wrap(e);
        }
    }

    private String getPropertyNameFromAnnotation(Object target, String value)
    {
        for (PropertyDescriptor pd : PropertyUtils.getPropertyDescriptors(target.getClass()))
        {
            JSONProperty jsonProperty = null;
            Method readMethod = pd.getReadMethod();
            Method writeMethod = pd.getWriteMethod();

            if (readMethod != null)
            {
                jsonProperty = readMethod.getAnnotation(JSONProperty.class);
            }
            if (jsonProperty == null && writeMethod != null)
            {
                jsonProperty = writeMethod.getAnnotation(JSONProperty.class);
            }

            if (jsonProperty != null && jsonProperty.value().equals(value))
            {
                return pd.getName();
            }
        }
        return value;
    }
}
