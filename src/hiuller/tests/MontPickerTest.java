package hiuller.tests;

import hiuller.gui.dialogs.MonthPicker;

public class MontPickerTest
{

	public static void main(String[] args)
	{

		MonthPicker mp = new MonthPicker();
		if( mp.hasResult() )
		{
			String selecao = mp.getTimeString();
			System.out.println( selecao );
		}
	}

}
