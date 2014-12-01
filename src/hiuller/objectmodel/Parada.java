package hiuller.objectmodel;

import hiuller.gui.Constants;
import hiuller.objectmodel.Equipamentos.Equip;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Parada
{
	private Equip equipamento;
	private int dia;
	private int hora;
	private int minuto;
	private int duracao;
	private String nota;
	private boolean isFixed;
	
	private Calendar start, end;
	private SimpleDateFormat format = new SimpleDateFormat("'dia' dd (HH:mm)");
	private int month, year;
	
	private int colorIndex, binNumber; // for display purposes
	
	
	public Parada(Equip equipamento, int dia, int hora, int minuto, int duracao, String nota, boolean isFixed)
	{
		if(Constants.MONTH == null)
		{
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.DAY_OF_MONTH, 1);
			
			Constants.setDate( cal.getTime() );
		}
		
		this.equipamento = equipamento;
		this.dia = dia;
		this.hora = hora;
		this.minuto = minuto;
		this.duracao = duracao;
		this.nota = nota;
		this.isFixed = isFixed;
		this.binNumber = -1; // not draw 
		
		start = Calendar.getInstance();
		end   = Calendar.getInstance();
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(Constants.MONTH);		
			
		this.month = cal.get(Calendar.MONTH)+1; // january = 0		
		this.year  = cal.get(Calendar.YEAR);
		
		try
		{
			start.setTime( new SimpleDateFormat("dd/MM/yyyy hh:mm").parse(String.format("%02d/%02d/%d %02d:%02d", dia, this.month, this.year, hora, minuto)) );			
		} catch (ParseException e)
		{			
			e.printStackTrace();
			System.exit(-1);
		}
		
		end.setTime( start.getTime() );
		end.add(Calendar.HOUR, this.duracao);
	}
	
	public String toString()
	{
		String name = Equipamentos.getEquipName(equipamento);		
		return String.format("Parada do %s: dia=%d hora=%02d:%02d duracao=%d h", name, dia, hora, minuto, duracao);
	}
	
	// smallest dia inside parada
	public int getMinDay()
	{
		return dia;
	}
	// retorna o dia onde termina a parada (pode passar de 31!)
	public int getMaxDay()
	{
		return end.get(Calendar.DAY_OF_MONTH);
	}	
	// retorna o intervalo percentual da parada num determinado dia
	public double[] getRangeDay(int dia)
	{
		double start = 0.0;		
		double end = 0.0;
		double dayDuration = 24.0*60.0*60.0*1000.0; // number of milliseconds in a day
		
		SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		Calendar day = Calendar.getInstance();
		
		try
		{
			
			day.setTime( f.parse(String.format("%02d/%02d/%d %02d:%02d", dia,  this.month, this.year, 0, 0)) );
		
		} catch (ParseException e)
		{			
			e.printStackTrace();
			System.exit(-1);
		}
		Calendar nextDay = (Calendar) day.clone();
		nextDay.add(Calendar.HOUR, 24);
		

		double distanceFromStart = this.start.getTimeInMillis() - day.getTimeInMillis();
		double distanceToEnd     = this.end.getTimeInMillis() - nextDay.getTimeInMillis();
		if(distanceFromStart < dayDuration)
		{
			if(distanceFromStart > 0)
				start = distanceFromStart / dayDuration;
				
			if(distanceToEnd > 0)
				end = 1.0;
			else
				if(-distanceToEnd < dayDuration)
					end = (dayDuration + distanceToEnd)/dayDuration;
		}
		
		return new double[]{start, end};
	}
	
	
	// getters and setters
	
	public boolean occursInDay(int day)
	{
		double[] range = getRangeDay(day);
		
		if(range[0] == 0.0 && range[1] == 0.0)		
			return false;
		else
			return true;
	}		
	
	public int getColorIndex()
	{
		return colorIndex;
	}

	public void setColorIndex(int colorIndex)
	{
		this.colorIndex = colorIndex;
	}
		

	public int getBinNumber()
	{
		return binNumber;
	}

	public void setBinNumber(int binNumber)
	{
		this.binNumber = binNumber;
	}

	public int getGroup()
	{
		return Equipamentos.getEquipGroup(equipamento);
	}
	
	public Equip getEquipamento()
	{
		return this.equipamento;
	}
	
	public String getEquipamentoName()
	{
		return Equipamentos.getEquipName(this.equipamento);
	}
	
	public int getEquipamentoNumber()
	{
		return equipamento.ordinal();
	}
	
	public String getInicioString()
	{
		return format.format(start.getTime());
	}

	public String getFinalString()
	{
		return format.format(end.getTime());
	}
	
	public String getFixaString()
	{
		return isFixed ? "Sim" : "Não";
	}

	public int getDia()
	{
		return dia;
	}

	public int getHora()
	{
		return hora;
	}

	public int getMinuto()
	{
		return minuto;
	}

	public int getDuracao()
	{
		return duracao;
	}
	
	public String getNota()
	{
		return nota;
	}

	public boolean isFixed()
	{
		return isFixed;
	}

	public String getInsertSQL()
	{
/*
		equip integer,
		dia integer,
		hora integer,
		minuto integer,
		month integer,
		year integer,
		duracao integer,
		nota text,
		isFixed integer,
		colorIndex integer,
		binNumber integer
*/		
		String fieldList = "equip, dia, hora, minuto, month, year, duracao, nota, isFixed, colorIndex, binNumber";
		return String.format("insert into paradas (%s) values (%d, %d, %d, %d, %d, %d, %d, '%s', %d, %d, %d)", 
				fieldList, equipamento.ordinal(), dia, hora, minuto, month, year, duracao, nota, isFixed ? 1:0, colorIndex, binNumber);
	}
}
