package hiuller.data.parada;

import hiuller.objectmodel.Equipamentos;
import hiuller.objectmodel.Parada;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class LeitoraParadasXML
{
	
	private Document doc;
	private Schema schema;
		
	public LeitoraParadasXML(String fileName)
	{
				
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		dbf.setIgnoringElementContentWhitespace(true);
		
		DocumentBuilder db = null;
		try {
			
			db = dbf.newDocumentBuilder();
			
		} catch (ParserConfigurationException e) 
		{
			e.printStackTrace();
		}
		
		try {
			
			doc = db.parse(new File(fileName));
			
		} catch (SAXException e1) {
			
			e1.printStackTrace();
			System.exit(-1);
			
		} catch (IOException e1) {
			
			e1.printStackTrace();
			System.exit(-1);
		}
		
		SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		InputStream input = this.getClass().getResourceAsStream("parada_schema.xsd");
		
		if (input == null)
		{
			System.out.println("Couldn't reach resource: parada_schema.xsd");
			System.exit(-1);
		}
		
		try {
			
			schema = sf.newSchema(new StreamSource(input));
			
		} catch (SAXException e) 
		{
			e.printStackTrace();
		}
		
		Validator validator = schema.newValidator();
		try {
			
			validator.validate(new DOMSource(doc));
			
		} catch (SAXException e) 
		{
			e.printStackTrace();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	private Parada montarParada(Element element)
	{
		String prefix = element.getPrefix();
		
		int field0=0, field1=0, field2=0,  field3=0,  field4=0,  field5=0; 
		String field6="";
		
		NodeList nl = element.getChildNodes();
		for (int i=0; i<nl.getLength(); i++)
		{
			Node n = nl.item(i);
			String nodeName = n.getNodeName();
			
			if(nodeName.equals("#text") || nodeName == null)
				continue;
			
			if(nodeName.equals(prefix + ":equipamento"))
				field0 = Integer.valueOf(n.getFirstChild().getNodeValue()).intValue();
			
			if(nodeName.equals(prefix + ":dia"))
				field1 = Integer.valueOf(n.getFirstChild().getNodeValue()).intValue();
			
			if(nodeName.equals(prefix + ":hora"))
				field2 = Integer.valueOf(n.getFirstChild().getNodeValue()).intValue();
			
			if(nodeName.equals(prefix + ":minuto"))
				field3 = Integer.valueOf(n.getFirstChild().getNodeValue()).intValue();
			
			if(nodeName.equals(prefix + ":duracao"))
				field4 = Integer.valueOf(n.getFirstChild().getNodeValue()).intValue();
			
			if(nodeName.equals(prefix + ":fixa"))
				field5 = Integer.valueOf(n.getFirstChild().getNodeValue()).intValue();
			
			if(nodeName.equals(prefix + ":nota"))
				if (n.hasChildNodes())
					field6 = n.getFirstChild().getNodeValue();
		}
		
		return new Parada(Equipamentos.createEquip(field0), field1, field2, field3, field4, field6, field5 == 1 ? true : false);
	}
	
	public ArrayList<Parada> parse()
	{
		if(doc == null)
		{
			System.out.println("Document wasn't initialized!");
			System.exit(-1);
		}
		
		ArrayList<Parada> list = new ArrayList<Parada>();
		
		Element root = doc.getDocumentElement();
		NodeList nl = root.getChildNodes();
		
		for(int i=0; i<nl.getLength(); i++)
		{
			Node n = nl.item(i);
			
			if ( n instanceof Element)
				list.add( montarParada((Element) n));
		}
		
		return list;		
	}	
	
}
