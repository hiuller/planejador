package hiuller.tests;

import hiuller.gui.components.ParadaLabel;
import hiuller.objectmodel.Equipamentos;
import hiuller.objectmodel.Equipamentos.Equip;
import hiuller.objectmodel.Parada;

import javax.swing.JFrame;

public class ParadaLabelTest
{

	public static void main(String[] args)
	{
		Equip equipamento = Equip.AF1;		
		int dia = 2;
		int duration = 50;
		
		Parada parada = new Parada(equipamento, dia, 8, 0, duration, "", false);
		ParadaLabel label = new ParadaLabel(dia, Equipamentos.getEquipGroup(equipamento), new Parada[]{parada});
		
		JFrame frame = new JFrame("ParadaLabelTest");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.add(label);
		frame.setSize(150, 150);
		
		frame.setLocationRelativeTo(null);
		
		frame.setVisible(true);

	}

}
