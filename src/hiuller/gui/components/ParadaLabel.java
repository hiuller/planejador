package hiuller.gui.components;

import hiuller.objectmodel.Parada;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

import javax.swing.JLabel;

public class ParadaLabel extends JLabel
{
	private static final long serialVersionUID = 1L;
	
	private int    dia;
	private int  group;
	private Parada[] paradas;
	
	final String[] txtParadas;
	
	private Color[] colors;
	
	public ParadaLabel(int dia, int group, final Parada[] paradas)
	{ // this tells the JLabel which group does it belong
		super(" ");
		
		//FIXME this.setToolTipText( parada.toString() );
// mouse events
		int n = paradas.length > 3 ? 3 : paradas.length;
		txtParadas = new String[ n ];
		for(int i=0; i<n; i++)
			txtParadas[i] = paradas[i].toString();
		
		this.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseEntered(MouseEvent e)
			{
				System.out.println("Mouse entrou");				
			}

			@Override
			public void mouseExited(MouseEvent e)
			{
				System.out.println("Mouse saiu");
			}			
		});
// end mouse events		
		
		this.dia = dia;
		this.group = group;
		this.paradas = paradas;
		
		colors = new Color[]{Color.red, Color.green, Color.blue, Color.cyan, Color.magenta};
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		if(paradas.length == 0)
			return;
		
		// draw the occupation bar
		double margin = 4.0;
		
		int l = paradas.length; // variable width		
		int n = l > 3 ? 3 : l; // draws 3 bars at most

		Dimension d = this.getSize();
		double availWidth = d.getWidth() - (3+1)*margin; // change 3 to n
		double width = availWidth / 3; // change 3 to n for variable number of bins
		
		for(int i=0; i<n; i++)
		{
			Parada parada = paradas[i];
			
			int bin = i;
			
			if(parada.getBinNumber() == -1) // being draw for the first time
			{
				parada.setBinNumber(i);
			} else {
				bin = parada.getBinNumber();
			}
						
			double[] percentages = parada.getRangeDay(this.dia);			
			
			Rectangle2D.Double occupationBar = new Rectangle2D.Double(
					margin*(bin+1) + width*bin, percentages[0]*d.getHeight(), width, percentages[1]*d.getHeight());
			
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor( colors[parada.getBinNumber()] );
			
			g2.fill( occupationBar );
		}
	}
	
	@Override
	public String toString()
	{
		return String.format("ParadaLabel Group %d (day %d)", group, dia);
	}

}
