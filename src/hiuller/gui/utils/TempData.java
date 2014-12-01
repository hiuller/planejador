package hiuller.gui.utils;

public class TempData implements SolutionProvider
{
/*
 	// agosto
	private static final int[] AC1 = new int[]
	{
		0,24,38,38,38,38,38,41,41,42,42,41,34,44,45,45,45,45,45,45,45,45,45,45,45,45,35,35,40,42,45
	};
*/	
	// outubro
	private static final int[] AC1 = new int[]
	{
		28,28,28,28,28,28,8,0,0,0,28,36,36,8,0,0,28,34,34,34,12,36,36,36,36,36,36,36,36,36,36
	};
	
	private static final int[] AC2 = new int[]
	{
		26,36,43,45,45,45,46,46,46,46,46,43,21,24,40,40,40,47,40,39,39,39,40,40,40,47,18,10,34,40,40
	};
	
	private static final int[] HIC = new int[]
	{
		0,0,0,0,0,0,0,0,0,0,0,0,6,0,0,0,0,0,3,3,3,3,0,0,3,0,0,0,0,0,0
	};

	private static final int[] DR = new int[]
	{
		12,8,8,5,5,5,5,0,4,4,0,12,6,5,15,15,15,0,6,6,6,6,15,15,6,5,0,9,16,15,15
	};
	
	private static final int[] RH = new int[]
	{
		5,0,0,7,7,7,7,19,14,14,33,18,0,15,15,15,15,34,15,12,12,14,15,15,19,14,14,0,15,15,19
	};

	private static final int[] FP2 = new int[]
	{
		0,0,0,6,5,5,5,0,12,12,0,6,0,2,3,3,3,0,1,1,1,1,3,3,3,13,0,0,0,2,2
	};
	
	private static final int[] FP1 = new int[]
	{
		0,0,0,0,0,0,0,0,0,0,0,0,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7
	};


	@Override
	public int[] corridas_ac1()
	{
		return AC1;
	}

	@Override
	public int[] corridas_ac2()
	{
		return AC2;
	}

	@Override
	public int[] restr_hic()
	{
		return HIC;
	}

	@Override
	public int[] restr_dr()
	{
		return DR;
	}

	@Override
	public int[] restr_rh()
	{
		return RH;
	}

	@Override
	public int[] restr_fp1()
	{
		return FP1;
	}

	@Override
	public int[] restr_fp2()
	{
		return FP2;
	}

	@Override
	public int hic_total()
	{
		return 21;
	}

	@Override
	public int dr_total()
	{
		return 244;
	}

	@Override
	public int rh_total()
	{
		return 404;
	}

	@Override
	public int fp2_total()
	{
		return 92;
	}
}
