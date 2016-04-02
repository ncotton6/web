package test;

import edu.rit.csci729.annotations.WebServiceField;

public class TestClass {

	@WebServiceField(names={"town"})
	private String municipality;
	@WebServiceField
	private String address;
	
}
