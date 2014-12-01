package hiuller.tests;

import hiuller.objectmodel.ListaDias;

import javax.swing.JFrame;

public class ListaDiasTest extends JFrame
{

	private static final long serialVersionUID = 1L;

	public static void main(String[] args)
	{
		new ListaDiasTest();
	}
	
	public ListaDiasTest()
	{
		super("ListaDiasTest()");
		this.add(new ListaDias(31, 0));
		this.pack();
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

}
