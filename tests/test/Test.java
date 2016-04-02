package test;

import edu.rit.csci729.util.Distance;

import java.util.List;

import edu.rit.csci729.*;
import edu.rit.csci729.model.Tuple;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Start");
		System.out.println(Distance.NGramSim("weather", "Weather", 2));
		
		ClassData cd = ClassMap.get().scanClass(TestClass.class);
		List<Tuple<Object,String[]>> d = cd.getInfo();
		printClassData(d);
		
		
		System.out.println("Done");
	}
	
	public static void printClassData(List<Tuple<Object,String[]>> d){
		for(Tuple<Object, String[]> tup : d){
			System.out.println(tup.v1);
			System.out.print("\t");
			for(String s : tup.v2){
				System.out.print(s + ", ");
			}
			System.out.println();
		}
	}

}
