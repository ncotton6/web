package test;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.List;

import edu.rit.csci729.Engine;
import edu.rit.csci729.model.*;

public class TestLibrary {

	private static String libraryService = "library";
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
		;
		HashMap<String,String> book=new HashMap<String,String>();
		book.put("Name","string");
		book.put("title", "string");
		book.put("summary", "string");
		
		
		
		TypeMapping.get().addService(libraryService, new HashMap<String,Map<String,String>>());
		TypeMapping.get().getService(libraryService).put("book", book);
		HashMap<String,String> outputs = new HashMap<String,String>();
		outputs.put("librarian", "string");
		outputs.put("books", "book");
	
		
	/*
	 * Synonym
	 */
		testCase(outputs, "label", "string", libraryService);
		
		/*
		 * Near similar
		 * 0.64
		 * 
		 */
		testCase(outputs, "author", "string", libraryService);
		
		/*
		 * arbitrary type present
		 * 0.533
		 */
		testCase(outputs, "food", "string", libraryService);
		
		/*
		 * arbitrary type not present
		 * 0.02
		 */
		testCase(outputs, "899w0w0w", "int", libraryService);
		
		

		
		
		
		
		System.out.println("End");
	}
	
}