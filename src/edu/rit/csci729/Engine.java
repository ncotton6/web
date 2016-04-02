package edu.rit.csci729;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import edu.rit.csci729.model.Concept;
import edu.rit.csci729.model.NoMappingFound;
import edu.rit.csci729.model.Operation;
import edu.rit.csci729.model.Tuple;
import edu.rit.csci729.util.Distance;
import edu.smu.tspell.wordnet.NounSynset;
import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.SynsetType;
import edu.smu.tspell.wordnet.WordNetDatabase;

public class Engine {

	private static Double w1=1d,w2=1d,w3=1d,w4=1d,w5=1d,w6=1d,w7=1d;
	
	
	private final static Collection<String> primatives = new ArrayList<String>() {
		{
			add("string");
		}
	};
	
	private final static Map<Tuple<String,String>,Double> typeMapping = new HashMap<Tuple<String,String>,Double>(){{
		put(new Tuple<String,String>("decimal","string"), 1d);
		put(new Tuple<String,String>("decimal","float"), 1d);
		put(new Tuple<String,String>("decimal","double"), 1d);
		put(new Tuple<String,String>("long","decimal"), 3d/4);
		put(new Tuple<String,String>("long","integer"), 1d);
		put(new Tuple<String,String>("integer","string"), 1d);
		put(new Tuple<String,String>("integer","long"), 2d/3);
		put(new Tuple<String,String>("integer","decimal"), 1d/3);
		put(new Tuple<String,String>("integer","float"), 1d/3);
		put(new Tuple<String,String>("integer","double"), 1d/3);
		put(new Tuple<String,String>("string","integer"), 1d/2);
		put(new Tuple<String,String>("string","decimal"), 1d/2);
	}};

	public static List<Tuple<String, String>> generateMapping(Class<?> c, Operation oper) throws NoMappingFound {
		ClassMap cMap = ClassMap.get();
		cMap.scanClass(c);
		ClassData cd = cMap.getClassData(c);
		return generateMapping(cd, oper);
	}

	public static List<Tuple<String, String>> generateMapping(ClassData cd, Operation oper) throws NoMappingFound {
		ArrayList<Tuple<String, String>> mappings = new ArrayList<Tuple<String, String>>();
		for (String key : oper.getInput().keySet()) {
			String value = oper.getInput().get(key);
			if (Engine.primatives.contains(value)) {
				// the simple case where the type is a primitive
				// there is no need to traverse a tree
				String mappedValue = findIdealMapping(key, cd);
				Tuple<String, String> t = new Tuple<String, String>();
				t.v1 = mappedValue;
				t.v2 = key;
				mappings.add(t);
			}
		}
		return mappings;
	}

	private static String findIdealMapping(String key, ClassData cd) {

		WordNetDatabase database = WordNetDatabase.getFileInstance();
		findContext();
		findSense();
		PriorityQueue<Tuple<String, Double>> proQue = new PriorityQueue<Tuple<String, Double>>(
				new Comparator<Tuple<String, Double>>() {
					@Override
					public int compare(Tuple<String, Double> o1, Tuple<String, Double> o2) {
						return (int) ((o1.v2 * 10000) - (o2.v2 * 10000));
					}
				});

		// test exact match
		for (String classInfo : cd.getInfo()) {
			Tuple<String, Double> tup = new Tuple<String, Double>();
			tup.v1 = classInfo;
			tup.v2 = NameMatch(key, classInfo);
			proQue.add(tup);
		}

		// test with wordnet
		Synset[] synonyms = database.getSynsets(key);
		for (Synset s : synonyms) {
			if (s instanceof NounSynset) {
				NounSynset ns = (NounSynset) s;
				String[] forms = ns.getWordForms();
				NounSynset[] hypernyms = ns.getHypernyms();
				NounSynset[] hyponyms = ns.getHyponyms();
				for (String classInfo : cd.getInfo()) {
					addForms(forms, proQue, classInfo);
					for (NounSynset hyper : hypernyms) {
						addForms(hyper.getWordForms(), proQue, classInfo);
					}
					for (NounSynset hypo : hyponyms) {
						addForms(hypo.getWordForms(), proQue, classInfo);
					}
				}
			}
		}
		System.out.println("Matching :: " + proQue.peek().v1 + "  - " + proQue.peek().v2);
		return proQue.peek().v1;
	}

	private static void findContext() {
		
	}

	private static void findSense() {

	}

	private static Double conceptSim(Concept c1, Concept c2){
		return ((w1*synSim(c1, c2)) + (w2*propSim(c1,c2)) + (w3*cvrgSim(c1, c2)))/(w1+w2+w3);
	}
	
	private static Double cvrgSim(Concept c1, Concept c2){
		return 1d;
	}
	
	private static Double synSim(Concept c1, Concept c2){
		return (w4 * NameMatch(c1.name, c2.name) + w5 * DescrMatch(c1.name, c1.name))/(w4 + w5);
	}
	
	private static Double propSim(Concept s1, Concept s2){
		// go through all of the properties of s1 finding matches in s2.
		return 1d;
	}
	
	private static Double propMatch(){
		return 1d;
	}
	
	private static Double rangeSim(){
		return (w6 * synSim() + w7*propSynSim())/(w6+w7);
	}
	
	private static Double synSim(){
		return 1d;
	}
	
	private static Double propSynSim(){
		return 1d;
	}
	
	private static Double cvrgSim(String s1, String s2){
		return 1d;
	}
	
	private static Double NameMatch(String s1, String s2) {
		// needs improvement
		if (s1.toLowerCase().equals(s2.toLowerCase()))
			return 0d;
		// LevenshteinDistance used instead of n-gram algorithm
		return ((double)Distance.LevenshteinDistance(s1, s2)/Math.max(s1.length(), s2.length())); 
	}
	
	private static Double DescrMatch(String s1, String s2){
		if(s1.toLowerCase().equals(s2.toLowerCase()))
			return 0d;
		return 1d; // do to the lack of descriptions
	}

	private static void addForms(String[] forms, PriorityQueue<Tuple<String, Double>> proQue, String classInfo) {
		for (String word : forms) {
			Tuple<String, Double> tup = new Tuple<String, Double>();
			tup.v1 = classInfo;
			tup.v2 = NameMatch(classInfo, word);
			proQue.add(tup);
		}
	}

	

}
