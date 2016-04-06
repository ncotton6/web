package edu.rit.csci729.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Marks the parameters to a method, as processable information.
 * 
 * @author Nathaniel Cotton
 *
 */
@Target(value = { ElementType.PARAMETER })
public @interface WebServiceParam {

	String[] names();

}
