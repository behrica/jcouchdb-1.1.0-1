package org.jcouchdb.document;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a class as aggressively instance-cacheable meaning that it's ok for
 * caches to return the same instance for a cached couchdb URI.
 *
 * @author fforw at gmx dot de
 */

@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Target(value = ElementType.TYPE)
public @interface InstanceCachable
{

}
