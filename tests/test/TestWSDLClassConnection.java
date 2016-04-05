package test;

import java.util.List;

import javax.wsdl.WSDLException;

import edu.rit.csci729.Engine;
import edu.rit.csci729.WsdlAdapter;
import edu.rit.csci729.model.FieldConnection;
import edu.rit.csci729.model.NoMappingFound;
import edu.rit.csci729.model.Operation;

public class TestWSDLClassConnection {

	public static void main(String[] args) throws WSDLException {
		System.out.println("Start");

		WsdlAdapter wa = new WsdlAdapter();
		List<Operation> operations = wa.processWSDL("test.wsdl");
		Class<?> c = TestClass.class;
		Engine eng = new Engine(null, wa.service);
		for (Operation op : operations) {
			System.out.println(
					String.format("Testing connection between: %s <=> %s", op.getOperationName(), c.getName()));
			try {
				List<FieldConnection> fcs = eng.generateMapping(c, op, 0.1, true);
				for (FieldConnection fc : fcs) {
					System.out.println(String.format("\t%s => (%s => %s) => %s with qc of %s", fc.fromConnection,
							fc.fromConnectionName, fc.toConnectionName, fc.toConnection, fc.qualityOfConnection));
				}
			} catch (NoMappingFound nmf) {
				System.out.println("\tThere was no mapping");
			}
		}
		System.out.println("Done");
	}

}
