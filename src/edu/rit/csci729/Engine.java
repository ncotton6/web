package edu.rit.csci729;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import edu.rit.csci729.model.FieldConnection;
import edu.rit.csci729.model.MappingSource;
import edu.rit.csci729.model.NoMappingFound;
import edu.rit.csci729.model.Operation;
import edu.rit.csci729.model.Tuple;
import edu.rit.csci729.model.TypeMapping;
import edu.rit.csci729.util.Distance;
import edu.smu.tspell.wordnet.NounSynset;
import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.WordNetDatabase;

/**
 * The {@link Engine is where all the connections are formulated between
 * connection points, and the inputs for wsdl operation. Or it can be used to
 *T formulate a connection between the outputs for one wsdl operation, and the
 * inputs of another. Then in rare cases it could also be used for form a
 * connection between fields in classes.
 * 
 * @author Nathaniel Cotton
 *
 */
public class Engine {

	// Private Variables
	private static String dictLocation = "C:\\WordNet\\2.1\\dict\\";
	private static Double w1 = 1d, w2 = 0.25d, w3 = 1d, w4 = 1d, w5 = 1d, w6 = 1d, w7 = 1d;

	// The table of type to type, and their respective value of likelihood of
	// transition
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
			put(new Tuple<String, String>("string", "double"), 1d / 2);
		}
	};

	// Sets up the word net location of the dictionary
	static {
		String value = System.getProperty("wordnet.database.dir", "");
		if (value.isEmpty()) {
			System.setProperty("wordnet.database.dir", dictLocation);
		}
	}

	private String fromService, toService;

	/**
	 * Simple constructor that sets the service values for the transitions.
	 * These are used for looking up the respective type mappings in the
	 * {@link TypeMapping} class.
	 * 
	 * @param fromService
	 * @param toService
	 */
	public Engine(String fromService, String toService) {
		this.fromService = fromService;
		this.toService = toService;
	}

	/**
	 * Generates a List of the connections between the to components, or throws
	 * and exception if there is not an adequate connection in the list. But,
	 * that will only happen if check is set to true, otherwise no exception
	 * will be thrown.
	 * 
	 * @param from
	 * @param to
	 * @param threshold
	 * @param check
	 * @return
	 * @throws NoMappingFound
	 */
	public List<FieldConnection> generateMapping(Class<?> from, Operation to, double threshold, boolean check)
			throws NoMappingFound {
		ClassMap cMap = ClassMap.get();
		cMap.scanClass(from);
		ClassData cd = cMap.getClassData(from);
		return mapGeneratService(cd.getMap(), to.getInputMap(), threshold, check);
	}

	/**
	 * 
	 * Generates a List of the connections between the to components, or throws
	 * and exception if there is not an adequate connection in the list. But,
	 * that will only happen if check is set to true, otherwise no exception
	 * will be thrown.
	 * 
	 * @param from
	 * @param to
	 * @param threshold
	 * @param check
	 * @return
	 * @throws NoMappingFound
	 */
	public List<FieldConnection> generateMapping(ClassData from, Operation to, double threshold, boolean check)
			throws NoMappingFound {
		return mapGeneratService(from.getMap(), to.getInputMap(), threshold, check);
	}

	/**
	 * 
	 * Generates a List of the connections between the to components, or throws
	 * and exception if there is not an adequate connection in the list. But,
	 * that will only happen if check is set to true, otherwise no exception
	 * will be thrown.
	 * 
	 * 
	 * @param from
	 * @param to
	 * @param threshold
	 * @param check
	 * @return
	 * @throws NoMappingFound
	 */
	public List<FieldConnection> generateMapping(Operation from, Operation to, double threshold, boolean check)
			throws NoMappingFound {
		return mapGeneratService(from.getOutputMap(), to.getInputMap(), threshold, check);
	}

	/**
	 * 
	 * 
	 * Generates a List of the connections between the to components, or throws
	 * and exception if there is not an adequate connection in the list. But,
	 * that will only happen if check is set to true, otherwise no exception
	 * will be thrown.
	 * 
	 * @param from
	 * @param to
	 * @param threshold
	 * @param check
	 * @return
	 * @throws NoMappingFound
	 */
	public List<FieldConnection> mapGeneratService(Map<MappingSource, String> from, Map<MappingSource, String> to,
			double threshold, boolean check) throws NoMappingFound {
		ArrayList<FieldConnection> mappings = new ArrayList<FieldConnection>();
		for (MappingSource key : to.keySet()) {
			String type = to.get(key);
			if (toService == null
					|| (toService != null && !TypeMapping.get().getService(toService).containsKey(type))) {
				mappings.add(findIdealMapping(key,(String)to.get(key), from));
			}
		}
		if (check) {
			Set<Object> used = new HashSet<Object>();
			for (FieldConnection fc : mappings) {
				if (used.contains(fc.fromConnection))
					throw new NoMappingFound("Multiple mappings from the same data value");
				if (fc.qualityOfConnection < threshold)
					throw new NoMappingFound("Best mapping didn't surpass set threshold");
				used.add(fc.fromConnection);
			}
		}
		return mappings;
	}

	/**
	 * 
	 * Given a single {@link MappingSource} that is trying to be connected to
	 * from a Map of available data. Then returns the greatest matched
	 * connection. Which in actuality may not be a very good match, but the
	 * threshold check elsewhere will cover that.
	 * 
	 * @param to
	 * @param value 
	 * @param from
	 * @return
	 */
	private FieldConnection findIdealMapping(MappingSource to, String value, Map<MappingSource, String> from) {

		WordNetDatabase database = WordNetDatabase.getFileInstance();
		PriorityQueue<FieldConnection> proQue = new PriorityQueue<FieldConnection>(new Comparator<FieldConnection>() {

			@Override
			public int compare(FieldConnection o1, FieldConnection o2) {
				if (o1.qualityOfConnection == o2.qualityOfConnection)
					return 0;
				return o1.qualityOfConnection < o2.qualityOfConnection ? 1 : -1;
			}
		});

		// if we had access to more semantic information performing a
		// findContext
		findContext();
		// and respectively findSense could limit the amount of computation
		// needed.
		findSense();

		// test exact match
		processForm(from, (String) value, to, proQue);

		// test with wordnet
		Synset[] synonyms = database.getSynsets((String) value);
		for (Synset s : synonyms) {
			if (s instanceof NounSynset) {
				NounSynset ns = (NounSynset) s;
				// Test synonyms
				String[] forms = ns.getWordForms();
				for (String f : forms) {
					processForm(from, f, to, proQue);
				}
				// Test hypernyms
				NounSynset[] hypernyms = ns.getHypernyms();
				for (NounSynset nss : hypernyms) {
					forms = nss.getWordForms();
					for (String f : forms) {
						processForm(from, f, to, proQue);
					}
				}
				// Test hyponyms
				NounSynset[] hyponyms = ns.getHyponyms();
				for (NounSynset nss : hyponyms) {
					forms = nss.getWordForms();
					for (String f : forms) {
						processForm(from, f, to, proQue);
					}
				}
			}
		}
		// return the first best value
		return proQue.peek();
	}

	/**
	 * Given a string and the mapping source, this function will run through all
	 * strings held in the source, and test against the destination.
	 * 
	 * @param from
	 * @param toWordForm
	 * @param to
	 * @param proQue
	 */
	private void processForm(Map<MappingSource, String> from, String toWordForm, MappingSource to,
			PriorityQueue<FieldConnection> proQue) {
		for (MappingSource mkey : from.keySet()) {
			String name = from.get(mkey);
			// test word similarity
			double value = Distance.NGramSim2(toWordForm.toLowerCase(), name.toLowerCase());
			// get type similarity
			Double typeValue = 0d;
			if(mkey.type.toLowerCase().equals(to.type.toLowerCase()))
				typeValue = 1d;
			else if(typeMapping.containsKey(new Tuple<String, String>(mkey.type.toLowerCase(), to.type.toLowerCase())))
				typeValue = typeMapping.get(new Tuple<String, String>(mkey.type.toLowerCase(), to.type.toLowerCase()));
			else
				typeValue = 0.1d; // something fairly low
			// word and type
			double combined = (w1*value + w2*typeValue)/(w1+w2);
			FieldConnection fc = new FieldConnection();
			fc.fromConnection = mkey.source;
			fc.fromConnectionName = name;
			fc.qualityOfConnection = combined;
			fc.toConnectionName = toWordForm;
			fc.toConnection = (String) to.source;
			proQue.add(fc);
		}
	}

	// The rest really isn't pertinent to our implementation at this time.

	private void findContext() {
	}

	private void findSense() {
	}

	/*
	 * private Double conceptSim(Concept c1, Concept c2) { return ((w1 *
	 * synSim(c1, c2)) + (w2 * propSim(c1, c2)) + (w3 * cvrgSim(c1, c2))) / (w1
	 * + w2 + w3); }
	 * 
	 * private Double cvrgSim(Concept c1, Concept c2) { return 1d; }
	 * 
	 * private Double synSim(Concept c1, Concept c2) { return (w4 *
	 * NameMatch(c1.name, c2.name) + w5 * DescrMatch(c1.name, c1.name)) / (w4 +
	 * w5); }
	 * 
	 * private Double propSim(Concept s1, Concept s2) { // go through all of the
	 * properties of s1 finding matches in s2. return 1d; }
	 */

	private Double propMatch() {
		return 1d;
	}

	private Double rangeSim() {
		return (w6 * synSim() + w7 * propSynSim()) / (w6 + w7);
	}

	private Double synSim() {
		return 1d;
	}

	private Double propSynSim() {
		return 1d;
	}

	private Double cvrgSim(String s1, String s2) {
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
}
