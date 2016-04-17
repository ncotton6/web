package test;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import edu.rit.csci729.model.MappingSource;
import edu.rit.csci729.model.Operation;
import edu.rit.csci729.model.TypeMapping;

public class TestComplexTypes {

	public static void main(String[] args) {
		System.out.println("Start");
		// complex type
		Map<String, Map<String, String>> details = new HashMap<String, Map<String, String>>();
		Map<String, String> person = new HashMap<String, String>();
		person.put("name", "string");
		person.put("age", "int");
		details.put("person", person);
		TypeMapping.get().addService("test", details);
		//
		// Test person information
		for(Entry<String, String> ent : TypeMapping.get().getService("test").get("person").entrySet()){
			System.out.println(ent.getKey() + " - " + ent.getValue());
		}
		//
		Operation oper = new Operation();
		Map<String, String> input = new HashMap<String, String>();
		input.put("emp", "person");
		input.put("salary", "double");
		oper.setInput(input);
		oper.setServiceName("test");
		Map<MappingSource, String> map = oper.getInputMap();
		for (Entry<MappingSource, String> ent : map.entrySet()) {
			System.out.println(String.format("(%s,%s)-%s", ent.getKey().source, ent.getKey().type, ent.getValue()));
		}
	}

}
