package test;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.List;

import edu.rit.csci729.Engine;
import edu.rit.csci729.model.*;

public class TestHotel {

	private static String hotelService = "hotel";
	public  static void testCase(HashMap outputs,String testString,String type,String serviceName) throws NoMappingFound
	{
		Operation oper = new Operation();
		oper.setServiceName(serviceName);
		oper.setOutput(outputs);
		
		/*
		 * testing for booking vs order
		 */
		Operation oper2 = new Operation();
		HashMap<String,String> inputs = new HashMap<String,String>();
		TypeMapping.get().addService("test", new HashMap<String,Map<String,String>>());
		inputs.put(testString, type);
		oper2.setInput(inputs);
		oper2.setServiceName("test");
		Engine e = new Engine(serviceName,"test");
		List<FieldConnection> connections = e.generateMapping(oper, oper2, 0.9d, false);
		
		for(FieldConnection fc : connections){
			System.out.println(fc.fromConnection + " : " + fc.fromConnectionName + " : " + fc.toConnectionName + " : " + fc.toConnection + " : " + fc.qualityOfConnection);
		}
		System.out.println();
		
		Map<MappingSource, String> map = oper.getOutputMap();
		for(Entry<MappingSource, String> ent : map.entrySet()){
			System.out.println(ent.getKey().source + " - " + ent.getKey().type + " :: " + ent.getValue());
		}
	}
	
	public static void main(String[] args) throws NoMappingFound{
		System.out.println("Start");
		// Create hotel operations
		HashMap<String,String> address = new HashMap<String,String>();
		address.put("street", "string");
		address.put("state", "string");
		address.put("zipcode", "string");
		TypeMapping.get().addService(hotelService, new HashMap<String,Map<String,String>>());
		TypeMapping.get().getService(hotelService).put("address", address);
		HashMap<String,String> outputs = new HashMap<String,String>();
		outputs.put("hotel", "string");
		outputs.put("rating", "double");
		outputs.put("location", "address");
	/*
	 * Synonym
	 */
	testCase(outputs, "resort", "string", hotelService);
	/*
	 * Near -similar
	 */
	testCase(outputs, "church", "string", hotelService);
			/*
			 * Arbitrary
			 */
	testCase(outputs, "novel", "string", hotelService);	
	/*
	 * Arbitrary without type matching
	 */
	testCase(outputs, "12/11/2012", "date", hotelService);
	
		
		System.out.println("End");
	}
	
}