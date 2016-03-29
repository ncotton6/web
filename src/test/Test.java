package test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.rit.csci729.ClassMap;
import edu.rit.csci729.Engine;
import edu.rit.csci729.model.NoMappingFound;
import edu.rit.csci729.model.Operation;
import edu.rit.csci729.model.Tuple;

public class Test {

	public static void main(String[] args) {
		System.setProperty("wordnet.database.dir", "C:\\WordNet\\2.1\\dict\\");
		Map<String, String> input = new HashMap<String, String>();
		Map<String, String> output = new HashMap<String, String>();

		input.put("city", "xs:string");
		input.put("address", "xs:string");

		Operation oper = new Operation(input, output);

		ClassMap.get().scanClass(TestClass.class);

		String[] names = ClassMap.get().getClassData(TestClass.class).getInfo();
		System.out.println(Arrays.toString(names));

		List<Tuple<String, String>> mapping = null;
		try {
			mapping = Engine.generateMapping(TestClass.class, oper);
		} catch (NoMappingFound e) {
			e.printStackTrace();
		}

		printMapping(mapping);

		System.out.println("Done");
	}

	private static void printMapping(List<Tuple<String, String>> mapping) {
		if (mapping == null) {
			System.out.println("Mapping was NULL");
		} else {
			for (Tuple<String, String> tup : mapping) {
				System.out.println(tup.v1+" ====> "+tup.v2);
			}
		}

	}

}
