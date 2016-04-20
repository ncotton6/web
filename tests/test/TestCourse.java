package test;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.List;

import edu.rit.csci729.Engine;
import edu.rit.csci729.model.*;
import edu.rit.csci729.util.Distance;

public class TestCourse {

	private static String courseService = "course";
	
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
		Operation oper = new Operation();
		oper.setServiceName(courseService);
		oper.setOutput(outputs);
		
		
		Operation oper2 = new Operation();
		HashMap<String,String> inputs = new HashMap<String,String>();
		TypeMapping.get().addService("test", new HashMap<String,Map<String,String>>());
		inputs.put("lesson", "string");
		oper2.setInput(inputs);
		oper2.setServiceName("test");
		Engine e = new Engine(courseService,"test");
		List<FieldConnection> connections = e.generateMapping(oper, oper2, 0.9d, false);
		
		for(FieldConnection fc : connections){
			System.out.println(fc.fromConnection + " : " + fc.fromConnectionName + " : " + fc.toConnectionName + " : " + fc.toConnection + " : " + fc.qualityOfConnection);
		}
		System.out.println();
		
		Map<MappingSource, String> map = oper.getOutputMap();
		for(Entry<MappingSource, String> ent : map.entrySet()){
			System.out.println(ent.getKey().source + " - " + ent.getKey().type + " :: " + ent.getValue());
		}
		
		Operation oper3 = new Operation();
		HashMap<String,String> inputs1 = new HashMap<String,String>();
		TypeMapping.get().addService("test", new HashMap<String,Map<String,String>>());
		inputs1.put("way", "string");
		oper3.setInput(inputs);
		oper3.setServiceName("test");
		
		Engine e1 = new Engine(courseService,"test");
		List<FieldConnection> connections1 = e1.generateMapping(oper, oper3, 0.9d, false);
		
		for(FieldConnection fc : connections1){
			System.out.println(fc.fromConnection + " : " + fc.fromConnectionName + " : " + fc.toConnectionName + " : " + fc.toConnection + " : " + fc.qualityOfConnection);
		}
		System.out.println();
		
		Map<MappingSource, String> map1 = oper.getOutputMap();
		for(Entry<MappingSource, String> ent : map1.entrySet()){
			System.out.println(ent.getKey().source + " - " + ent.getKey().type + " :: " + ent.getValue());
		}
		
		
		
		
		System.out.println("End");
	}
	
}