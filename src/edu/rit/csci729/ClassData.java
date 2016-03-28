package edu.rit.csci729;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

import edu.rit.csci729.annotations.WebServiceMethod;

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
			this.fields.add(f);
		}
	}
	
}
