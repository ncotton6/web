package edu.rit.csci729.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a Field to be processed by the engine
 * 
 * @author Nathaniel Cotton
 *
 */
@Target(value={ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface WebServiceField {

	String[] names() default {};
	
}
