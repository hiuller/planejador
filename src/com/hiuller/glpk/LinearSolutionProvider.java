package com.hiuller.glpk;

import hiuller.gui.utils.SolutionProvider;

public class LinearSolutionProvider implements SolutionProvider
{

	private static final double
		prodh_hic = 151.0,
		prodh_dr  = 200.0,
		prodh_rh  = 248.0,
		prodh_fp2 = 220.0,
		prodh_co2 = 239.0, // comum aciaria 2
		prodh_co1 = 147.0, // comum aciaria 1
		prodh_fp1 = 130.0;
	
	private static final double
		ue_conv_ac2 = 0.69,
		ue_mlc4     = 0.98;
	
	@Override
	public int[] corridas_ac1()
	{
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] restr_dr()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] restr_rh()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] restr_fp1()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] restr_fp2()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int hic_total()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int dr_total()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int rh_total()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int fp2_total()
	{
		// TODO Auto-generated method stub
		return 0;
	}

}
