package hiuller.gui.dialogs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class MonthPicker extends JDialog implements ActionListener
{

	private static final long serialVersionUID = 1L;
	
	private boolean b_hasResult = false;

	private JComboBox<String>
		field1;
	private JLabel 
		lab1, lab2;
	private JSpinner
		field2;
	private JButton
		ok, cancel;
	
	private String[] months = new String[]{
			"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"
	};
	
	public MonthPicker()
	{
		super((JDialog) null, "Plano do mês", true);
		
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		this.setIconImage(new ImageIcon( this.getClass().getClassLoader().getResource("tools.png") ).getImage());
		
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		
	// layout management
		ok = new JButton("Aceitar"); 		ok.addActionListener(this);
		cancel = new JButton("Cancelar");	cancel.addActionListener(this);
		lab1 = new JLabel("Mês");
		lab2 = new JLabel("Ano");
		field1 = new JComboBox<String>(months);
		field1.setSelectedIndex(month);
		field2 = new JSpinner(new SpinnerNumberModel(year, year-1, year+6, 1)); 
		
		// http://stackoverflow.com/questions/6198118/disabling-digit-grouping-in-a-jspinner
		field2.setEditor(new JSpinner.NumberEditor(field2,"#"));
		
		
		GroupLayout layout = new GroupLayout(this.getContentPane());
		this.setLayout(layout); // this was the only piece of code I didn't put by myself
		
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		
		layout.setHorizontalGroup(
				layout.createSequentialGroup()
					.addGroup( layout.createParallelGroup().addComponent(lab1).addComponent(field1).addComponent(ok) )
					.addGroup( layout.createParallelGroup().addComponent(lab2).addComponent(field2).addComponent(cancel) )
		);

		layout.setVerticalGroup(
				layout.createParallelGroup()
					.addGroup( layout.createSequentialGroup().addComponent(lab1).addComponent(field1).addComponent(ok) )
					.addGroup( layout.createSequentialGroup().addComponent(lab2).addComponent(field2).addComponent(cancel) )					
		);

	// end of layout management
		this.pack();
		
		this.setLocationRelativeTo(null);
		
		ok.requestFocus();
		
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if( e.getSource() == ok )
		{
			b_hasResult = true;
		}

		if( e.getSource() == cancel )
		{
			b_hasResult = false;
		}
		
		this.dispose();
	}
	
	public boolean hasResult()
	{
		return b_hasResult;
	}

	public String getTimeString()
	{
		int month = field1.getSelectedIndex() + 1; // because it's zero-based	
		int year  = (int) field2.getValue();		
		return String.format("01/%02d/%4d", month, year);
	}
}
