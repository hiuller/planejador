package hiuller.objectmodel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;

import javax.swing.JLabel;

public class ValorLabel extends JLabel
{
	private static final long serialVersionUID = 1L;

	private double value;
	private boolean changed;
	private Color canto;
	private static final int CANTO_SIZE = 8;
	private double[] limits;
	
	public ValorLabel(double value, String unit, boolean changed)
	{
		super("");
	
		this.value = value;
		this.changed = changed;
		
		if(value != 0.0)		
				this.setText( String.format("%,.0f %s  ", value, unit) );
						
		this.setHorizontalAlignment( JLabel.RIGHT );	
		
		this.canto = Color.red;
	}
	public ValorLabel(double value, String unit, boolean changed, int decimals)
	{
		this(value, unit, changed);
		
		if(value != 0.0)		
			this.setText( String.format(String.format("%%,.%df %%s  ", decimals), value, unit) );
	}
	public ValorLabel(double value, String unit, boolean changed, double[] limits)
	{
		this(value, unit, changed);
		this.limits = limits;
	}
	
	public double[] getLimites()
	{
		return this.limits;
	}
	
	public double getValue()
	{
		return this.value;
	}
	
	public void setCantoColor(Color canto)
	{
		this.canto = canto;
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		// draw something extra to make changed values more remarkable
		if(changed)
		{
			Graphics2D g2d = (Graphics2D) g;
			Color tempColor = g2d.getColor();
			
			Polygon canto = new Polygon(new int[]{0, CANTO_SIZE, 0}, new int[]{CANTO_SIZE, 0, 0}, 3);
			g2d.setColor(this.canto);
			g2d.fill(canto);
						
			g2d.setColor(tempColor);
		}
	}
	
	public String toString()
	{
		return String.format("%.0f", value);
	}
	
}
