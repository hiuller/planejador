package hiuller.objectmodel;

public class CalculoCapacidade
{
	/*
	 * Estes parâmetros foram obtidos pela utilizacao do programa calculo_capacidade.m
	 * em linguagem MATLAB executado no ambiente Octavew
	 */
	
	private static double   Theta_zero = 11.449460;
	private static double[] Theta      = new double[]{
			28.395283,
			-0.138801,
			 0.660300,
		   -11.051075,
			 1.081846
	};
	private static double[] r          = new double[]{
			-0.194444,
			-0.590502,
			-0.072123
	};
	
	public static int capacidade(int dr, int rh, int fp, double[] disp)
	{
		if(disp.length != 5)
			return -1;
		
		double h = Theta_zero;
		for(int i=0; i<Theta.length; i++)
			h += disp[i]*Theta[i];
		
		double   c = 0;
		double[] p = new double[]{ dr, rh, fp };
		for(int i=0; i<r.length; i++)
			c += r[i]*p[i];
		
		return (h-c) < 0 ? 0 : (int) Math.round(h-c);
	}
}
