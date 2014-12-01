package research;

public class BranchingTest
{

	public static void main(String[] args)
	{
		// my problem is to explore the size of the problem of
		// scheduling 388 DR heats into 31 days, where the number of
		// heats per day can range from 0 to 16.
		
		System.out.printf("Iniciando Branch & Bound...\n");

		branch("", dias, plano);
		
		System.out.printf("The total number of combinations is %d.\n", count);
		
	}

	static int plano = 4,
			   dias = 4,
			   max = 2,
			   count = 0;
	
	public static void branch(String str, int days_left, int total_left)
	{
		if(total_left <= 0) // base case
		{
			if(str.length() < dias)
			{
				int completar = dias - str.length();
				for(int i=0; i<completar; i++)
					str += 0;
			}
				
			count++;			
			System.out.println(str);
			
			return;
		}
		
		int min = 0, max2 = max;
		int folga = max*days_left - total_left;
				
		if(folga==0)
			min=max;
		else
			if(folga<0)
				return;
			else
				if(folga==0)
					min=1;
		
		if(max2>total_left)
			max2 = total_left;
			
		
		for(int i=min; i<=max2; i++)
			branch(str + i, days_left-1, total_left-i);
	
	}

}
