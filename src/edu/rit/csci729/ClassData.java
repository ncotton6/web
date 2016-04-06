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

/**
 * {@link ClassData} is rather simple model object, with the exception of its
 * ability to scan a class upon construction. Based off the scanned class the
 * fields and methods lists are populated respective of their types.
 * 
 * @author Nathaniel Cotton
 *
 */
public class ClassData {

	// private variables
	private Class<?> clazz;
	private Collection<Field> fields;
	private Collection<Method> methods;

	/**
	 * Constructor takes a class to be scanned
	 * 
	 * @param clazz
	 */
	public ClassData(Class<?> clazz) {
		this.clazz = clazz;
		this.fields = new ArrayList<Field>();
		this.methods = new ArrayList<Method>();
		scanClass();
	}

	/**
	 * Scans the set class searching for methods and fields annotated with the,
	 * {@link WebServiceMethod} or {@link WebServiceField} annotations. If they
	 * are annotated they are added to a collection for their respective types.
	 */
	private void scanClass() {
		Method[] methods = clazz.getDeclaredMethods();
		for (Method m : methods) {
			if (m.getAnnotation(WebServiceMethod.class) != null) {
				this.methods.add(m);
			}
		}
		Field[] fields = clazz.getDeclaredFields();
		for (Field f : fields) {
			if (f.getAnnotation(WebServiceField.class) != null)
				this.fields.add(f);
		}
	}

	/**
	 * Based off the information pulled during the scanning of the class, a list
	 * of {@link Tuple} can be created. That list represents all of the possible
	 * points of connection with the class. {@link Tuple.v1} represents the
	 * object that provides the connect, either a Field, or a Method.
	 * {@link Tuple.v2} holds an array of Strings, that represents all the
	 * semantic meanings of {@link Tuple.v1}.
	 * 
	 * @return
	 */
	public List<Tuple<Object, String[]>> getInfo() {
		ArrayList<Tuple<Object, String[]>> terms = new ArrayList<Tuple<Object, String[]>>();
		Set<String> s = new HashSet<String>();
		for (Field f : fields) {
			WebServiceField wsf = f.getAnnotation(WebServiceField.class);
			s.clear();
			s.addAll(Arrays.asList(wsf.names()));
			s.add(f.getName());
			Tuple<Object, String[]> tup = new Tuple<Object, String[]>(f, s.toArray(new String[s.size()]));
			terms.add(tup);
		}
		// At the moment only fields are being added
		// for(Method m : methods){
		// WebServiceMethod wsm = m.getAnnotation(WebServiceMethod.class);

		// }
		return terms;
	}

	/**
	 * Based off the information pulled off of the class during scanning. A Map
	 * representation of the data can be provided. Where the key is a
	 * {@link MappingSource} and the value is a String. The
	 * {@link MappingSource} holds the object for the connection, along with the
	 * type representing the connection.
	 * 
	 * @return
	 */
	public Map<MappingSource, String> getMap() {
		Map<MappingSource, String> fromClassData = new HashMap<MappingSource, String>();
		List<Tuple<Object, String[]>> data = getInfo();
		for (Tuple<Object, String[]> tup : data) {
			for (String s : tup.v2) {
				MappingSource ms = new MappingSource();
				ms.source = tup.v1;
				String type = "";
				if (ms.source instanceof Field) {
					Field f = (Field) ms.source;
					type = f.getType().getName().toLowerCase();
				}
				ms.type = type;
				fromClassData.put(ms, s);
			}
		}
		return fromClassData;
	}

}
