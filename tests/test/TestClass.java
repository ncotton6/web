package test;

import edu.rit.csci729.annotations.WebServiceField;

public class TestClass {

	@WebServiceField(names={"city","town"})
	private String municipality;
	@WebServiceField
	private String address;
	
}
