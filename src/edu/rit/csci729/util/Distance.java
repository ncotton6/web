package edu.rit.csci729.util;

import info.debatty.java.stringsimilarity.Levenshtein;
import info.debatty.java.stringsimilarity.NGram;

public class Distance {
	
	
	public static double NLevenshteinSim(String s1, String s2){
		Levenshtein l = new Levenshtein();
		return l.similarity(s1, s2);
	}
	
	public static double NGramSim2(String s1, String s2){
		return Distance.NGramSim(s1, s2, 2);
	}
	
	public static double NGramSim(String s1, String s2, int num){
		NGram ng = new NGram(num);
		return ng.similarity(s1, s2);
	}
	
	
	public static int LevenshteinDistance(String s1, String s2) {
		return LevenshteinDistance(s1, s1.length(), s2, s2.length());
	}
	
	private static int LevenshteinDistance(String s1, int lenS1, String s2, int lenS2) {
		int cost = 0;
		if (lenS1 == 0)
			return lenS2;
		if (lenS2 == 0)
			return lenS1;

		if (s1.charAt(lenS1 - 1) == s2.charAt(lenS2 - 1))
			cost = 0;
		else
			cost = 1;

		return Math.min(LevenshteinDistance(s1, lenS1 - 1, s2, lenS2) + 1,
				Math.min(LevenshteinDistance(s1, lenS1, s2, lenS2 - 1) + 1,
						LevenshteinDistance(s1, lenS1 - 1, s2, lenS2 - 1) + cost));
	}
}
