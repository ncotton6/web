package edu.rit.csci729.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(value = { ElementType.PARAMETER })
public @interface WebServiceParam {

	String[] names();

}
