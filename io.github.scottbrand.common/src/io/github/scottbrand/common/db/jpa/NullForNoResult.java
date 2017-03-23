package io.github.scottbrand.common.db.jpa;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * When using JPA query.getSingleResult()
 * and no result is available, the persistence API raises an
 * exception.
 * Using this annotation with the PersistenceManager, will
 * sallow the exception and return null instead.
 * 
 * @author Scott Brand
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface NullForNoResult
{

} 
