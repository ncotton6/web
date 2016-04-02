package edu.rit.csci729;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import edu.rit.csci729.model.Concept;
import edu.rit.csci729.model.FieldConnection;
import edu.rit.csci729.model.NoMappingFound;
import edu.rit.csci729.model.Operation;
import edu.rit.csci729.model.Tuple;
import edu.rit.csci729.util.Distance;
import edu.smu.tspell.wordnet.NounSynset;
import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.WordNetDatabase;

public class Engine {
	private static String dictLocation = "C:\\WordNet\\2.1\\dict\\";
	private static Double w1 = 1d, w2 = 1d, w3 = 1d, w4 = 1d, w5 = 1d, w6 = 1d, w7 = 1d;
	private final static Collection<String> primatives = new ArrayList<String>() {
		{
			add("string");
			add("boolean");
			add("byte");
			add("double");
			add("float");
			add("integer");
			add("long");
			add("short");
			add("decimal");
			add("int");
			// add("anyURI");
			// add("dateTime");
			// add("QName");
		}
	};

	private final static Map<Tuple<String, String>, Double> typeMapping = new HashMap<Tuple<String, String>, Double>() {
		{
			put(new Tuple<String, String>("decimal", "string"), 1d);
			put(new Tuple<String, String>("decimal", "float"), 1d);
			put(new Tuple<String, String>("decimal", "double"), 1d);
			put(new Tuple<String, String>("long", "decimal"), 3d / 4);
			put(new Tuple<String, String>("long", "integer"), 1d);
			put(new Tuple<String, String>("integer", "string"), 1d);
			put(new Tuple<String, String>("integer", "long"), 2d / 3);
			put(new Tuple<String, String>("integer", "decimal"), 1d / 3);
			put(new Tuple<String, String>("integer", "float"), 1d / 3);
			put(new Tuple<String, String>("integer", "double"), 1d / 3);
			put(new Tuple<String, String>("string", "integer"), 1d / 2);
			put(new Tuple<String, String>("string", "decimal"), 1d / 2);
		}
	};

	public static List<FieldConnection> generateMapping(Class<?> c, Operation oper, double threshold,
			boolean throwException) throws NoMappingFound {
		ClassMap cMap = ClassMap.get();
		cMap.scanClass(c);
		ClassData cd = cMap.getClassData(c);
		return generateMapping(cd, oper, threshold, throwException);
	}

	public static List<FieldConnection> generateMapping(Map<Object, String> map, Operation oper, double threshold,
			boolean throwException) throws NoMappingFound {
		setDict();
		ArrayList<FieldConnection> mappings = new ArrayList<FieldConnection>();
		for (String key : oper.getInput().keySet()) {
			String value = oper.getInput().get(key);
			if (Engine.primatives.contains(value)) {
				mappings.add(findIdealMapping(key, map));
			}
		}
		if (throwException) {
			Set<Object> used = new HashSet<Object>();
			for (FieldConnection fc : mappings) {
				if (used.contains(fc.classConnection))
					throw new NoMappingFound("Multiple mappings from the same data value");
				if (fc.qualityOfConnection < threshold)
					throw new NoMappingFound("Best mapping didn't surpass set threshold");
				used.add(fc.classConnection);
			}
		}
		return mappings;
	}

	public static List<FieldConnection> generateMapping(ClassData cd, Operation oper, double threshold,
			boolean throwException) throws NoMappingFound {
		Map<Object, String> fromClassData = new HashMap<Object, String>();
		List<Tuple<Object, String[]>> data = cd.getInfo();
		for (Tuple<Object, String[]> tup : data) {
			for (String s : tup.v2) {
				fromClassData.put(tup.v1, s);
			}
		}
		return generateMapping(fromClassData, oper, threshold, throwException);
	}

	private static FieldConnection findIdealMapping(String key, Map<Object, String> map) {

		WordNetDatabase database = WordNetDatabase.getFileInstance();
		PriorityQueue<FieldConnection> proQue = new PriorityQueue<FieldConnection>(new Comparator<FieldConnection>() {

			@Override
			public int compare(FieldConnection o1, FieldConnection o2) {
				if (o1.qualityOfConnection == o2.qualityOfConnection)
					return 0;
				return o1.qualityOfConnection < o2.qualityOfConnection ? 1 : -1;
			}
		});

		findContext();
		findSense();

		// test exact match
		processForm(map, key, key, proQue);

		// test with wordnet

		Synset[] synonyms = database.getSynsets(key);
		for (Synset s : synonyms) {
			if (s instanceof NounSynset) {
				NounSynset ns = (NounSynset) s;
				String[] forms = ns.getWordForms();
				for (String f : forms) {
					processForm(map, f, key, proQue);
				}
				NounSynset[] hypernyms = ns.getHypernyms();
				for (NounSynset nss : hypernyms) {
					forms = nss.getWordForms();
					for (String f : forms) {
						processForm(map, f, key, proQue);
					}
				}
				NounSynset[] hyponyms = ns.getHyponyms();
				for (NounSynset nss : hyponyms) {
					forms = nss.getWordForms();
					for (String f : forms) {
						processForm(map, f, key, proQue);
					}
				}
			}
		}

		return proQue.peek();
	}

	private static void processForm(Map<Object, String> map, String form, String key,
			PriorityQueue<FieldConnection> proQue) {
		for (Object mkey : map.keySet()) {
			String name = map.get(mkey);
			double value = Distance.NGramSim2(form.toLowerCase(), name.toLowerCase());
			FieldConnection fc = new FieldConnection();
			fc.classConnection = mkey;
			fc.classConnectionName = name;
			fc.qualityOfConnection = value;
			fc.webServiceConnectionName = form;
			fc.webServiceName = key;
			proQue.add(fc);
		}
	}

	private static void findContext() {
	}

	private static void findSense() {
	}

	private static Double conceptSim(Concept c1, Concept c2) {
		return ((w1 * synSim(c1, c2)) + (w2 * propSim(c1, c2)) + (w3 * cvrgSim(c1, c2))) / (w1 + w2 + w3);
	}

	private static Double cvrgSim(Concept c1, Concept c2) {
		return 1d;
	}

	private static Double synSim(Concept c1, Concept c2) {
		return (w4 * NameMatch(c1.name, c2.name) + w5 * DescrMatch(c1.name, c1.name)) / (w4 + w5);
	}

	private static Double propSim(Concept s1, Concept s2) {
		// go through all of the properties of s1 finding matches in s2.
		return 1d;
	}

	private static Double propMatch() {
		return 1d;
	}

	private static Double rangeSim() {
		return (w6 * synSim() + w7 * propSynSim()) / (w6 + w7);
	}

	private static Double synSim() {
		return 1d;
	}

	private static Double propSynSim() {
		return 1d;
	}

	private static Double cvrgSim(String s1, String s2) {
		return 1d;
	}

	private static Double NameMatch(String s1, String s2) {
		// needs improvement
		if (s1.toLowerCase().equals(s2.toLowerCase()))
			return 0d;
		// LevenshteinDistance used instead of n-gram algorithm
		return ((double) Distance.LevenshteinDistance(s1, s2) / Math.max(s1.length(), s2.length()));
	}

	private static Double DescrMatch(String s1, String s2) {
		if (s1.toLowerCase().equals(s2.toLowerCase()))
			return 0d;
		return 1d; // do to the lack of descriptions
	}

	// private static void addForms(String[] forms,
	// PriorityQueue<Tuple<Tuple<Object,String>, Double>> proQue, String
	// classInfo) {
	// for (String word : forms) {
	// Tuple<String, Double> tup = new Tuple<String, Double>();
	// tup.v1 = classInfo;
	// tup.v2 = NameMatch(classInfo, word);
	// proQue.add(tup);
	// }
	// }

	private static void setDict() {
		String value = System.getProperty("wordnet.database.dir", "");
		if (value.isEmpty()) {
			System.setProperty("wordnet.database.dir", dictLocation);
		}
	}
}
