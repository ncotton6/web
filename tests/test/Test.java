package test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.rit.csci729.*;
import edu.rit.csci729.model.*;
import edu.rit.csci729.util.Distance;

public class Test {

	public static void main(String[] args) throws NoMappingFound {
		
		System.out.println(Distance.NGramSim2("zip_code", "zip code"));
		System.out.println(Distance.NLevenshteinSim("zip_code", "zip code"));
		
		
		System.out.println("Start");
		ClassData cd = ClassMap.get().scanClass(TestClass.class);
		Map<String, String> input = new HashMap<String, String>();
		input.put("fname", "string");
		input.put("lname", "string");
		Map<String, String> output = new HashMap<String, String>();
		Operation oper = new Operation(input, output);
		Engine e = new Engine(null, null);
		printFieldConnection(e.generateMapping(TestClass.class, oper,0.25d,true));
		input.put("age", "string");
		System.out.println("\n");
		printFieldConnection(e.generateMapping(TestClass.class, oper,0.25d,true));
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
					fc.fromConnection + " ==>(" + fc.fromConnectionName + " ==> " + fc.toConnectionName
							+ ")==> " + fc.toConnection + " (similarity " + fc.qualityOfConnection + ")");
		}
	}

}
