package edu.rit.csci729.model;

import java.util.HashMap;
import java.util.Map;

public class TypeMapping {

	private Map<String,Map<String,String>> typeMap = null;
	private static TypeMapping mapping = null;
	
	private TypeMapping(){
		this.typeMap = new HashMap<String,Map<String,String>>();
	}
	
	private static TypeMapping get(){
		if(mapping == null){
			synchronized (TypeMapping.class) {
				if(mapping == null){
					TypeMapping.mapping = new TypeMapping();
				}
			}
		}
		return TypeMapping.mapping;
	}
	
	public void addMapping(String type, Map<String,String> structure){
		this.typeMap.put(type, structure);
	}
	
	public Map<String,String> getMapping(String type){
		return this.typeMap.get(type);
	}
	
}
