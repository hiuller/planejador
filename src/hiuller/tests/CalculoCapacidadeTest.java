package hiuller.tests;

import hiuller.objectmodel.CalculoCapacidade;

public class CalculoCapacidadeTest
{

	public static void main(String[] args)
	{
		double[] parada_rh = new double[]{
				1.00, 1.00, 1.00, 0.33, 1.00
		};

		int dr =  4;
		int rh = 14;
		int fp = 16;
		
		int y = 48;
		
		int y_bar = CalculoCapacidade.capacidade(dr, rh, fp, parada_rh);
		System.out.printf("Real=%d, calculado=%d\n", y, y_bar);
	}

}
