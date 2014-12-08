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
		
		System.out.printf("\nThe number of combinations si %d\n", counter);
		
		counter = 0;
		listar(3, "");
		System.out.printf("\nThe number of combinations si %d\n", counter);
	}
	
	public static int counter;
	public static final int max = 31;
	
	public static void listar(int level, String partial)
	{
		if(level==0) {
			System.out.printf(partial+'\n'); counter++; }
		else
			for(int i=0; i<max; i++)
			{
				listar(level-1, String.format("%s-%d", partial, i));
			}
	}


}
