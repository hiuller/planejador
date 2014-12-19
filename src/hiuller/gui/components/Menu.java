package hiuller.gui.components;

import hiuller.gui.MainWindow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;

public class Menu extends JMenuBar implements ActionListener
{

	private static final long serialVersionUID = 1L;
	
	private MainWindow owner;
	
	private JMenu
		arquivo, inserir, configurar, simular, exibir, ajuda;
	
	private JMenuItem
		novo, abrir, salvar, bloquear, exportar, pdf, sair, paradas, demanda, parametros, calcular, limpar, testar,
		historico, saldo, lst_parad, prod_dec, rest_dec, console,
		sobre;
	
	private boolean
		isOpen = false;
	
	private void initialize(JMenu menu, String name, char mnemonic, JComponent[] itens)
	{
		menu = new JMenu(name);
		if(mnemonic != ' ')
			menu.setMnemonic(mnemonic);
		
		for(int i=0; i<itens.length; i++)
			menu.add(itens[i]);
		
		this.add(menu);
	}
	
	private JMenuItem initializeItem(String name, char mnemonic, String shortcut)
	{
		/*
		 * the shortcut is a string of length two where the first letter is
		 * either A or C (which stands for 'ALT' or 'CTRL' and the second
		 * character is the letter to be used along with the first key
		 */
		
		JMenuItem item = new JMenuItem(name);
		if(mnemonic != ' ')
			item.setMnemonic(mnemonic);		
		
		if( shortcut != null)
		{
			int modifier = 0;
			
			if( shortcut.startsWith("A") )
				modifier = InputEvent.ALT_MASK;
	
			if( shortcut.startsWith("C") )
				modifier = InputEvent.CTRL_MASK;
			
			item.setAccelerator(KeyStroke.getKeyStroke(shortcut.charAt(1), modifier));
		}
				
		// sets the action listener
		item.addActionListener(this);
		
		return item;
	}
	
	public void setOpen( boolean isOpen )
	{
		this.isOpen = isOpen;
		updateState();
	}
	
	public void updateState()
	{
		parametros.setEnabled( isOpen );
		paradas.setEnabled( isOpen );
		demanda.setEnabled( isOpen );
		salvar.setEnabled( isOpen );
		bloquear.setEnabled( isOpen );
		exportar.setEnabled( isOpen );
		pdf.setEnabled( isOpen );
		calcular.setEnabled( isOpen );
		limpar.setEnabled( isOpen );
		saldo.setEnabled( isOpen );
		historico.setEnabled( isOpen );
		lst_parad.setEnabled( isOpen );
		prod_dec.setEnabled( isOpen );
		rest_dec.setEnabled( isOpen );
		testar.setEnabled( isOpen );
		
		// not implemented yet
		JMenuItem[] naoImplementados = new JMenuItem[]
		{
			historico, saldo, prod_dec, rest_dec
		};
		for(JMenuItem i : naoImplementados)
			i.setEnabled(false);
	}
	
	public Menu(MainWindow owner)
	{
		super();
		
		this.owner = owner;
		
		novo     =initializeItem ( "Novo", 'N', "CN");
		abrir    =initializeItem ( "Abrir...", 'A', "CA");
		salvar   =initializeItem ( "Salvar...", 'V', null);
		bloquear =initializeItem ( "Proteger...", 'P', null);
		// FIXME exportar =initializeItem ( "Exportar para Excel...", 'X', null);
		exportar =initializeItem ( "Exportar dados crus para temp_data", 'X', null);
		pdf      =initializeItem ( "Criar PDF...", 'f', null);
		sair     =initializeItem ( "Sair",  'S', "AX");		
		initialize( arquivo, "Arquivo", 'A', new JComponent[]{novo, abrir, salvar, bloquear, new JSeparator(), exportar, pdf, new JSeparator(), sair} );
		
		paradas = initializeItem("Paradas...", 'P', null);
		demanda = initializeItem("Demanda...", 'D', null);
		initialize( inserir, "Inserir", 'I', new JComponent[]{paradas, demanda});
		
		parametros = initializeItem("Parâmetros...", 'P', null);		
		initialize( configurar, "Configurações", 'C', new JComponent[]{parametros});
		
		calcular = initializeItem("Calcular", 'C', null);
		limpar = initializeItem("Limpar", ' ', null);
		testar = initializeItem("Testar...", 'T', null);
		initialize(simular, "Simular", 'L', new JComponent[]{calcular, limpar, new JSeparator(), testar});
		
		historico = initializeItem("Histórico de alterações", 'H', "CH");
		lst_parad = initializeItem("Lista de Paradas", 'P', null);
		saldo = initializeItem("Evolução do saldo", ' ', null);
		prod_dec = initializeItem("Produção por decêncio", ' ', null);
		rest_dec = initializeItem("Restritivos por decêndio", ' ', null);
		console = initializeItem("Console", 'C', "CG");
		initialize(exibir, "Exibir", 'x', new JComponent[]{historico, lst_parad, saldo, new JSeparator(), prod_dec, rest_dec, new JSeparator(), console});
		
		sobre = initializeItem("Sobre...", 'S', null);		
		initialize( ajuda, "Ajuda", 'u', new JComponent[]{sobre});
		
		// state management
		updateState();
	}
	
	// action implementation

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if( e.getSource() == abrir )
		{
			JFileChooser chooser = new JFileChooser();
			chooser.setCurrentDirectory( new File(".\\temp_data") );
			FileFilter filtro = new FileFilter()
			{
				
				@Override
				public String getDescription()
				{
						return "Plano da Aciaria (*.pla)";
				}
				
				@Override
				public boolean accept(File f)
				{
					if (f.getAbsolutePath().endsWith(".pla"))
						return true;
					
					return false;
				}
			};
			chooser.setFileFilter(filtro);
			
			int result = chooser.showOpenDialog(null);
			
			if ( result == JFileChooser.APPROVE_OPTION )
			{
				String location = chooser.getSelectedFile().getAbsolutePath();
				owner.openFile(location); 
				
			} // else let's ignore it
			
		}
		
		if( e.getSource() == salvar )
		{
			
			if(isOpen)
			{ // in this case, we already have a file location and will just save changes to disk
				final String location = owner.getCurrentFileName();
				// delete current file
				boolean result = new File(location).delete();
				// write all again
				if(result == true)
				{	
					Thread t = new Thread(new Runnable()
					{
						public void run()
						{
							owner.writeToFile(location);
						}
					});
					t.start();	
				}
				else
					JOptionPane.showMessageDialog(null, "Falha ao tentar salvar.");
			} else {
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory( new File(".\\temp_data") );
				FileFilter filtro = new FileFilter()
				{
					
					@Override
					public String getDescription()
					{
						return "Plano da Aciaria (*.pla)";
					}
					
					@Override
					public boolean accept(File f)
					{
						if (f.getAbsolutePath().endsWith(".pla"))
							return true;
						
						return false;
					}
				};
				chooser.setFileFilter(filtro);
				
				int result = chooser.showSaveDialog(null);
				
				if ( result == JFileChooser.APPROVE_OPTION )
				{
					String location = chooser.getSelectedFile().getAbsolutePath();
					if(!location.endsWith(".pla"))
						location += ".pla";
					
					owner.writeToFile(location);
					
				} // else let's ignore it
			}
		}
		
		if( e.getSource() == sair )
		{
			System.exit(-1);
		}
		
		if( e.getSource() == sobre)
		{			
		
			String message = "Planejamento da produção mensal das Aciarias.\nVersão 0.1 iniciada em 22/jul/14\npor Hiuller C. Araujo";
			JOptionPane.showMessageDialog(null, message, "Sobre", JOptionPane.INFORMATION_MESSAGE,
					new ImageIcon(this.getClass().getClassLoader().getResource("calendar.png")));
		}
		
		if( e.getSource() == parametros )
		{
			owner.showEditorParametros();
		}

		if( e.getSource() == novo )
		{
			owner.newPlano();
		}
		
		if( e.getSource() == paradas )
		{
			owner.inserirParada();
		}
		
		if( e.getSource() == demanda )
		{
			owner.inserirDemanda();
		}
		
		if( e.getSource() == calcular )
		{
			try 
			{
				owner.calcular();
			} catch (Exception exception)
			{
				String message = exception.getMessage();
				String title = "Exception when trying to call owner.calcular()";
				JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
			}
		}
		
		if( e.getSource() == lst_parad )
		{
			owner.listarParadas();
		}
		
		if( e.getSource() == console )
		{
			owner.showConsole();
		}
	
		if( e.getSource() == testar)
		{
			owner.testar();
		}
		
		if( e.getSource() == pdf )
		{
			owner.exportPDF();
		}
		
		if( e.getSource() == limpar)
		{
			owner.limpar();
		}
		
		if( e.getSource() == exportar )
		{
			owner.exportar();
		}
	}

}
