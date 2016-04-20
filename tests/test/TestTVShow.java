package test;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.List;

import edu.rit.csci729.Engine;
import edu.rit.csci729.model.*;

public class TestTVShow {

	private static String TVService = "show";
	
	public static void main(String[] args) throws NoMappingFound{
		System.out.println("Start");
		// Create hotel operations
		HashMap<String,String> description = new HashMap<String,String>();
		description.put("leadName", "string");
		description.put("summary", "string");

		TypeMapping.get().addService(TVService, new HashMap<String,Map<String,String>>());
		TypeMapping.get().getService(TVService).put("description", description);
		HashMap<String,String> outputs = new HashMap<String,String>();
		outputs.put("title", "string");
		outputs.put("description", "description");
		outputs.put("airDate", "date");
		Operation oper = new Operation();
		oper.setServiceName(TVService);
		oper.setOutput(outputs);
		
		
		Operation oper2 = new Operation();
		HashMap<String,String> inputs = new HashMap<String,String>();
		TypeMapping.get().addService("test", new HashMap<String,Map<String,String>>());
		inputs.put("headline", "string");
		oper2.setInput(inputs);
		oper2.setServiceName("test");
		
		
		Engine e = new Engine(TVService,"test");
		List<FieldConnection> connections = e.generateMapping(oper, oper2, 0.5d, false);
		
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