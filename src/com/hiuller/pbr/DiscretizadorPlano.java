package com.hiuller.pbr;

import java.util.ArrayList;

public class DiscretizadorPlano
{
	public static int calcNumCombinacoes(int demanda, int numDias, int[] base)
	{
		int numValores = base.length; // numero de fatores, estou chamando isso de base

		int[] x = new int[numValores];  // um vetor para as solucoes		
		ArrayList<int[]> solucoes = new ArrayList<int[]>(); // uma lista para todas as soluções válidas, uma alternativa 
															// seria retornar somente a primeira solução e fingir que só existe uma...;p
					


		return solucoes.size();
	}
	
	// funcao recursiva de busca por solucoes (fatoracao sobre a base)
	private boolean procure(int demanda, int numDias, int[] base)
	{
		//if(sum == demanda)
			return true;
		//else
			//return procure(demanda, numDias, base)
	}
	
	public static void main(String[] args)
	{
	/*
		int plano_fp1 = 133;
		int[] vlr_discretos = new int[]{0, 5, 7};
		
		int numComb = calcNumCombinacoes(plano_fp1, vlr_discretos);
		
		System.out.printf("Numero de combinações para fazer 133 corridas com valores {0, 5, 7} = %d", numComb);
	*/
		// setup do problema
		
		int demanda = 31;
		int numDias = 31;
		int tetoDemanda = 155;
		int[] valores = new int[]{0, 4, 5};
		
		int numComb = calcNumCombinacoes(demanda, numDias, valores);
		System.out.printf("Foi encontrada %d solução(ões) para a demanda 31 com base {0, 4, 5}\n", numComb);

	}
}
