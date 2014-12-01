package hiuller.gui.dialogs;

import hiuller.objectmodel.Equipamentos;
import hiuller.objectmodel.Equipamentos.Equip;
import hiuller.objectmodel.Parada;
import hiuller.objectmodel.Plano;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class InserirParada extends JDialog implements ActionListener
{
	private static final long serialVersionUID = 1L;

	private Plano owner;
	
	private JLabel
		lab1, lab2, lab3, lab4;
	private JCheckBox muda;
	private JComboBox<String>
		equi;
	private JComboBox<Integer>
		data;
	private JTextField
		inic, dura;
	private JScrollPane
		box;
	private JTextArea
		area;
	private JButton
		ok, cancel;
	
	public InserirParada(Plano owner)
	{
		super((JDialog) null, "Inserir parada", true);
		
		this.owner = owner;
		
		this.setIconImage( new ImageIcon( this.getClass().getClassLoader().getResource("gantt.png") ).getImage() );
		
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
	// layout management
			lab1 = new JLabel("Equipamento:");
			lab2 = new JLabel("Data");
			lab3 = new JLabel("Duração (h)");
			lab4 = new JLabel("Inicio (HH:MM)");
			equi = new JComboBox<String>( Equipamentos.names );
			
			muda = new JCheckBox("Fixar nesta data");
			muda.setSelected(false);
			
			Vector<Integer> datas = new Vector<Integer>();
			for(int i=0; i<owner.getNumDias(); i++)
				datas.add(new Integer(i+1));			
			data = new JComboBox<Integer>(datas);
			
			inic = new JTextField("08:30");
			inic.setHorizontalAlignment(JTextField.CENTER);
			
			dura = new JTextField("12");
			dura.setHorizontalAlignment(JTextField.CENTER);
			
			Border nota = BorderFactory.createTitledBorder(" Nota ");
			
			area = new JTextArea("", 5, 25);
			area.setLineWrap(true);			
			
			box = new JScrollPane(area);
			box.setBorder(nota);
			
			ok = new JButton("Inserir");
			ok.addActionListener(this);
			cancel = new JButton("Cancelar");
			cancel.addActionListener(this);
			
			// let's do some group layout
			GroupLayout layout = new GroupLayout(this.getContentPane());
			
			layout.setAutoCreateContainerGaps(true);
			layout.setAutoCreateGaps(true);
			
			layout.setVerticalGroup(
					layout.createSequentialGroup()
						.addComponent(lab1)
						.addComponent(equi)
						.addComponent(lab2)
						.addGroup(layout.createParallelGroup().addComponent(data).addComponent(muda))
						.addGroup(layout.createParallelGroup()
								.addGroup(layout.createSequentialGroup().addComponent(lab4).addComponent(inic))
								.addGroup(layout.createSequentialGroup().addComponent(lab3).addComponent(dura))
						)
						.addComponent(box)
						.addGroup(layout.createParallelGroup().addComponent(ok).addComponent(cancel))
			);

			layout.setHorizontalGroup(
					layout.createParallelGroup()
						.addComponent(lab1)
						.addGroup(layout.createSequentialGroup().addComponent(equi).addGap(180))
						.addComponent(lab2)
						.addGroup(layout.createSequentialGroup().addComponent(data).addComponent(muda).addGap(120))
						.addGroup(layout.createSequentialGroup()
								.addGroup(layout.createParallelGroup().addComponent(lab4).addComponent(inic))
								.addGroup(layout.createParallelGroup().addComponent(lab3).addComponent(dura))
								.addGap(150)
								)						
						.addComponent(box)
						.addGroup(layout.createSequentialGroup().addComponent(ok).addComponent(cancel))
			);

			//layout.linkSize(data, dura);
			
			this.setLayout(layout);
			
	// end of layout management
		
		this.pack();
		
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if( e.getSource() == cancel)
		{
			this.dispose();
		}
		
		if( e.getSource() == ok )
		{
			// TODO check for inconsistency in the values
			
			// TODO hard code
			Equip equipamento = Equipamentos.createEquip( equi.getSelectedIndex() );
			int dia = ( (Integer) this.data.getSelectedItem() ).intValue();
			
			Calendar c = null;
			try
			{
				c = Calendar.getInstance();
				c.setTime( new SimpleDateFormat("HH:mm").parse( inic.getText() ) );
			} catch (ParseException f)
			{
				fail();
				return;
			}
			
			int hora = c.get(Calendar.HOUR_OF_DAY);
			int minuto = c.get(Calendar.MINUTE);
			int duracao = -1;
			try {
				duracao = (Integer.valueOf( dura.getText() ).intValue());
			} catch (NumberFormatException f) 
			{
				fail();
				return;
			}
			String nota = area.getText();
			boolean isFixed = muda.isSelected();
			
			Parada parada = new Parada(equipamento, dia, hora, minuto, duracao, nota, isFixed);
				
			owner.inserirParada(parada);
			
			this.dispose();
		}
	}
	
	private void fail()
	{
		String title = "Entrada inválida";
		String message = "Algum campo do formulário contém entrada inválida. ";
		JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
		this.dispose();
	}
}
