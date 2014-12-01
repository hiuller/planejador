package hiuller.gui.utils;

import hiuller.gui.Constants;
import hiuller.gui.components.ParadaLabel;
import hiuller.objectmodel.Parada;
import hiuller.objectmodel.ValorLabel;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class Utils
{

	public static void setupTable(JTable table)
	{
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setGridColor(Color.gray);
        table.setFont(Constants.TABLE_FONT);
        table.setRowHeight(Constants.TABLE_ROW_HEIGHT);
        table.getTableHeader().setFont(Constants.TABLE_HEADER_FONT);
	}
	
    //http://stackoverflow.com/questions/13013989/how-to-adjust-jtable-columns-to-fit-the-longest-content-in-column-cells
	public static void adjustColumnSizes(JTable table, int column, int margin) 
    {
        DefaultTableColumnModel colModel = (DefaultTableColumnModel) table.getColumnModel();
        TableColumn col = colModel.getColumn(column);
        int width;

        TableCellRenderer renderer = col.getHeaderRenderer();
        if (renderer == null) {
            renderer = table.getTableHeader().getDefaultRenderer();
        }
        Component comp = renderer.getTableCellRendererComponent(table, col.getHeaderValue(), false, false, 0, 0);
        width = comp.getPreferredSize().width;

        for (int r = 0; r < table.getRowCount(); r++) {
            renderer = table.getCellRenderer(r, column);
            comp = renderer.getTableCellRendererComponent(table, table.getValueAt(r, column), false, false, r, column);
            int currentWidth = comp.getPreferredSize().width;
            width = Math.max(width, currentWidth);
        }

        width += 2 * margin;

        col.setPreferredWidth(width);
        col.setWidth(width);
    }
	
	public static TableCellRenderer createParadaRender(final int group, final ArrayList<Parada> paradas)
	{
		
		TableCellRenderer result = new TableCellRenderer(){
	 		@Override
	 		public Component getTableCellRendererComponent(JTable table,
	 				Object value, boolean isSelected, boolean hasFocus, int row,
	 				int column)
	 		{	 			

	 			int day = row+1;
	 			
	 			ArrayList<Parada> paradasGroup = new ArrayList<Parada>();
	 			for(Parada parada : paradas)
	 			{
	 				if( parada.getGroup() == group ) // the parada's from this group	 					
	 				{
	 					if( parada.occursInDay(row+1) ) // is this parada related to this day? - can we have more than one? sure we can	 						
	 						paradasGroup.add(parada);
	 				}
	 			}
	 			
	 			JLabel result = null;
	 			// if there is no parada for this group
	 			if(paradasGroup.size() == 0)
	 			{
	 				result = new JLabel();
	 			}	 			
	 			
	 			result = new ParadaLabel(day, group, paradasGroup.toArray(new Parada[paradasGroup.size()]) );
	 			result.setOpaque(true);

	 			if(isSelected)
				{
					result.setBackground( UIManager.getDefaults().getColor("Table[Enabled+Selected].textBackground") );
					result.setForeground( Color.white );
				}
	 			else
		 			if(day%2 == 0) // if it's even
		 				result.setBackground( UIManager.getDefaults().getColor("Table.alternateRowColor") );
		 			else
		 				result.setBackground( Color.white );

	 			return result;
	 		}     	   
        };
        
        return result;
	}

	public static TableCellRenderer createNumberRenderer(final boolean bold)
	{
		return new TableCellRenderer(){

			@Override
			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus,
					int row, int column)
			{
				ValorLabel result = null;
				if(value == null)
					result = new ValorLabel(0.0, "", false);
				else
					result = (ValorLabel) value;
				
				if(bold)
					result.setFont( Constants.TABLE_FONT_BOLD );
				else
					result.setFont( Constants.TABLE_FONT );

				// choose the background color myself
				result.setOpaque(true);
				if(isSelected)
				{
					result.setBackground( UIManager.getDefaults().getColor("Table[Enabled+Selected].textBackground") );
					result.setForeground(Color.white);
					
					if(result instanceof ValorLabel)
						result.setCantoColor(Color.green);
					
				} else {
					
					if(result instanceof ValorLabel)
						result.setCantoColor(Color.red);

					if( (row+1)%2 == 0) // if it's even
						result.setBackground( UIManager.getDefaults().getColor("Table.alternateRowColor") );
					else
						result.setBackground( Color.white );
				}

				// special color for totals
				Calendar cal = Calendar.getInstance();
				cal.setTime(Constants.MONTH);
				int maxDay = cal.getMaximum(Calendar.DAY_OF_MONTH);
				
				if( row+1>maxDay && !isSelected ) // is totals and not selected
					result.setBackground( Color.yellow );
				
				// checking if it has limits
				if(result instanceof ValorLabel)
					if( result.getLimites() != null)
					{
						double[] limits = result.getLimites();
						double dvalue = result.getValue();
						if( dvalue < limits[0] || dvalue > limits[1] )
						{
							result.setBackground( Color.red );
							result.setForeground(Color.yellow);
						}
							
					}
				
				return result;
			}
			
		};
	}

	public static void manageException(Exception e)
	{
		// TODO Auto-generated method stub
		
	}
	
	public static String strDouble(double d)
	{
		return String.format("%.5f", d).replace(',', '.');
	}
	public static String strDouble(double d, int decimalPlaces)
	{
		String format = String.format("%%.%df", decimalPlaces);
		return String.format(format, d).replace(',', '.');
	}
	
	public static double myGaussian(double z)
	{
		double displacement = 10.0; //0.873012728131518;
		double desvio = 0.8;
		return 1 * (displacement + (1/(desvio*Math.sqrt(2*Math.PI))) *Math.exp(-Math.pow(z, 2)/(2*Math.pow(desvio, 2))));
	}
}
