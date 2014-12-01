package hiuller.tests;

import hiuller.data.parada.EscritoraParadasXML;
import hiuller.data.parada.LeitoraParadasXML;
import hiuller.objectmodel.Parada;
import hiuller.objectmodel.Equipamentos.Equip;

import java.io.File;
import java.util.ArrayList;

public class LeitoraEscritoraParadasXMLTest
{

	public static void main(String[] args)
	{
		
		String location = (new File(".")).getAbsolutePath() + "\\src\\hiuller\\data\\parada\\teste.xml";
		String destination = (new File(".")).getAbsolutePath() + "\\temp_data\\saida.xml";

		LeitoraParadasXML leitora = new LeitoraParadasXML(location);
		ArrayList<Parada> lista = leitora.parse();
		
		for(Parada p:lista)
			System.out.println(p);
		
		
		Parada nova = new Parada(Equip.CONV_AC2, 10, 4, 30, 24, "Esta parada é uma brasa", true);
		lista.add(nova);
		
		EscritoraParadasXML escritora = new EscritoraParadasXML();
		escritora.assembleTree(lista);
		escritora.Save(destination);
	
		LeitoraParadasXML leitora2 = new LeitoraParadasXML(destination);
		lista = leitora2.parse();

		for(Parada p:lista)
			System.out.println(p);
	}

}
