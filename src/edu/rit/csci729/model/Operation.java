package edu.rit.csci729.model;

import java.util.Map;

public class Operation {

	private Map<String,String> input = null;
	private Map<String,String> output = null;
	
	public Operation(Map<String,String> input, Map<String,String> output){
		this.input = input;
		this.output = output;
	}

	public Map<String, String> getInput() {
		return input;
	}

	public void setInput(Map<String, String> input) {
		this.input = input;
	}

	public Map<String, String> getOutput() {
		return output;
	}

	public void setOutput(Map<String, String> output) {
		this.output = output;
	}
	
}
