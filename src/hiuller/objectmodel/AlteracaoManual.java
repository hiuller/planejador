package hiuller.objectmodel;


public class AlteracaoManual
{
	private int 
		parametro,
		dia,
		row, 
		col;
	private double valor;
	// private String nota;
	
	public AlteracaoManual(int parametro, int dia, double valor)
	{
		this.parametro = parametro;
		this.dia = dia;
		this.row = dia-1;
		this.valor = valor;
	}

	public int getParametro()
	{
		return parametro;
	}

	public int getDia()
	{
		return dia;
	}

	public int getRow()
	{
		return row;
	}

	public int getCol()
	{
		return col;
	}

	public double getValor()
	{
		return valor;
	}
	
	public String getInsertSQL()
	{
/*
    parametro integer,
	dia integer,
	valor real		
 */
		String fieldList = "parametro, dia, valor";
		return String.format("insert into alteracs (%s) values (%d, %d, %s)",
				fieldList, parametro, dia, String.format("%.3f", valor).replace(',', '.'));
	}
}
