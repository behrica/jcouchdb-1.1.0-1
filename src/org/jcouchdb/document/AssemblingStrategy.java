package org.jcouchdb.document;

import java.util.Comparator;


public interface AssemblingStrategy extends Comparator<ValueAndDocumentRow>
{
    <D> void assemble(D doc,ValueAndDocumentRow<? extends Object, D> row) throws Exception;
}
