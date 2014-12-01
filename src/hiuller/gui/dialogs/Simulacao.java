package hiuller.gui.dialogs;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JDialog;

public class Simulacao extends JDialog 
{
	private static final long serialVersionUID = 1L;

	//private Plano owner;
	
	public Simulacao(/*Plano owner*/)
	{
		super((JDialog) null, "Configurar Simulação", true);
		
		// this.owner = owner;
		
		this.setIconImage( new ImageIcon( this.getClass().getClassLoader().getResource("tools.png") ).getImage() );
		
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
	// layout management
			// let's do some group layout
			GroupLayout layout = new GroupLayout(this.getContentPane());
			
			layout.setAutoCreateContainerGaps(true);
			layout.setAutoCreateGaps(true);
			
			layout.setVerticalGroup(
					layout.createParallelGroup()
			);

			layout.setHorizontalGroup(
					layout.createParallelGroup()
			);

			
			this.setLayout(layout);
			
	// end of layout management
		
		this.pack();
		
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

}
