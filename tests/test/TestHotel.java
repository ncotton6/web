package test;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.List;

import edu.rit.csci729.Engine;
import edu.rit.csci729.model.*;

public class TestHotel {

	private static String hotelService = "hotel";
	
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
		Operation oper = new Operation();
		oper.setServiceName(hotelService);
		oper.setOutput(outputs);
		
		
		Operation oper2 = new Operation();
		HashMap<String,String> inputs = new HashMap<String,String>();
		TypeMapping.get().addService("test", new HashMap<String,Map<String,String>>());
		inputs.put("resort", "string");
		oper2.setInput(inputs);
		oper2.setServiceName("test");
		
		Engine e = new Engine(hotelService,"test");
		List<FieldConnection> connections = e.generateMapping(oper, oper2, 0.9d, false);
		
		for(FieldConnection fc : connections){
			System.out.println(fc.fromConnection + " : " + fc.fromConnectionName + " : " + fc.toConnectionName + " : " + fc.toConnection + " : " + fc.qualityOfConnection);
		}
		System.out.println();
		
		Map<MappingSource, String> map = oper.getOutputMap();
		for(Entry<MappingSource, String> ent : map.entrySet()){
			System.out.println(ent.getKey().source + " - " + ent.getKey().type + " :: " + ent.getValue());
		}
		
		
		
		
		System.out.println("End");
	}
	
}
