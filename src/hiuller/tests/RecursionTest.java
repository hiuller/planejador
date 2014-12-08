package hiuller.tests;

import java.util.ArrayList;

import com.hiuller.pbr.DiscretizadorPlano;

public class RecursionTest
{

	public static void main(String[] args)
	{	
		int[] base = new int[]{0, 2, 4, 8};		
		int numDias = 31;
		int demanda = 166;
		
		DiscretizadorPlano dp = new DiscretizadorPlano(demanda, numDias, base);
		System.out.printf("The number of ways to attain the demand is %d out of a total of %d.\n", dp.getNumSolucoes(), dp.getNumCombinacoes());
		
		ArrayList<int[]> solucoes = dp.getSolucoes();
		for(int[] e : solucoes)
			dp.print(e);
		
		dp.showHistograma();
	}


}
