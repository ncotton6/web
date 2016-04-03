package edu.rit.csci729.model;

import java.util.HashMap;
import java.util.Map;

public class TypeMapping {

	private Map<String,Map<String,Map<String,String>>> typeMap = null;
	private static TypeMapping mapping = null;
	
	private TypeMapping(){
		this.typeMap = new HashMap<String,Map<String,Map<String,String>>>();
	}
	
	public static TypeMapping get(){
		if(mapping == null){
			synchronized (TypeMapping.class) {
				if(mapping == null){
					TypeMapping.mapping = new TypeMapping();
				}
			}
		}
		return TypeMapping.mapping;
	}
	
	public void addService(String service, Map<String,Map<String,String>> details){
		typeMap.put(service, details);
	}
	
	public Map<String,Map<String,String>> getService(String service){
		return typeMap.get(service);
	}
	
}
