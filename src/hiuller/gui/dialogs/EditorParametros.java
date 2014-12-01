package hiuller.gui.dialogs;

import hiuller.gui.Constants;
import hiuller.gui.utils.Utils;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class EditorParametros extends JDialog
{
	private static final long serialVersionUID = 1L;
	
	public static final int NUM_PARAMETROS = 26+1;
	
	private static final String[] names = new String[]
	{
		"Parâmetro", "Valor"
	};
	
	public static final String[] params = new String[] 
	{
		"Perda de gusa", 							// 00
		"Rendimento Ac1", 							// 01
		"Rendimento Ac2",							// 02
		"% Sucata Aciaria 1 - FP", 					// 03
		"% Sucata Aciaria 1 - Comum",				// 04
		"% Sucata Aciaria 2 - HIC",					// 05
		"% Sucata Aciaria 2 - DR",					// 06
		"% Sucata Aciaria 2 - RH",					// 07
		"% Sucata Aciaria 2 - FP",					// 08
		"% Sucata Aciaria 2 - Comum",				// 09
		"Peso corrida Aciaria 1 - FP",				// 10
		"Peso corrida Aciaria 1 - Comum",			// 11
		"Peso corrida Aciaria 1 - Transferência",   // 12
		"Peso corrida Aciaria 2 - HIC",				// 13
		"Peso corrida Aciaria 2 - DR",				// 14
		"Peso corrida Aciaria 2 - RH",				// 15
		"Peso corrida Aciaria 2 - FP",				// 16
		"Peso corrida Aciaria 2 - Comum",			// 17		

		"Produtividade Aciaria 1 - FP",				// 18
		"Produtividade Aciaria 1 - Comum",			// 19
		
		"Saldo de gusa inicial",					// 20
		"Pode reduzir sucata?",
		"Fixar restritivos nos decêndios",
		
		"Produção diária padrão AF1",				// 23
		"Produção diária padrão AF2",				// 24
		"Produção diária padrão AF3",				// 25
		
		"Número de corridas transferência"
		
	};
	
	
	public static final int
		PERDA_GUSA     =  0,
		REND_AC1       =  1,
		REND_AC2       =  2,
		PSUC_AC1_FP1   =  3,
		PSUC_AC1_COMUM =  4,
		PSUC_AC2_HIC   =  5,
		PSUC_AC2_DR    =  6,
		PSUC_AC2_RH    =  7,
		PSUC_AC2_FP    =  8,
		PSUC_AC2_COMUM =  9,
		CM_AC1_FP1     = 10,
		CM_AC1_COMUM   = 11,
		CM_AC1_TRAFS   = 12,
		CM_AC2_HIC     = 13,
		CM_AC2_DR      = 14,
		CM_AC2_RH      = 15,
		CM_AC2_FP2     = 16,
		CM_AC2_COMUM   = 17,
		SALDO_INI      = 20,
		PROD_AF1       = 23,
		PROD_AF2       = 24,
		PROD_AF3       = 25,
		PROD_AC1       = 19;
	
	// those are the standard values, but every saved file will have its own set of values
	public static double[] values = new double[]
	{
		0.0267, 0.9068, 0.9046, 0.12, 0.17, 0.14, 0.13, 0.16, 0.13, 0.18, 74.0, 76.0, 71.0, 163.0, 164.0, 165.0, 170.0, 171.0, 
		44.0, 46.0,
		1800.0, 1.0, 1.0,
		1950.0, 1950.0, 7300.0,
		20
	};
	
	public EditorParametros()
	{ 
		super( (JFrame) null, "Editor de parâmetros", true );				
		
		this.setIconImage( new ImageIcon(this.getClass().getClassLoader().getResource("tools.png")).getImage() );
		
		this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		
	// table management		
		JTable table = new JTable(new TableModel(){

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
				return names.length;
			}

			@Override
			public String getColumnName(int columnIndex)
			{
				return names[columnIndex];
			}

			@Override
			public int getRowCount()
			{
				return params.length;
			}

			@Override
			public Object getValueAt(int rowIndex, int columnIndex)
			{
				if(columnIndex == 0)
					return params[rowIndex];

				if(columnIndex == 1)
					return String.format("%.4f", values[rowIndex]);

				return null;
			}

			@Override
			public boolean isCellEditable(int rowIndex, int columnIndex)
			{
					return false;
			}

			@Override
			public void removeTableModelListener(TableModelListener l){}

			@Override
			public void setValueAt(Object aValue, int rowIndex, int columnIndex){}
			
		});
		
		//table.setFont(new Font("Courier", Font.PLAIN, 13));
		table.setFont(Constants.TABLE_FONT);
		table.getTableHeader().setFont(Constants.TABLE_HEADER_FONT);
		table.setRowHeight(Constants.TABLE_ROW_HEIGHT);
		
		Utils.adjustColumnSizes(table, 0, 2);
		Utils.adjustColumnSizes(table, 1, 2);
		

		table.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if (e.getClickCount() == 2)
				{
					JTable tab = (JTable) e.getSource();
					int row = tab.rowAtPoint(e.getPoint());
					
					String message = String.format("Entre com o novo valor \n\n   <%s>\n", params[row]);
					
					String newValue = JOptionPane.showInputDialog(null, message, values[row]);
					if(newValue != null)
					{
						try {
							
							values[row] = Double.parseDouble(newValue);;
							
						} catch (NumberFormatException f) {
							
							JOptionPane.showMessageDialog(null, "O formato de número não é válido", "Valor inválido", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			}			
		});
		
		this.add(new JScrollPane(table));		
	// end of table management
		
		this.pack();
		this.setLocationRelativeTo(null);			
	}
	
	public void setParameter(int parameter, double newValue)
	{
		values[parameter] = newValue;
	}
	
	public double getParameter(int parameter)
	{
		return values[parameter];
	}
	
	public void showDialog()
	{
		this.setVisible(true);
	}
	
	public Iterator<String> getCSVIterator()
	{
		ArrayList<String> list = new ArrayList<String>();
		for(int i=0; i<NUM_PARAMETROS; i++)
		{
			list.add(String.format("%s,%s\n", params[i], Utils.strDouble( values[i]), 7 ));
		}
		return list.iterator();
	}

}
