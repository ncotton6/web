package edu.rit.csci729.model;

/**
 * There is a chance that there is not sufficient connection between two
 * components, at that time a {@link NoMappingFound} exception will be thrown.
 * 
 * @author Nathaniel Cotton
 *
 */
public class NoMappingFound extends Exception {

	public NoMappingFound(String string) {
		super(string);
	}

}
