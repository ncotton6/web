package test;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.List;

import edu.rit.csci729.Engine;
import edu.rit.csci729.model.*;

public class TestCourse {

	private static String courseService = "course";
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
	
		HashMap<String,String> professor = new HashMap<String,String>();
		professor.put("name", "string");
		professor.put("gradeLevel", "string");
		professor.put("department", "string");
		HashMap<String,String> student = new HashMap<String,String>();
		student.put("name", "string");
		student.put("grade", "string");

		TypeMapping.get().addService(courseService, new HashMap<String,Map<String,String>>());
		TypeMapping.get().getService(courseService).put("professor", professor);
		TypeMapping.get().getService(courseService).put("student",student);
		HashMap<String,String> outputs = new HashMap<String,String>();
		outputs.put("professor", "professor");
		outputs.put("student", "student");
		outputs.put("classTime", "date");
		/*
		 * Synonym testing
		 * department-sectot
		 * Result-1.0
		 */
		testCase(outputs, "sector", "string", courseService);
		/*
		 * Near similar
		 * 0.6799
		 */
		testCase(outputs, "leader", "string", courseService);
		/*
		 * arbitrary string
		 * 0.2
		 */
		testCase(outputs, "hsjsks", "string", courseService);
		
		
		
		System.out.println("End");
	}
	
}