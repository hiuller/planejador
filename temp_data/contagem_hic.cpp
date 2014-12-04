#include <iostream>
#include <stdio.h>

using namespace std;

int main()
{
	int count = 0;
	// o maximo de corridas e 155 e o minimo é zero, vamos fazer um histograma
	int histograma[156];
	for(int i=0; i<156; i++)
		histograma[i]=0;
		
	for(int x0=0; x0<=31; x0++)
		for(int x1=0; x1<=31; x1++)
			for(int x2=0; x2<=31; x2++)
				if(x0+x1+x2==31)
				{
					int producao = x0*0+x1*4+x2*5;
					if(producao==31) // exibir resultados para esta demanda
					{
						printf("%2d %2d %2d -> (%3d)\n", x0, x1, x2, producao);
					}
					histograma[producao]++;
					count++;
				}
		
	int num_solucoes = 0;
	if(true)
	{
		for(int i=0; i<156; i++)
			if(histograma[i]!=0)
			{
				//printf("[%3d] = %d\n", i, histograma[i]);
				num_solucoes++;
			}
	}
	printf("O total de combinacoes e %d e o numero de solucoes e %d\n", count, num_solucoes);
	
	return(0);
}
