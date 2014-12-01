package hiuller.tests;

import hiuller.gui.utils.Utils;

public class MyGaussianTest
{

	public static void main(String[] args)
	{

		for(int i=-9; i<10; i++)
			System.out.printf("z=%d    norm=%.5f\n", i, Utils.myGaussian((double)i));
		
	}

}
