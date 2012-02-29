package org.jcouchdb.document;

public class ChangeEntry
{
    private String rev;

    public String getRev()
    {
        return rev;
    }

    public void setRev(String rev)
    {
        this.rev = rev;
    }

    @Override
    public String toString()
    {
        return super.toString() + "[rev=" + rev + "]";
    }
}
