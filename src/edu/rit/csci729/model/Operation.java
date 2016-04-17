package edu.rit.csci729.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Simple modal class to hold information pertaining to the inputs and outputs
 * of an operation.
 * 
 * @author Nathaniel Cotton
 *
 */
public class Operation {

	private String operationName;
	private String serviceName;
	private Map<String, String> input = null;
	private Map<String, String> output = null;

	public Operation() {
	}

	public Operation(Map<String, String> input, Map<String, String> output) {
		this.input = input;
		this.output = output;
	}

	public Operation(Map<String, String> input, Map<String, String> output, String operationName) {
		this.input = input;
		this.output = output;
		this.operationName = operationName;
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

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public Map<MappingSource, String> getInputMap() {
		return generateMapping(input);
	}

	public Map<MappingSource, String> getOutputMap() {
		return generateMapping(output);
	}

	private Map<MappingSource, String> generateMapping(Map<String, String> map) {
		Map<MappingSource, String> ret = new HashMap<MappingSource, String>();
		for (Entry<String, String> entry : map.entrySet()) {
			if (TypeMapping.get().getService(serviceName).containsKey(entry.getValue())) {
				Map<MappingSource,String> temp = generateMapping(TypeMapping.get().getService(serviceName).get(entry.getValue()));
				for(Entry<MappingSource, String> ent : temp.entrySet()){
					MappingSource ms = new MappingSource();
					ms.source = entry.getKey()+"."+ent.getKey().source;
					ms.type = ent.getKey().type;
					ret.put(ms,ent.getValue());
				}
			} else {
				MappingSource ms = new MappingSource();
				ms.source = entry.getKey();
				ms.type = entry.getValue();
				ret.put(ms, entry.getKey());
			}
		}
		return ret;
	}

}
