package hiuller.objectmodel;

public class Equipamentos
{
	
	public static enum Equip {
		AF1, AF2, AF3, ACIARIA_2, ACIARIA_1, CONV_AC2, CONV_AC1, FP1, FP2, CAS, RH2, RH3, MLC1, MLC2, MLC3, MLC4
	}
	
	// groups
	// 0 - AF
	// 1 - primary
	// 2 - secondary
	// 3 - casting
	// 4 - primary+secondary+casting	
	public static final int[] groups = new int[]
	{
		0, 0, 0, 4, 4, 1, 1, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3
	};	
	
	public static final String names[] = new String[]
	{
		"AF1", "AF2", "AF3",
		"Aciaria 2",
		"Aciaria 1",
		"Convertedor Ac2",
		"Convertedor Ac1",
		"Forno Panela 1",
		"Forno Panela 2",
		"CAS-OB",
		"RH 2",
		"RH 3",
		"Maquina 1",
		"Maquina 2",
		"Maquina 3",
		"Maquina 4",		
	};
	
	public static Equip createEquip(int index)
	{
		switch(index)
		{
		case 0:
			return Equip.AF1; 
		case 1:
			return Equip.AF2; 
		case 2:
			return Equip.AF3; 
		case 3:
			return Equip.ACIARIA_2; 
		case 4:
			return Equip.ACIARIA_1; 
		case 5:
			return Equip.CONV_AC2;
		case 6:
			return Equip.CONV_AC1; 
		case 7:
			return Equip.FP1;
		case 8:
			return Equip.FP2; 
		case 9:
			return Equip.CAS;
		case 10:
			return Equip.RH2; 
		case 11:
			return Equip.RH3; 
		case 12:
			return Equip.MLC1; 
		case 13:
			return Equip.MLC2; 
		case 14:
			return Equip.MLC3; 
		case 15:
			return Equip.MLC4;		
		}
		return null;
	}
	
	public static String getEquipName(Equip equip)
	{
		return names[ equip.ordinal() ];
	}
	
	public static int getEquipGroup(Equip equip)
	{
		return groups[equip.ordinal()];
	}
}
