package test;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.List;

import edu.rit.csci729.Engine;
import edu.rit.csci729.model.*;

public class TestEmployee {

	private static String employeeService = "employee";
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

		TypeMapping.get().addService(employeeService, new HashMap<String,Map<String,String>>());

		HashMap<String,String> outputs = new HashMap<String,String>();
		outputs.put("employeeName", "string");
		outputs.put("employeeId", "double");
		
	/*
	 * Synonym
	 * 0.733
	 */
		testCase(outputs, "worker", "string", employeeService);
		/*
		 * Near similar
		 * but with a type not present
		 * 0.23333
		 */
		testCase(outputs, "salary", "int", employeeService);
		
		/*
		 * arbitrary type present
		 * 0.333
		 */
		testCase(outputs, "Broadway", "string", employeeService);
		
		/*
		 * arbitrary type not present
		 * 0.2
		 */
		testCase(outputs, "0.2", "double", employeeService);
		
	
		
		
		
		System.out.println("End");
	}
	
}