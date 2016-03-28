package edu.rit.csci729;

import java.util.ArrayList;
import java.util.List;

import edu.rit.csci729.model.NoMappingFound;
import edu.rit.csci729.model.Operation;
import edu.rit.csci729.model.Tuple;

public class Engine {

	public List<Tuple> generateMapping(Class<?> c, Operation oper) throws NoMappingFound{
		ClassMap cMap = ClassMap.get();
		cMap.scanClass(c);
		ClassData cd = cMap.getClassData(c);		
		return generateMapping(cd,oper);
	}
	
	public List<Tuple> generateMapping(ClassData cd, Operation oper) throws NoMappingFound{
		ArrayList<Tuple> mappings = new ArrayList<Tuple>();
		return mappings;
	}
	
}
