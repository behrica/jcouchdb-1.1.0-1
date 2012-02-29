package org.jcouchdb.document.test;

import java.util.ArrayList;
import java.util.List;

import org.svenson.JSONReference;

public class Post
    extends TestBase
{
    private User owner;

    private List<Comment> comments = new ArrayList<Comment>();

    private String text;

    @JSONReference
    public User getOwner()
    {
        return owner;
    }

    public void setOwner(User owner)
    {
        this.owner = owner;
    }

    @JSONReference
    public List<Comment> getComments()
    {
        return comments;
    }

    public void setComments(List<Comment> comments)
    {
        this.comments = comments;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

}
