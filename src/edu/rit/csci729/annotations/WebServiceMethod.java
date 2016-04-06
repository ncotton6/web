package edu.rit.csci729.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Adds a Method to be processed by the engine.
 * 
 * @author Nathaniel Cotton
 *
 */
@Target(value = { ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface WebServiceMethod {

	String[] names();

}
