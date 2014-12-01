package hiuller.gui.dialogs;

import hiuller.gui.utils.Utils;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

public class InserirDemanda extends JDialog
{

	private static final long serialVersionUID = 1L;
	
	private JTable table;
	private JScrollPane scroll;
	
	public static final int 
		HIC = 0,
		DR  = 1,
		RH  = 2,
		FP  = 3,
		FP1 = 4;
	
	private final String[] restritivos = new String[]{
			"HIC", "DR", "RH", "FP", "FP Ac1"
	};
	
	private final String[] decendios = new String[]{
		"Restritivo", "01 dec", "02 dec", "03 dec"	
	};
	
	private final int ROW = 5, COL = 3;
	
	private double[][] values = new double[ROW][COL];
	
	public InserirDemanda()
	{
		super((JDialog) null, "Inserir demanda do PCP", true);
		
		this.setIconImage( new ImageIcon( this.getClass().getClassLoader().getResource("gantt.png") ).getImage() );
		
		this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		
		for(int i=0; i<ROW; i++)
			for(int j=0; j<COL; j++)
				values[i][j] = 0.0;
		
	// layout management
		table = new JTable();
		
		Utils.setupTable(table);
				
		TableModel modelo = new TableModel(){

			@Override
			public void addTableModelListener(TableModelListener l){}

			@Override
			public Class<?> getColumnClass(int columnIndex)
			{
				return String.class;
			}

			@Override
			public int getColumnCount()
			{
				return decendios.length;
			}

			@Override
			public String getColumnName(int columnIndex)
			{
				return decendios[columnIndex];
			}

			@Override
			public int getRowCount()
			{
				return restritivos.length;
			}

			@Override
			public Object getValueAt(int rowIndex, int columnIndex)
			{
				if( columnIndex == 0 )
					return restritivos[rowIndex];
				else
					return values[rowIndex][columnIndex-1];
			}

			@Override
			public boolean isCellEditable(int rowIndex, int columnIndex)
			{
				return false;
			}

			@Override
			public void removeTableModelListener(TableModelListener l){}

			@Override
			public void setValueAt(Object aValue, int rowIndex, int columnIndex)
			{
			}
		};
		
		table.setModel(modelo);
		scroll = new JScrollPane(table);		
		
		Border borda = BorderFactory.createTitledBorder(" Número de corridas ");
		scroll.setBorder(borda);
		
		// http://stackoverflow.com/questions/6523974/shrink-jscroll-pane-to-same-height-as-jtable/6524224#6524224
		table.setPreferredScrollableViewportSize(table.getPreferredSize());
		
		table.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if( e.getClickCount() == 2)
				{
					int row = table.rowAtPoint   (e.getPoint());
					int col = table.columnAtPoint(e.getPoint());
					
					String message = String.format("Demanda de %s no %s\n", restritivos[row], decendios[col]);
					String newValue = JOptionPane.showInputDialog(null, message, values[row][col-1]);
					if(newValue != null)
					{
						try {
							
							values[row][col-1] = Double.parseDouble(newValue);;
						
						} catch (NumberFormatException f) {
							
							JOptionPane.showMessageDialog(null, "O formato de número não é válido", "Valor inválido", JOptionPane.ERROR_MESSAGE);
						}
					}

				}
			}
			
		});
		
		// omit zero value
		TableCellRenderer demandaRenderer = new TableCellRenderer()
		{
			
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value,
					boolean isSelected, boolean hasFocus, int row, int column)
			{
				Component c = new DefaultTableCellRenderer().getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				JLabel result = (JLabel) c;
				
				if((double) value == 0.0)
				{
					result.setText("");
				}
				
				return result;				
			}
		};
		for(int c=1;c<4; c++)
			table.getColumnModel().getColumn(c).setCellRenderer(demandaRenderer);
		// end of omit zero value
		
		
		this.setLayout(new BorderLayout());
		
		this.add(scroll, BorderLayout.CENTER);
		
	// end of layout management
		
		this.pack();
		this.revalidate();
		
		this.setLocationRelativeTo(null);
	}

	public void setValues(int row, double[] values)
	{
		if(row >= 0 && row < ROW)
		{
			if(values.length == COL)
			{
				for(int i=0; i<COL; i++)
					this.values[row][i] = values[i];
			}
		}
	}

	public double getDemanda(int restritivo, int decendio)
	{
		if(decendio < 1 || decendio > 3)
			throw new IllegalArgumentException("Decêndio deve estar entre 1 e 3.");
		
		if(restritivo < 0 || restritivo > 4)
			throw new IllegalArgumentException("Restritivo deve estar entre 0 e 4.");
	
		return values[restritivo][decendio-1];
	}
	
	public Iterator<String> getSQLInserts()
	{
		ArrayList<String> result = new ArrayList<String>();
		
		String strFormat = "insert into demanda (aco,decendio1,decendio2,decendio3) values (%d, %d, %d, %d)";
		
		for(int i=0; i<ROW; i++)
		{
				String sql = String.format(strFormat, i, (int) values[i][0], (int) values[i][1], (int) values[i][2]);
				result.add(sql);
		}
		return result.iterator();
	}
}
