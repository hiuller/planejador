 package hiuller.data.parada;

import hiuller.objectmodel.Parada;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class EscritoraParadasXML {
	
	private Document doc;	
	private Schema schema;
	
	public static final String PREFIX = "tns";

	public EscritoraParadasXML()
	{
		DocumentBuilder documentBuilder = null;
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		
		documentBuilderFactory.setNamespaceAware(true);
		documentBuilderFactory.setIgnoringElementContentWhitespace(true);
		
		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);		
		InputStream input = this.getClass().getResourceAsStream("parada_schema.xsd");
		
		if (input == null)
		{
			
		}
		
		try {
			
			schema = schemaFactory.newSchema(new StreamSource(input));
			
		} catch (SAXException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		documentBuilderFactory.setValidating(true);
		documentBuilderFactory.setSchema(schema);
		documentBuilderFactory.setNamespaceAware(true);
		
		try {
			
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
			
		} catch (ParserConfigurationException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.doc = documentBuilder.newDocument();
		
		Element e = doc.createElementNS("http://www.example.org/parada_schema", "root_element");
		e.setPrefix(PREFIX);
		
		this.doc.appendChild((Node) e);
	}
	
	public void assembleTree(ArrayList<Parada> paradas)
	{
		for(Parada parada:paradas)
			doc.getDocumentElement().appendChild((Node) createElement(parada));
	}

	private void appendIntegerElement(Element root, String name, Object value)
	{
		Element element = doc.createElement(PREFIX + ":" + name);
		element.appendChild(doc.createTextNode( value.toString() ));
		root.appendChild(element);		
	}
	
	private Element createElement(Parada parada)
	{
		Element root = doc.createElement(PREFIX + ":item_colecao");
		
		appendIntegerElement(root, "equipamento", parada.getEquipamentoNumber());
		appendIntegerElement(root, "dia",         parada.getDia());
		appendIntegerElement(root, "hora",        parada.getHora());
		appendIntegerElement(root, "minuto",      parada.getMinuto());
		appendIntegerElement(root, "duracao",     parada.getDuracao());
		appendIntegerElement(root, "fixa",        parada.isFixed() ? 1 : 0);
		appendIntegerElement(root, "nota",        parada.getNota());
		
		return root;
	}
	
	public boolean Save(String fileName)
	{
		File file = new File(fileName);
		try {
			
			file.createNewFile();
			
		} catch (IOException e) {
			String message = String.format("Não foi possível criar o arquivo desejado: [%s].\n'%s'", fileName, e.getMessage());
			JOptionPane.showMessageDialog(null, message, "Exceção na classe XML_Writer.java", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		transformerFactory.setAttribute("indent-number", new Integer(2));
		
		Transformer transformer = null;
		try {
			
			transformer = transformerFactory.newTransformer();
			
		} catch (TransformerConfigurationException e) 
		{
			String message = String.format("Falha na criação do Transformer.\n'%s'", e.getMessage());
			JOptionPane.showMessageDialog(null, message, "Exceção na classe XML_Writer.java", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		
		Source source = new DOMSource(doc);		
		
		OutputStream outputStream = null;
		
		try {
			
			outputStream = new FileOutputStream(file);
			
		} catch (FileNotFoundException e) 
		{
			String message = String.format("Falha na criação do arquivo de saída.\n'%s'", e.getMessage());
			JOptionPane.showMessageDialog(null, message, "Exceção na classe XML_Writer.java", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		OutputStreamWriter outputStreamWriter = null;
		try
		{
			outputStreamWriter = new OutputStreamWriter(outputStream, "UTF8");
		} catch (UnsupportedEncodingException e1)
		{
			e1.printStackTrace();
		}
		Result result = new StreamResult(outputStreamWriter);		

		try {
			
			transformer.transform(source, result);
			
		} catch (TransformerException e) 
		{
			String message = String.format("Falha na tradução do XML.\n'%s'", e.getMessage());
			JOptionPane.showMessageDialog(null, message, "Exceção na classe XML_Writer.java", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		try
		{
			outputStream.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return true;		
	}
}
