package test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.rit.csci729.*;
import edu.rit.csci729.model.*;

public class Test {

	public static void main(String[] args) throws NoMappingFound {
		// TODO Auto-generated method stub
		System.out.println("Start");

		ClassData cd = ClassMap.get().scanClass(TestClass.class);
		Map<String, String> input = new HashMap<String, String>();
		input.put("fname", "string");
		input.put("lname", "string");
		Map<String, String> output = new HashMap<String, String>();
		Operation oper = new Operation(input, output);
		printFieldConnection(Engine.generateMapping(TestClass.class, oper,0.25d,true));
		input.put("age", "string");
		System.out.println("\n");
		printFieldConnection(Engine.generateMapping(TestClass.class, oper,0.25d,true));
		System.out.println("Done");
	}

	public static void printClassData(List<Tuple<Object, String[]>> d) {
		for (Tuple<Object, String[]> tup : d) {
			System.out.println(tup.v1);
			System.out.print("\t");
			for (String s : tup.v2) {
				System.out.print(s + ", ");
			}
			System.out.println();
		}
	}

	public static void printFieldConnection(List<FieldConnection> conn) {
		for (FieldConnection fc : conn) {
			System.out.println(
					fc.classConnection + " ==>(" + fc.classConnectionName + " ==> " + fc.webServiceConnectionName
							+ ")==> " + fc.webServiceName + " (similarity " + fc.qualityOfConnection + ")");
		}
	}

}
