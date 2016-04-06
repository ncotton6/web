package test;

import java.util.List;

import javax.wsdl.WSDLException;

import edu.rit.csci729.Engine;
import edu.rit.csci729.WsdlAdapter;
import edu.rit.csci729.model.FieldConnection;
import edu.rit.csci729.model.NoMappingFound;
import edu.rit.csci729.model.Operation;

public class TestWsdlToWsdl {

	public static void main(String[] args) throws WSDLException {
		System.out.println("Start");

		WsdlAdapter wa = new WsdlAdapter();
		List<Operation> fromOperations = wa.processWSDL("from.wsdl");
		String fromService = wa.service;
		List<Operation> toOperations = wa.processWSDL("to.wsdl");
		String toService = wa.service;

		System.out.println("Beginning Tests\n======================");
		System.out.println("Searching for connections inside from wsdl\n======================");
		testConnections(fromOperations, fromOperations);
		System.out.println("\n\nSearching for connections inside to wsdl\n======================");
		testConnections(toOperations, toOperations);
		System.out.println("\n\nSearching for connections from from wsdl to to wsdl\n======================");
		testConnections(fromOperations, toOperations);
		System.out.println("\n\nSearching for connections from to wsdl to from wsdl\n======================");
		testConnections(toOperations, fromOperations);
		

		System.out.println("Done");
	}
	
	
	public static void testConnections(List<Operation> from, List<Operation> to)
	{
		for (int i = 0; i < to.size(); ++i) {
			for (int z = 0; z < from.size(); ++z) {
				Operation toOp = to.get(i);
				Operation fromOp = from.get(z);
				Engine e = new Engine(fromOp.getServiceName(), toOp.getServiceName());
				try {
					List<FieldConnection> fcs = e.generateMapping(fromOp, toOp, 0.5, true);
					System.out.println(String.format("Connection was found between operation %s <=> operation %s", fromOp.getOperationName(),
							toOp.getOperationName()));
					for (FieldConnection fc : fcs) {
						System.out.println(String.format("\t%s => (%s => %s) => %s with quality of connection %s", fc.fromConnection,
								fc.fromConnectionName, fc.toConnectionName, fc.toConnection, fc.qualityOfConnection));
					}
				} catch (NoMappingFound nmf) {
					System.out.println(String.format("The was no mapping found between %s <=/=> %s",
							fromOp.getOperationName(), toOp.getOperationName()));
				}
			}
		}
	}
}
