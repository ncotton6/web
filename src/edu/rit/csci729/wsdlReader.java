package edu.rit.csci729;

/*
 * 
 * Reference:-Xumin Liu's pdf on XML parsing
 */
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;

public class wsdlReader {
	static HashMap<String, String> inputhas = new HashMap<String, String>();
	static HashMap<String, String> outputhas = new HashMap<String, String>();
	static LinkedList<String> inputMessages = new LinkedList<String>();
	static LinkedList<String> outputMessages = new LinkedList<String>();
	static LinkedList<String> operations=new LinkedList<String>();

	static String getOperation(String file)
			throws ParserConfigurationException, SAXException, IOException {
		String operation = null;
		DocumentBuilderFactory db = DocumentBuilderFactory.newInstance();
		DocumentBuilder db1 = db.newDocumentBuilder();
		Document d = db1.parse(file);
		d.getDocumentElement().normalize();
		NodeList nmessage = (NodeList) d.getElementsByTagName("portType");
		for (int i = 0; i < nmessage.getLength(); i++) {

			Node node = nmessage.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element e = (Element) node;
				NodeList n1 = (NodeList) e.getElementsByTagName("operation");

				for (int k = 0; k < n1.getLength(); k++) {
					Node node1 = n1.item(k);
					if (node1.getNodeType() == Node.ELEMENT_NODE) {
						Element e1 = (Element) node1;
						operation = e1.getAttribute("name");

					}
				}
			}

		}

		return operation;

	}

	static String getinputoutput(String file) throws SAXException, IOException,
			ParserConfigurationException {

		String operation = null;
		DocumentBuilderFactory db = DocumentBuilderFactory.newInstance();
		DocumentBuilder db1 = db.newDocumentBuilder();
		Document d = db1.parse(file);
		d.getDocumentElement().normalize();
		NodeList nmessage = (NodeList) d.getElementsByTagName("portType");
		for (int i = 0; i < nmessage.getLength(); i++) {

			Node node = nmessage.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element e = (Element) node;
				NodeList n1 = (NodeList) e.getElementsByTagName("operation");

				for (int k = 0; k < n1.getLength(); k++) {
					Node node1 = n1.item(k);
					if (node1.getNodeType() == Node.ELEMENT_NODE) {
						Element e1 = (Element) node1;
						operation = e1.getAttribute("name");
						NodeList inputs = (NodeList) e1
								.getElementsByTagName("input");
						for (int inputindex = 0; inputindex < inputs
								.getLength(); inputindex++) {
							Node inputNode = inputs.item(inputindex);
							if (inputNode.getNodeType() == Node.ELEMENT_NODE) {
								Element in = (Element) inputNode;

								inputMessages.add(in.getAttribute("message"));

							}
						}
						NodeList outputs = (NodeList) e1
								.getElementsByTagName("input");
						for (int outputindex = 0; outputindex < inputs
								.getLength(); outputindex++) {
							Node outputNode = inputs.item(outputindex);
							if (outputNode.getNodeType() == Node.ELEMENT_NODE) {
								Element ou = (Element) outputNode;
								outputMessages.add(ou.getAttribute("message"));

							}
						}

					}
				}
			}

		}

		return operation;

	}

	static void checkMessages(String file) throws ParserConfigurationException,
			SAXException, IOException {

		DocumentBuilderFactory db = DocumentBuilderFactory.newInstance();
		DocumentBuilder db1 = db.newDocumentBuilder();
		Document d = db1.parse("newFile.wsdl");
		d.getDocumentElement().normalize();
		NodeList n = (NodeList) d.getElementsByTagName("message");
		boolean inputFlag = false;
		boolean outputFlag = false;
		for (int i = 0; i < n.getLength(); i++) {

			Node node = n.item(i);

			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element e = (Element) node;
				String toCheck = e.getAttribute("name");
				if (checkInInputs(toCheck)) {
					inputFlag=true;
				} else {
					outputFlag=true;
				}

				NodeList n1 = (NodeList) e.getElementsByTagName("part");
				// System.out.println(n1.getLength());

				for (int k = 0; k < n1.getLength(); k++) {
					Node insideItem = n1.item(k);
					if (insideItem.getNodeType() == Node.ELEMENT_NODE) {
						Element newElement = (Element) insideItem;
						String name = newElement.getAttribute("name");
						String type=newElement.getAttribute("type");
						if(inputFlag)
						{
							inputhas.put(name, type);
						}
						else
						{
							outputhas.put(name, type);
						}

					}

				}
			}

			inputFlag = false;
			outputFlag = false;

		}
	}

	static boolean checkInInputs(String input) {
		Iterator i = inputMessages.iterator();
		while (i.hasNext()) {
			String string = (String) i.next();
			if (string.contains(input)) {
				return true;
			}
		}

		return false;
	}

	static boolean checkInOutputs(String output) {
		Iterator i = outputMessages.iterator();
		
		while (i.hasNext()) {
			String string = (String) i.next();
			System.out.println(string+ " "+output);
			if (string.contains(output)) {
				System.out.println("int list");
				return true;
			}
		}
System.out.println("intlist");
		return false;
	}

	public static void main(String[] args) throws ParserConfigurationException,
			SAXException, IOException {

		FileReader f = new FileReader("newFile.wsdl");
		BufferedReader bufferedreader = new BufferedReader(f);
		DocumentBuilderFactory db = DocumentBuilderFactory.newInstance();
		DocumentBuilder db1 = db.newDocumentBuilder();
		Document d = db1.parse("newFile.wsdl");
		d.getDocumentElement().normalize();
		NodeList n = (NodeList) d.getElementsByTagName("message");
		for (int i = 0; i < n.getLength(); i++) {

			Node node = n.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element e = (Element) node;
				NodeList n1 = (NodeList) e.getElementsByTagName("part");
				for (int k = 0; k < n1.getLength(); k++) {
					Node insideItem = n1.item(k);
					if (insideItem.getNodeType() == Node.ELEMENT_NODE) {
						Element newElement = (Element) insideItem;

					}

				}
			}
		}

		String newOperation=getOperation("newFile.wsdl");
		System.out.println(newOperation);
		getinputoutput("newFile.wsdl");
		checkMessages("newFile.wsdl");
		
		Iterator i=inputhas.entrySet().iterator();
		while(i.hasNext())
		{
			Entry inputEntry=(Entry) i.next();
			String input=(String) inputEntry.getKey();
			String type=(String) inputEntry.getValue();
			System.out.println("input  "+ input+" "+type);
			
		}
		Iterator ouputIterator=outputhas.entrySet().iterator();
		while(ouputIterator.hasNext())
		{
			Entry outputEntry=(Entry) ouputIterator.next();
			String output=(String) outputEntry.getKey();
			String type=(String) outputEntry.getValue();
			System.out.println("output  "+ output+" "+type);
			
		}

	}

}
