package hiuller.tests;

public class RecursionTest
{
	// conteúdo da classe RecursionTest.java
	public static void main(String[] args)
	{
		// this is the brute force approach
		counter = 0;
		for(int i=0; i<32; i++)
			for(int j=0; j<32; j++)
				for(int k=0; k<32; k++)
				{
					if(i+j+k==31)
					{
						counter++;
						System.out.printf("%02d-%02d-%02d\n", i, j, k);
					}
				}
		
		System.out.printf("The number of combinations si %d\n", counter);
		
		counter = 0;
		listar(3, 31);
		System.out.printf("The number of combinations si %d\n", counter);
	}
	
	public static int counter;
	
	public static void listar(int level, int max)
	{
		counter++;
		System.out.printf("level=%d; max=%02d\n", level, max);
		if(level==0)
			return;
		else
			if(max==1)
				listar(level-1, 32);
			else
				listar(level-0, max-1);				
	}


}
