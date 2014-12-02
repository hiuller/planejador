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
		parada1		= 6,
		parada2		= 7,
		paradaRH	= 8,
		inspecao1   = 9,
		inspecao2   =10,
		paradaMLC1  =11,
		paradaMLC2  =12;
	
	private int[] classificacao_ac2;
	
	public ClasseDia(Plano plano)
	{
				
		double[][] matriz = plano.computeAvailabilityMatrix(false);
		int n = plano.getNumDias();		
		classificacao_ac2 = new int[n];
		
		// using decision tree created by RapidMiner
		for(int i=0; i<n; i++)
		{			
			classificacao_ac2[i] = decisionTreeAc2(matriz[i][0], matriz[i][1], matriz[i][2], matriz[i][3], matriz[i][4]);
		}
	}
	
	public void spit()
	{
		for(int i=0; i<classificacao_ac2.length; i++)
			System.out.printf("dia %02d = %d\n", i+1, classificacao_ac2[i]);
	}
	
	
	private int decisionTreeAc2(double ld, double ob, double fp, double rh, double ml)
	{
		if(ld <= 0.167)
			return parada2;
		else
			if(rh <= 0.583)
				return paradaRH;
			else
				if(ld <= 0.5)
					return ld2;
				else
					if(ld <= 0.833)
						return parada2;
					else
						if(fp <= 0.5)
							return paradaRH;
						else
							if(rh <= 0.833)
								return paradaRH;
							else
								if(ml <= 0.521)
									return inspecao2;
								else
									if(ml <= 0.771)
										return paradaMLC2;
									else
										return normal2;
	}
}
