package hiuller.tests;

import hiuller.gui.dialogs.InserirParada;
import hiuller.objectmodel.Plano;

import java.util.Calendar;

public class InserirParadaTest
{

	public static void main(String[] args)
	{
		Calendar c = Calendar.getInstance();
		Plano plano = new Plano(c.getTime());
		new InserirParada(plano);
	}

}
