package hiuller.gui.utils;

import hiuller.gui.Constants;
import hiuller.objectmodel.Plano;

import org.jgap.FitnessFunction;
import org.jgap.IChromosome;

public class PlanoFitness extends FitnessFunction
{
	private static final long serialVersionUID = 1L;
	private Plano plano;
	
	public PlanoFitness(Plano plano)
	{
		this.plano = plano;
	}
	
	public void send(IChromosome subject)
	{
		evaluate(subject);
	}
	
	public SolutionProvider getProvider(IChromosome subject)
	{
		int[] values = new int[ plano.getNumDias()*4 ];
		for(int i=0; i<values.length; i++)
		{
			values[i] = ((Integer) subject.getGene(i).getAllele()).intValue();
		}
		return new Solution(values);
	}
	
	@Override
	protected double evaluate(IChromosome a_subject)
	{
		// TODO Auto-generated method stub
		
		int[] values = new int[ plano.getNumDias()*4 ];
		for(int i=0; i<values.length; i++)
		{
			values[i] = ((Integer) a_subject.getGene(i).getAllele()).intValue();
		}
		
		// steps
		// 1	create a solution provider with the data from a_subjet
		Solution solution = new Solution(values);				
		// 2    pass it to the plano
		plano.setSolutionProvider(solution);
		// 3    plano.calcular();
		plano.calcular(false);
		// 4    get the fitness value
		double result = plano.getFitness();
		// 5    clear the solution provider
		plano.clearSolutionProvider();
		// 6	return the fitness value		
		return result;
	}

}

class Solution implements SolutionProvider
{
	
	private int[] values;
	
	public Solution(int[] values)
	{
		this.values = values;
	}
	
	private int[] getChunk(int chunk)
	{
		int chunkSize = values.length/4;
		int[] result = new int[chunkSize];
		
		int start = (chunk-1)*chunkSize;
		
		for(int i=0; i<chunkSize; i++)
			result[i] = values[i+start];
		
		return result;
	}
	private int sumChunk(int chunk)
	{
		int sum = 0;
		int chunkSize = values.length/4;
		int[] vals = getChunk(chunk);
		
		for(int i=0; i<chunkSize; i++)
			sum += vals[i];
		
		return sum;
	}
	

	@Override
	public int[] corridas_ac1()
	{		
		return new TempData().corridas_ac1();
	}

	@Override
	public int[] corridas_ac2()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] restr_hic()
	{
		int[] hic = getChunk(1);
		int[] result = new int[hic.length];
		
		for(int i=0; i<hic.length; i++)
			result[i] = hic[i]*Constants.TAM_SERIE_HIC;
				
		return result;
	}

	@Override
	public int[] restr_dr()
	{
		return getChunk(2);
	}

	@Override
	public int[] restr_rh()
	{
		return getChunk(3);
	}

	@Override
	public int[] restr_fp1()
	{
		return new TempData().restr_fp1();
	}

	@Override
	public int[] restr_fp2()
	{
		return getChunk(4);
	}

	@Override
	public int hic_total()
	{
		return sumChunk(1);
	}

	@Override
	public int dr_total()
	{
		return sumChunk(2);
	}

	@Override
	public int rh_total()
	{
		return sumChunk(3);
	}

	@Override
	public int fp2_total()
	{
		return sumChunk(4);
	}
	
}