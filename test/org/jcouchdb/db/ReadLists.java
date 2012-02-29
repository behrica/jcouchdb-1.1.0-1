package org.jcouchdb.db;

import java.util.Map;

import org.jcouchdb.document.DesignDocument;

public class ReadLists
{
    public static void main(String[] args)
    {
        Database db = new Database("localhost", "couchbbs");
        
        DesignDocument doc = db.getDesignDocument("couchbbs");
        System.out.println(doc.getListFunctions().get("index"));
    }

}
