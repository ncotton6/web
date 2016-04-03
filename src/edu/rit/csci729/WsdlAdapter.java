package edu.rit.csci729;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.wsdl.Definition;
import javax.wsdl.Input;
import javax.wsdl.Message;
import javax.wsdl.Output;
import javax.wsdl.Part;
import javax.wsdl.Service;
import javax.wsdl.Types;
import javax.wsdl.WSDLException;
import javax.wsdl.extensions.schema.Schema;
import javax.wsdl.extensions.soap.SOAPAddress;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;

import org.w3c.dom.Element;

import com.ibm.wsdl.OperationImpl;
import com.ibm.wsdl.PortImpl;
import com.ibm.wsdl.PortTypeImpl;
import com.ibm.wsdl.extensions.schema.SchemaImpl;

import edu.rit.csci729.model.Operation;

public class WsdlAdapter {

	public WsdlAdapter() {

	}

	public List<Operation> processWSDL(String wsdl) throws WSDLException {
		// locals
		List<Operation> opers = new ArrayList<Operation>();
		Map<Object,Map<String,String>> messageMap = new HashMap<Object,Map<String,String>>();
		
		
		WSDLReader reader = WSDLFactory.newInstance().newWSDLReader();
		Definition wsdlDef = reader.readWSDL(wsdl);

		// setup messages
		Map messages = wsdlDef.getMessages();
		for(Object key : messages.keySet()){
			Message m = (Message)messages.get(key);
			Map parts = m.getParts();
			Map<String,String> indivMessageMap = new HashMap<String,String>();
			for(Object partKey : parts.keySet()){
				Part p = (Part)parts.get(partKey);
				String type = "";
				if(p.getTypeName() != null)
					type = p.getTypeName().getLocalPart();
				else if(p.getElementName() != null)
					type = p.getElementName().getLocalPart();
				indivMessageMap.put(p.getName(), type);
			}
			messageMap.put(m.getQName().getLocalPart(), indivMessageMap);
		}		
		
		// set service name
		Map services = wsdlDef.getServices();
		for (Object service : services.keySet()) {
			Service s = (Service) services.get(service);
			String serviceName = s.getQName().getLocalPart();
		}

		Map ports = wsdlDef.getPortTypes();
		System.out.println();
		for (Object p : ports.keySet()) {
			Object value = ports.get(p);
			PortTypeImpl port = (PortTypeImpl) value;
			List operations = port.getOperations();
			for (Object oper : operations) {
				Operation localOperation = new Operation();
				OperationImpl operation = (OperationImpl) oper;
				localOperation.setOperationName(operation.getName());
				Input input = operation.getInput();
				Message m = input.getMessage();
				Map<String,String> inputMap = new HashMap<String,String>();
				if (m != null) {
					Message message = wsdlDef.getMessage(m.getQName());
					localOperation.setInput(messageMap.get(m.getQName().getLocalPart()));
				}
				Output output = operation.getOutput();
				m = output.getMessage(); 
				Map<String,String> outputMap = new HashMap<String,String>();
				if (m != null) {
					Message message = wsdlDef.getMessage(m.getQName());
					localOperation.setOutput(messageMap.get(m.getQName().getLocalPart()));
				}
				opers.add(localOperation);
			}
		}

		Types types = wsdlDef.getTypes();
		
		if (types != null)
			for (Object o : types.getExtensibilityElements()) {
				if(o instanceof SchemaImpl){
					Schema si = (Schema)o;
					System.out.println("+++++++++");
					System.out.println(si.getElement());
					System.out.println("+++++++++");
				}
			}
		
		return opers;
	}

}
