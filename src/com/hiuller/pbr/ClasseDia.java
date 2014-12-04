package com.hiuller.pbr;

import hiuller.objectmodel.Plano;

public class ClasseDia
{
	
	public static final int
		normal1		= 0,
		normal2		= 1,
		campanha1	= 2,
		campanha2	= 3,
		ld1			= 4,
		ld2         = 5,
		aciaria1	= 6,
		aciaria2    = 7,
		rh2			= 8,
		inspecao1   = 9,
		inspecao2   =10,
		mlc1		=11,
		mlc2        =12,
		fp2         =13;
	public static final String[] labels = new String[]{
		"Aciaria 1 normal",
		"Aciaria 2 normal",
		"Campanha na Aciaria 1",
		"Campanha na Aciaria 2",
		"Parada de LD na Aciaria 1",
		"Parada de LD na Aciaria 2",
		"Parada total Aciaria 1",
		"Parada total Aciaria 2",
		"RH parado",
		"Inspecao MLC Aciaria 1",
		"Inspecao MLC Aciaria 2",
		"Parada de máquina na Aciaria 1",
		"Parada de máquina na Aciaria 2",
		"FP2 parado"
	};
	public static final int NUM_FAMILIAS = 14; 
	
	private int[] classificacao_ac2,
				  classificacao_ac1,
				  histograma_ac1,
				  histograma_ac2;
	
	public ClasseDia(Plano plano)
	{
				
		double[][] matriz = plano.computeAvailabilityMatrix(false);
		int n = plano.getNumDias();		
		classificacao_ac1 = new int[n];
		classificacao_ac2 = new int[n];
		histograma_ac1    = new int[NUM_FAMILIAS];
		histograma_ac2    = new int[NUM_FAMILIAS];
		
		// using decision tree created by RapidMiner
		for(int i=0; i<n; i++)
		{	
			int classe_ac1 = decisionTreeAc1(matriz[i][5], matriz[i][6], matriz[i][7]);
			classificacao_ac1[i] = classe_ac1;
			histograma_ac1[classe_ac1]++;
			
			int classe_ac2 = decisionTreeAc2(matriz[i][0], matriz[i][1], matriz[i][2], matriz[i][3], matriz[i][4]);
			classificacao_ac2[i] = classe_ac2;
			histograma_ac2[classe_ac2]++;
		}
	}
	
	public void spit()
	{
		System.out.printf("\nAciaria 1\n");
		//for(int i=0; i<classificacao_ac2.length; i++)
		//	System.out.printf("dia %02d = %d\n", i+1, classificacao_ac1[i]);
		for(int i=0; i<NUM_FAMILIAS; i++)
		{
			if(histograma_ac1[i]>0)
				System.out.printf("Família %02d count = %d (%s)\n", i, histograma_ac1[i], labels[i]);
		}
		
		System.out.printf("Aciaria 2\n");
		//for(int i=0; i<classificacao_ac2.length; i++)
		//	System.out.printf("dia %02d = %d\n", i+1, classificacao_ac2[i]);
		for(int i=0; i<NUM_FAMILIAS; i++)
		{
			if(histograma_ac2[i]>0)
				System.out.printf("Família %02d count = %d (%s)\n", i, histograma_ac2[i], labels[i]);
		}					
	}
	
	
	
	private int decisionTreeAc2(double ld, double ob, double fp, double rh, double ml)
	{
		if(ld<=0.896)
			  return aciaria2;
			else
			  if(rh<=0.833)
			    return rh2;
			  else
			    if(fp<=0.5)
			      return fp2;
			    else
			      if(ml<=0.521)
			        return inspecao2;
			      else
			        if(ml<=0.771)
			          return mlc2;
			        else
			          return normal2;
	}
	
	private int decisionTreeAc1(double ld, double fp, double ml)
	{
		if(ld<=0.5)
			return aciaria1;
		else
			return normal1;
	}
}
