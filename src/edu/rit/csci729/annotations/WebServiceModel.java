package edu.rit.csci729.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * NOT USED
 * 
 * But the intention was for it to mark classes that would be scanned at
 * runtime.
 * 
 * @author Nathaniel Cotton
 *
 */
@Target(value = { ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface WebServiceModel {

}
