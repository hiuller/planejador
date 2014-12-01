package hiuller.gui.components;

import java.awt.BorderLayout;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

public class Console extends JFrame
{
	private static final long serialVersionUID = 1L;

	ArrayList<String> data;
	private JList<String> console;
	
	public Console()
	{
		super("Console");
		
		this.setIconImage( new ImageIcon(this.getClass().getClassLoader().getResource("tools.png")).getImage() );
		
		this.setLayout(new BorderLayout());
				 
		data = new ArrayList<String>();
		console = new JList<String>( createModel() );
		
		console.setFont(new Font("Courier", Font.PLAIN, 13));
		
		this.add(new JScrollPane(console), BorderLayout.CENTER);
		
		this.setSize(600, 400);
		
		this.setLocationRelativeTo(null);
		
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	}
	
	private ListModel<String> createModel()
	{
		return new ListModel<String>(){

			@Override
			public void addListDataListener(ListDataListener l){}

			@Override
			public String getElementAt(int index)
			{
				return data.get(index);
			}

			@Override
			public int getSize()
			{
				return data.size();
			}

			@Override
			public void removeListDataListener(ListDataListener l){}
			
		};
	}
	
	public void print(String str)
	{
		Calendar cal = Calendar.getInstance();
		String modified = String.format("%s - %s", 
				new SimpleDateFormat("dd/MM/YY HH:mm:ss").format(cal.getTime()),
				str);
		
		data.add( modified );
		console.setModel( createModel() );
	}
	
	public void clear()
	{
		data.clear();
	}
	
	public void showConsole()
	{
		this.setVisible(true);
	}
}
