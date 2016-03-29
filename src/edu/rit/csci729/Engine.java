package edu.rit.csci729;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import edu.rit.csci729.model.NoMappingFound;
import edu.rit.csci729.model.Operation;
import edu.rit.csci729.model.Tuple;
import edu.smu.tspell.wordnet.NounSynset;
import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.SynsetType;
import edu.smu.tspell.wordnet.WordNetDatabase;

public class Engine {


	private static Collection<String> primatives = new ArrayList<String>(){{
		add("xs:string");		
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
						return (int) ((o2.v2 * 10000) - (o1.v2 * 10000));
					}
				});

		// test exact match
		for (String classInfo : cd.getInfo()) {
			Tuple<String, Double> tup = new Tuple<String, Double>();
			tup.v1 = classInfo;
			tup.v2 = compareStrings(key, classInfo);
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
		// TODO Auto-generated method stub

	}

	private static void findSense() {

	}

	private static Double compareStrings(String s1, String s2) {
		// needs improvement
		if (s1.toLowerCase().equals(s2.toLowerCase()))
			return 1d;
		return 0d;
	}

	private static void addForms(String[] forms, PriorityQueue<Tuple<String, Double>> proQue, String classInfo) {
		for (String word : forms) {
			Tuple<String, Double> tup = new Tuple<String, Double>();
			tup.v1 = classInfo;
			tup.v2 = compareStrings(classInfo, word);
			proQue.add(tup);
		}
	}

}
