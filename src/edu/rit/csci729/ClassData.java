package edu.rit.csci729;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.rit.csci729.annotations.WebServiceField;
import edu.rit.csci729.annotations.WebServiceMethod;
import edu.rit.csci729.model.MappingSource;
import edu.rit.csci729.model.Tuple;

public class ClassData {

	private Class<?> clazz;
	private Collection<Field> fields;
	private Collection<Method> methods;
	
	public ClassData(Class<?> clazz){
		this.clazz = clazz;
		this.fields = new ArrayList<Field>();
		this.methods = new ArrayList<Method>();
		scanClass();
	}
	
	private void scanClass(){
		Method[] methods = clazz.getDeclaredMethods();
		for(Method m : methods){
			if(m.getAnnotation(WebServiceMethod.class) != null){
				this.methods.add(m);
			}
		}
		Field[] fields = clazz.getDeclaredFields();
		for(Field f : fields){
			if(f.getAnnotation(WebServiceField.class) != null)
				this.fields.add(f);
		}
	}

	public List<Tuple<Object,String[]>> getInfo() {
		ArrayList<Tuple<Object,String[]>> terms = new ArrayList<Tuple<Object,String[]>>();
		Set<String> s = new HashSet<String>();
		for(Field f : fields){
			WebServiceField wsf = f.getAnnotation(WebServiceField.class);
			s.clear();
			s.addAll(Arrays.asList(wsf.names()));
			s.add(f.getName());
			Tuple<Object,String[]> tup = new Tuple<Object, String[]>(f, s.toArray(new String[s.size()]));
			terms.add(tup);
		}
		//for(Method m : methods){
			//WebServiceMethod wsm = m.getAnnotation(WebServiceMethod.class);
			
		//}
		return terms;
	}
	
	public Map<MappingSource,String> getMap(){
		Map<MappingSource, String> fromClassData = new HashMap<MappingSource, String>();
		List<Tuple<Object, String[]>> data = getInfo();
		for (Tuple<Object, String[]> tup : data) {
			for (String s : tup.v2) {
				MappingSource ms = new MappingSource();
				ms.source = tup.v1;
				String type = "";
				if(ms.source instanceof Field){
					Field f = (Field)ms.source;
					type = f.getType().getName().toLowerCase();
				}
				ms.type = type;
				fromClassData.put(ms, s);
			}
		}
		return fromClassData;
	}
	
}
