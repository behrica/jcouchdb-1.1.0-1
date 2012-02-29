package org.jcouchdb.document;

public interface ChangeListener
{
    void onChange(ChangeNotification changeNotification);
}
