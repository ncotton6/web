package test;

import java.util.List;
import java.util.Map.Entry;

import javax.wsdl.WSDLException;

import edu.rit.csci729.WsdlAdapter;
import edu.rit.csci729.model.Operation;

public class TestWSDLReader {

	public static void main(String[] args) throws WSDLException {
		System.out.println("Start");
		WsdlAdapter wa = new WsdlAdapter();
		List<Operation> operations = wa.processWSDL("ex.wsdl");
		for (Operation oper : operations) {
			System.out.println(oper.getOperationName());
			System.out.println("\tInput:");
			if (oper.getInput() != null)
				for (Entry<String, String> e : oper.getInput().entrySet()) {
					System.out.println("\t\t " + e.getKey() + " >> " + e.getValue());
				}
			System.out.println("\tOutput:");
			if (oper.getOutput() != null)
				for (Entry<String, String> e : oper.getOutput().entrySet()) {
					System.out.println("\t\t " + e.getKey() + " >> " + e.getValue());
				}
		}

		System.out.println("Done");
	}

}
