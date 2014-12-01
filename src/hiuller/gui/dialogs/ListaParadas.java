package hiuller.gui.dialogs;

import hiuller.data.parada.EscritoraParadasXML;
import hiuller.data.parada.LeitoraParadasXML;
import hiuller.gui.Constants;
import hiuller.gui.utils.Utils;
import hiuller.objectmodel.Parada;
import hiuller.objectmodel.Plano;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.TableModel;

public class ListaParadas extends JDialog
{
	private static final long serialVersionUID = 1L;
	
	private Plano plano;
	
	private JTable table;
	
	private JMenuBar barra;
	private JMenu lista;
	private JMenuItem limpar, exportar, carregar, fechar;
	private JLabel status;
	
	private static final String[] names = new String[]{
		"Equipamento",
		"Inicio",
		"Duração",
		"Final",
		"Fixa",
		"Nota"
	};
	
	public ListaParadas(final Plano plano)
	{ 
		super( (JFrame) null, "Lista de paradas", true );
		
		this.plano = plano;
		
		this.setIconImage( new ImageIcon(this.getClass().getClassLoader().getResource("tools.png")).getImage() );
		
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
	// table management		
		table = new JTable( setupModel() );
		
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
					
					Parada p = plano.getParadas().get(row);
					
					String title = "Remover parada";
					String message = String.format("Tem certeza que deseja remover a\n\t%s?", p.toString());
					
					int result = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION);
					
					if(result == JOptionPane.YES_OPTION)
						plano.getParadas().remove(p);
					
					table.setModel( setupModel() );
					
				}
			}			
		});
		
		this.setLayout(new BorderLayout());
		
		this.add(new JScrollPane(table), BorderLayout.CENTER);		
	// end of table management

// menu configuration
		barra = new JMenuBar();
		lista = new JMenu("Lista");
		limpar = new JMenuItem("Limpar lista de paradas");
		exportar = new JMenuItem("Exportar lista de paradas...");
		carregar = new JMenuItem("Carregar lista de paradas...");
		fechar = new JMenuItem("Fechar");
		
		lista.add(limpar);
		lista.add(exportar);
		lista.add(carregar);
		lista.add(new JSeparator());
		lista.add(fechar);
		barra.add(lista);
		
		fechar.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				dispose();
			}
		});

		lista.addFocusListener(new FocusListener()
		{			
			@Override
			public void focusLost(FocusEvent e)
			{
				status.setText(" perdeu focus ");
			}
			
			@Override
			public void focusGained(FocusEvent e){}
		});
		
		ChangeListener mudanca = new ChangeListener()
		{
			@Override
			public void stateChanged(ChangeEvent e)
			{
				String message = " ";
				if( ((JMenuItem) e.getSource()).isArmed() )
				{
					if(e.getSource() == limpar)
						message = " Remove todas as paradas da lista atual.";
					if(e.getSource() == exportar)
						message = " Exporta as paradas do plano atual para um arquivo que pode ser carregado em outro plano.";
					if(e.getSource() == carregar)
						message = " Substitui as paradas do plano atual pelas paradas de um arquivo separado.";
				}
				
				status.setText(message);
			}			
		};
		
		limpar.addChangeListener(mudanca);
		exportar.addChangeListener(mudanca);
		carregar.addChangeListener(mudanca);

		// implementing the ActionListeners
		exportar.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e)
			{				
				exportar();
			}			
		});
		
		carregar.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e)
			{				
				carregar();
			}			
		});		
		
		limpar.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e)
			{
				limpar();
			}			
		});
		
		this.add(barra, BorderLayout.NORTH);	
		
		status = new JLabel(" ");
		this.add(status, BorderLayout.SOUTH);
// end of menu configuration		
		
		this.setSize(Constants.PARADAS_LIST_WIDTH, Constants.PARADAS_LIST_HEIGHT); //this.pack();
		this.setLocationRelativeTo(null);		
		
		this.setVisible(true);
	}
	
	private int getNumParadas()
	{
		return plano.getParadas().size();
	}
	
	private void exportar()
	{
		String destination = "";
		
		if(plano.getParadas().size() == 0)
		{
			String title = "Falha ao exportar";
			String message = "Não há paradas para serem exportadas";
			JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		// create a dialog
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Exportar Lista de Paradas");
		chooser.setDialogType(JFileChooser.SAVE_DIALOG);
		chooser.setMultiSelectionEnabled(false);
		chooser.setFileFilter(new FileFilter()
		{
			
			@Override
			public String getDescription()
			{				
				return "Arquivo de dados XML";
			}
			
			@Override
			public boolean accept(File f)
			{
				if(f.getAbsolutePath().endsWith(".xml"))
					return true;
				
				return false;
			}
		});
		
		int result = chooser.showSaveDialog(null);
		
		if (result == JFileChooser.APPROVE_OPTION)
		{
			destination = chooser.getSelectedFile().getAbsolutePath();
			EscritoraParadasXML escritora = new EscritoraParadasXML();
			
			escritora.assembleTree(plano.getParadas());
			escritora.Save(destination);			
			
			String title   = "Exportar paradas";
			String message = "Paradas salvas com sucesso!";
			JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
		}
		
	}

	private void carregar()
	{
		String destination = "";
				
		// create a dialog
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Carregar Lista de Paradas");
		chooser.setDialogType(JFileChooser.OPEN_DIALOG);
		chooser.setMultiSelectionEnabled(false);
		chooser.setFileFilter(new FileFilter()
		{
			
			@Override
			public String getDescription()
			{				
				return "Arquivo de dados XML";
			}
			
			@Override
			public boolean accept(File f)
			{
				if(f.getAbsolutePath().endsWith(".xml"))
					return true;
				
				return false;
			}
		});
		
		int result = chooser.showOpenDialog(null);
		
		if (result == JFileChooser.APPROVE_OPTION)
		{
			destination = chooser.getSelectedFile().getAbsolutePath();
			LeitoraParadasXML leitora = new LeitoraParadasXML(destination);

			ArrayList<Parada> paradas = leitora.parse();
			plano.carregarParadas(paradas);
			
			table.setModel( setupModel() );
			
			String title   = "Importar paradas";
			String message = "Paradas importadas com sucesso!";
			JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
		}	
	}
	
	
	private void limpar()
	{
		int result = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja remover todas as paradas do plano atual?", "Limpar Paradas?", JOptionPane.YES_NO_OPTION);
		if( result == JOptionPane.OK_OPTION)
		{
			plano.limparParadas();
			table.setModel( setupModel() );
		} else {
			return;
		}
	}
	
	private TableModel setupModel()
	{
		return new TableModel(){

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
				return getNumParadas();
			}

			@Override
			public Object getValueAt(int rowIndex, int columnIndex)
			{
				if(columnIndex == 0) // equipamentos
					return plano.getParadas().get(rowIndex).getEquipamentoName();

				if(columnIndex == 1) // inicio
					return plano.getParadas().get(rowIndex).getInicioString();

				if(columnIndex == 2) // duração
					return plano.getParadas().get(rowIndex).getDuracao();

				if(columnIndex == 3) // final
					return plano.getParadas().get(rowIndex).getFinalString();
				
				if(columnIndex == 4) // fixa
					return plano.getParadas().get(rowIndex).getFixaString();
				
				if(columnIndex == 5) // nota
					return plano.getParadas().get(rowIndex).getNota();
				
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
	}
}
