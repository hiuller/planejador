package hiuller.tests;

import hiuller.objectmodel.Plano;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.JFrame;

public class PlanoGroupingTest extends JFrame
{

	private static final long serialVersionUID = 1L;

	public static void main(String[] args)
	{
		new PlanoGroupingTest();
	}
	
	
	
	
	public PlanoGroupingTest()
	{
		super("Plano de produção");
		
		Plano plano = null;
		try
		{
			
			plano = new Plano(new SimpleDateFormat("dd/MM/yy").parse("01/02/2014"));
			
		} catch (ParseException e)
		{
			e.printStackTrace();
			System.exit(-1);
		}
		
		this.add(plano);
		
		this.pack();
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.setLocationRelativeTo(null);
		
		this.setVisible(true);
	}

}
