package hiuller.tests;

import hiuller.objectmodel.Equipamentos.Equip;
import hiuller.objectmodel.Parada;

public class ParadaTest
{

	public static void main(String[] args)
	{
		Equip equipamento = Equip.AF1;
		int dia = 2;
		int hora = 8;
		int minuto = 0;
		int duracao = 50;
		
		Parada parada = new Parada(equipamento, dia, hora, minuto, duracao, "", false);
		
		System.out.println( parada );
		System.out.printf("Entre os dias %d e %d.\n", parada.getMinDay(), parada.getMaxDay() );
		
		for(int i=1; i<6; i++)
		{
			double[] range = parada.getRangeDay(i);
			System.out.printf("parada.getRangeDay(%d) := (%.3f, %.3f)\n", i, range[0], range[1]);
		}
		
		Parada parada2 = new Parada(equipamento, dia, hora, minuto, 8, "", false); // 2 h break
		System.out.println(parada2);
		System.out.printf("Entre os dias %d e %d.\n", parada2.getMinDay(), parada2.getMaxDay() );
		double[] range = parada2.getRangeDay(dia);
		System.out.printf("parada.getRangeDay(1) := (%.3f, %.3f)\n", range[0], range[1]);
	}

}
