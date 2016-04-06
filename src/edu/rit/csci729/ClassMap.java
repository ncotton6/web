package edu.rit.csci729;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * {@link ClassMap} holds a collection of {@link ClassData} objects for quick
 * retrieval at a moments notice.
 * 
 * @author Nathaniel Cotton
 *
 */
public class ClassMap {

	// private variables
	private Map<Class<?>, ClassData> classData;
	private static ClassMap classMap = null;

	private ClassMap() {
		classData = new HashMap<Class<?>, ClassData>();
	}

	/**
	 * In order to implement the singleton design pattern, the constructor has
	 * been created as private. Therefore the only way to get an instance of the
	 * ClassMap is through the get method.
	 * 
	 * @return ClassMap
	 */
	public static ClassMap get() {
		if (classMap == null) {
			synchronized (ClassMap.class) {
				if (classMap == null) {
					classMap = new ClassMap();
				}
			}
		}
		return classMap;
	}

	/**
	 * Scans a collection of classes into the system
	 * 
	 * @param clazzes
	 */
	public void scanClasses(Collection<String> clazzes) {
		for (String clazz : clazzes) {
			try {
				Class<?> c = Class.forName(clazz);
				scanClass(c);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Scans a single class and adds it to the system.
	 * 
	 * @param c
	 * @return
	 */
	public ClassData scanClass(Class<?> c) {
		if (!classData.containsKey(c)) {
			ClassData cd = new ClassData(c);
			classData.put(c, cd);
		}
		return classData.get(c);
	}

	/**
	 * Retrieves a class from the system.
	 * 
	 * @param c
	 * @return
	 */
	public ClassData getClassData(Class<?> c) {
		return classData.get(c);
	}

}
