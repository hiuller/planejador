package hiuller.objectmodel;

import gth.ColumnGroup;
import gth.GroupableTableColumnModel;
import gth.GroupableTableHeader;
import hiuller.gui.Constants;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

public class ListaDias extends JTable
{

	private static final long serialVersionUID = 1L;
	private int numDias;
	//private final int shift;
	
	private static final String[] labels = new String[]{
		" ", " "
	};
	
	private static final String[] dias = new String[]{
		"dom", "seg", "ter", "qua", "qui", "sex", "sab"  
	};
	
	//private JTable table;
	
	
	public ListaDias(int numDias, final int shift)
	{
		super();
		
		this.numDias = numDias;
		//this.shift = shift;
		
		TableModel modelo = new TableModel()
		{
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
				return labels.length;
			}

			@Override
			public String getColumnName(int columnIndex)
			{
				return labels[columnIndex];
			}

			@Override
			public int getRowCount()
			{
				return ListaDias.this.numDias;
			}

			@Override
			public Object getValueAt(int rowIndex, int columnIndex)
			{
				if(columnIndex == 0)
					return rowIndex+1; 
				
				if(columnIndex == 1)
					return dias[(shift+rowIndex)%7];
				
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
		};
		
//		JTable table = new JTable();
//		
//		table.setColumnModel(new GroupableTableColumnModel());
//		table.setTableHeader(new GroupableTableHeader((GroupableTableColumnModel)table.getColumnModel()));
//		table.setModel(modelo);
//		
//		GroupableTableColumnModel cm = (GroupableTableColumnModel)table.getColumnModel();
//		
//		ColumnGroup grupo = new ColumnGroup("Data");
//		grupo.add(cm.getColumn(0));
//		grupo.add(cm.getColumn(1));
//		cm.addColumnGroup(grupo);
//		
//        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//        table.setGridColor(Color.gray);

		
		
// group headers		
		this.setColumnModel(new GroupableTableColumnModel());
		this.setTableHeader(new GroupableTableHeader((GroupableTableColumnModel)this.getColumnModel()));
		this.setModel(modelo);
		
		this.setFont(Constants.TABLE_FONT);
		this.getTableHeader().setFont(Constants.TABLE_HEADER_FONT);
		this.setRowHeight(Constants.TABLE_ROW_HEIGHT);

		// mark the weekends
		this.getColumnModel().getColumn(1).setCellRenderer(new TableCellRenderer()
		{
			
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value,
					boolean isSelected, boolean hasFocus, int row, int column)
			{
				String day = (String) value;
				Component result = new DefaultTableCellRenderer().getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

				if(day.equals("sab") | day.equals("dom"))
				{
					result.setBackground( new Color(255, 127, 39) );
				}
				
				// http://stackoverflow.com/questions/8658175/text-align-in-jlabel
				JLabel r = (JLabel) result;
				r.setHorizontalAlignment(SwingConstants.CENTER);
				
				return r;
			}
		});
		// end of marking the weekends		
		
		GroupableTableColumnModel cm = (GroupableTableColumnModel)this.getColumnModel();
		
		ColumnGroup grupo = new ColumnGroup("Data");
		grupo.add(cm.getColumn(0));
		grupo.add(cm.getColumn(1));
		cm.addColumnGroup(grupo);
		
        this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        this.setGridColor(Color.gray);
        
        adjustColumnSizes(0, 2);
        adjustColumnSizes(1, 2);
   	}

    //http://stackoverflow.com/questions/13013989/how-to-adjust-jtable-columns-to-fit-the-longest-content-in-column-cells
	public void adjustColumnSizes(int column, int margin) 
    {
        DefaultTableColumnModel colModel = (DefaultTableColumnModel) this.getColumnModel();
        TableColumn col = colModel.getColumn(column);
        int width;

        TableCellRenderer renderer = col.getHeaderRenderer();
        if (renderer == null) {
            renderer = this.getTableHeader().getDefaultRenderer();
        }
        Component comp = renderer.getTableCellRendererComponent(this, col.getHeaderValue(), false, false, 0, 0);
        width = comp.getPreferredSize().width;

        for (int r = 0; r < this.getRowCount(); r++) {
            renderer = this.getCellRenderer(r, column);
            comp = renderer.getTableCellRendererComponent(this, this.getValueAt(r, column), false, false, r, column);
            int currentWidth = comp.getPreferredSize().width;
            width = Math.max(width, currentWidth);
        }

        width += 2 * margin;

        col.setPreferredWidth(width);
        col.setWidth(width);
    }
	
	
	

}
