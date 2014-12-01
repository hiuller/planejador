package hiuller.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.util.Date;

public class Constants
{
	public static final String
		APP_TITLE				= "Planejador Aciaria v0.1";
	
	public static final int
		TABLE_ROW_HEIGHT        = 25,
		PARADAS_LIST_WIDTH		= 700,
		PARADAS_LIST_HEIGHT     = 500,
		
		MAX_NUMBER_EVOLUTIONS   = 100,
		POPULATION_SIZE         = 40,
		
		TAM_SERIE_HIC           = 4,
		MAX_SERIE_HIC           = 1,
		MAX_CORR_DR             = 19,
		MAX_CORR_RH             = 36,
		MAX_CORR_FP             = 10;
		
	public static final double
		LIMITE_SALDO_BAIXO      = 1000.0,
		LIMITE_SALDO_ALTO       = 3000.0,
		MIN_SUCATA_AC1          = 10.0,
		MIN_SUCATA_AC2          = 12.0;
	
	public static final Dimension
		APP_STARTING_DIM        = new Dimension(900, 600);
	
	public static final Font
		TABLE_FONT              = new Font("Arial", Font.PLAIN, 14),
		TABLE_FONT_BOLD         = new Font("Arial", Font.BOLD, 14),
		TABLE_HEADER_FONT       = new Font("Arial", Font.BOLD, 15);
	
	public static Date MONTH;
	
	public static void setDate(Date month)
	{
		MONTH= month;
	}
	
	public static MainWindow mainWindowHandle;
}
