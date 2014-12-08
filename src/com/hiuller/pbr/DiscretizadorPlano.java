package com.hiuller.pbr;

import java.util.ArrayList;


public class DiscretizadorPlano
{
	
	private int numCombinacoes, numSolucoes, demanda;
	private int max;
	private int[] base;
	ArrayList<int[]> solucoes;
	boolean verbose = false;
	int[] histograma;
	
	public DiscretizadorPlano(int demanda, int numDias, int[] base)
	{
		this.base = base;
		this.max = numDias;
		this.demanda = demanda;
		
		//compute results
		solucoes = new ArrayList< int[] >();
		histograma = new int[numDias];
		listar(base.length, new int[base.length]);
	}
	
	public void setVerbose(boolean verbose)
	{
		this.verbose = verbose;
	}
	
	public int getNumCombinacoes()
	{
		return numCombinacoes;
	}
	
	public int getNumSolucoes()
	{
		return numSolucoes;
	}
	
	public ArrayList<int[]> getSolucoes()
	{
		return solucoes;
	}
	
	public void showHistograma()
	{
		for(int i=0; i<this.max; i++)
			System.out.printf("dias > %02d > %d\n", i+1, histograma[i]);
	}
	
	private int sum(int[] partial)
	{
		int result = 0;
		for(int i=0; i<partial.length; i++)
			result+=partial[i];
		return result;
	}
	
	public void print(int[] partial)
	{
		String format = "";
		for(int i=0; i<partial.length; i++)
			format += String.format("%02d-", partial[i]);
		
		format = format.substring(0, format.length()-1);
		
		System.out.println(format);
	}
	
	private int product(int[] partial, int[] base)
	{
		int result = 0;
		assert partial.length == base.length : "Dimensões diferentes entre o partial e a base.";
		for(int i=0; i<base.length; i++)
			result += partial[i]*base[i];
		
		return result;
	}
	
	private int numDiasOperando(int[] partial)
	{
		int result = 0;
		for(int i=0; i<base.length; i++)
			result += base[i] > 0 ? partial[i] : 0;
		
		return result;
	}
	
	private void listar(int level, int[] partial)
	{
		if(level==0)
		{
			numCombinacoes++;
			if(sum(partial)==max)
				if(product(partial, base)==demanda)
				{
					solucoes.add( partial.clone() );
					histograma[numDiasOperando(partial.clone())-1]++;
					if(verbose)
						print(partial);
					numSolucoes++;
				}
		}
		else
			for(int i=0; i<=max; i++)
			{
				int[] temp = partial;
				temp[level-1] = i;
				listar(level-1, temp);
			}
	}

}
