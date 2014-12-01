package hiuller.gui.utils;

public interface SolutionProvider
{	
	public int[] corridas_ac1();
	public int[] corridas_ac2();
	public int[] restr_hic();
	public int[] restr_dr();
	public int[] restr_rh();
	public int[] restr_fp1();
	public int[] restr_fp2();
	
	public int hic_total();
	public int  dr_total();
	public int  rh_total();
	public int fp2_total();
}
