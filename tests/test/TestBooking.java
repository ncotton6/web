package test;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.List;

import edu.rit.csci729.Engine;
import edu.rit.csci729.model.*;

public class TestBooking {
	

	private static String bookingService = "booking";
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
		HashMap<String,String> customer = new HashMap<String,String>();
		address.put("name", "string");
		address.put("age", "int");
		address.put("address", "address");
		TypeMapping.get().addService(bookingService, new HashMap<String,Map<String,String>>());
		TypeMapping.get().getService(bookingService).put("address", address);
		HashMap<String,String> outputs = new HashMap<String,String>();
		outputs.put("customer", "customer");
		outputs.put("quantity", "string");
		outputs.put("date", "date");
		/*
		 * synonym or very similar in meaning
		 * 0.82
		 */
		testCase(outputs, "volume", "int", bookingService);
		/*
		 * near similar
		 * 0.8
		 */
		testCase(outputs,"quality","string",bookingService);
		/*
		 * arbitrary
		 * 0.5
		 */
		testCase(outputs, "noodles", "string", bookingService);
		/*
		 * arbitrary not present
		 * 0.1
		 */
testCase(outputs, "2.3", "double", bookingService);
		
		
		
		System.out.println("End");
	}
	
}