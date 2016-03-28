package edu.rit.csci729.model;

import java.util.Map;

public class Operation {

	Map<String,String> input = null;
	Map<String,String> output = null;
	
	public Operation(Map<String,String> input, Map<String,String> output){
		this.input = input;
		this.output = output;
	}
	
}
