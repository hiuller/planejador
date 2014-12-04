package com.hiuller.pbr;

public class DiscretizadorPlano
{
	public static int calcNumCombinacoes(int demanda, int tetoDemanda, int numDias, int[] valores)
	{
		int numValores = valores.length;
		
		int count = 0;

		int[] x = new int[numValores];
		for(int i=0; i<numValores; i++)
			x[i] = 0;
		
		int[] histograma = new int[tetoDemanda+1];
		for(int i=0; i<tetoDemanda+1; i++)
			histograma[i]=0;
			
		// atualmente preciso de um loop para cada elemento no conjunto...
/*		
		for(int x0=0; x0<=numDias; x0++)
			for(int x1=0; x1<=numDias; x1++)
				for(int x2=0; x2<=numDias; x2++)
					if(x0+x1+x2==numDias)
					{
						int producao = x0*valores[0]+x1*valores[1]+x2*valores[2];
						if(producao==demanda) // exibir resultados para esta demanda
						{
							System.out.printf("%2d %2d %2d -> (%3d)\n", x0, x1, x2, producao);
						}
						histograma[producao]++;
						count++;
					}
*/
		// o tamanho da matriz é numDias^numValores, muito grande para inicializar um vetor?
		// o caso limite atualmente é 31^6=887.503.681
		for(int i=0; i<numValores; i++)		
			for(x[i]=0; x[i]<=numDias; x[i]++)
			{
				int sum = 0;
				for(int j=0; j<numValores; j++)
					sum += x[j];
				
				if(sum == numDias)
				{
					int producao = 0;
					for(int j=0; j<numValores; j++)
						producao += x[j]*valores[j];
					
					if(producao==demanda)
					{
						histograma[producao]++;
						count++;
					}
				}				
			}

		int num_solucoes = 0;
		if(true)
		{
			for(int i=0; i<=tetoDemanda; i++)
				if(histograma[i]!=0)
				{
					//System.out.printf("[%3d] = %d\n", i, histograma[i]);
					num_solucoes++;
				}
		}
		System.out.printf("O total de combinacoes e %d e o numero de solucoes e %d\n", count, num_solucoes);

		return count;
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
		
		int numComb = calcNumCombinacoes(demanda, tetoDemanda, numDias, valores);
		System.out.printf("Numero de combinações para fazer 31 corridas HIC com valores {0, 4, 5} = %d", numComb);

	}
}
