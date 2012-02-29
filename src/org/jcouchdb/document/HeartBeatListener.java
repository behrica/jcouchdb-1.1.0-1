package org.jcouchdb.document;

/**
 * Extension of the {@link ChangeListener} interface that can also react on heart beat events.
 * 
 * @author fforw at gmx dot de
 *
 */
public interface HeartBeatListener extends ChangeListener
{
    void heartbeat();
}
