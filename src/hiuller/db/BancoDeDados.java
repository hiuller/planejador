package hiuller.db;

import hiuller.gui.Constants;
import hiuller.gui.MainWindow;
import hiuller.gui.dialogs.EditorParametros;
import hiuller.objectmodel.AlteracaoManual;
import hiuller.objectmodel.Equipamentos;
import hiuller.objectmodel.Equipamentos.Equip;
import hiuller.objectmodel.Parada;
import hiuller.objectmodel.Plano;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import javax.swing.JOptionPane;

public class BancoDeDados
{

	private String location;
	
	public BancoDeDados(String location, boolean overwrite)
	{
		this.setLocation(location);

		if(overwrite)
		{
			File file = new File(location);
			if(file.exists())
				file.delete();
		}
		
	}
// servicos de escrita	
	public void savePlano(Plano plano)
	{
		boolean readOnly = false;
		DataConnection connection = new DataConnection(location, null, readOnly);
		
		// create a table to store parameters, paradas
		String create_params_table  = DataConnection.sqlFromFile("create_params_table.sql");
		String create_paradas_table = DataConnection.sqlFromFile("create_paradas_table.sql");
		String create_alteracs_table = DataConnection.sqlFromFile("create_alteracs_table.sql");
		String create_meta_table = DataConnection.sqlFromFile("create_meta_table.sql");
		String create_plano_table = DataConnection.sqlFromFile("create_plano_table.sql");
		String create_demanda_table = DataConnection.sqlFromFile("create_demanda_table.sql");
		
		try
		{
//			connection.execute("drop table if exists params");
//			connection.execute("drop table if exists paradas");
//			connection.execute("drop table if exists alteracs");
//			connection.execute("drop table if exists meta_data");
//			connection.execute("drop table if exists plano");
//			connection.execute("drop table if exists demanda");
			
			connection.execute(create_params_table);
			connection.execute(create_paradas_table);
			connection.execute(create_alteracs_table);
			connection.execute(create_meta_table);
			connection.execute(create_plano_table);
			connection.execute(create_demanda_table);
			
			for(int i=0; i<EditorParametros.values.length; i++)
			{
				String insert_param = String.format(
						"insert into params (idt, value) values (%d, %s)", 
						i, String.format("%.6f", EditorParametros.values[i]).replace(',', '.'));
				
				connection.execute(insert_param);
			}
			
			for(Parada p : plano.getParadas())
			{
				connection.execute( p.getInsertSQL() );
			}
			
			for(AlteracaoManual am : plano.getAlteracoesManuais())
			{
				connection.execute( am.getInsertSQL() );
			}
			
			if(plano.hasData())
			{
				Iterator<String> iterator = plano.getPlanoSQLInserts();
				while( iterator.hasNext() )
				{
					String insert_plano = iterator.next();
					connection.execute( insert_plano );
				}
			}
			
			if(true)
			{
				Iterator<String> iterator = plano.getInserirDemanda().getSQLInserts();
				while( iterator.hasNext() )
				{
					String insert_plano = iterator.next();
					connection.execute( insert_plano );
				}
			}
			
			String month = new SimpleDateFormat("yyyy-MM-dd").format(Constants.MONTH);
			connection.execute(String.format("insert into meta_data (version, month) values (%d, '%s')", 
					MainWindow.VERSION, month));
			
		} catch (SQLException e)
		{			
			e.printStackTrace();
			System.exit(-1);
		}
		
	}
// servicos de leitura	
	public void openPersistenceFile()
	{
		
	}
	public Plano readPlano()
	{
		boolean readOnly = false;
		DataConnection connection = new DataConnection(location, null, readOnly);
		
		String month = null;
		int version = -1;
		
		ResultSet rs = null;
		try 
		{
			rs = connection.getResultSet("select version, month from meta_data");
			while( rs.next() )
			{
				version = rs.getInt(1);
				month = rs.getString(2);
			}
			
		} catch(SQLException e) 
		{
			e.printStackTrace();
			System.exit(-1);
		}
		
		if(version != MainWindow.VERSION)
		{
			String title = "Versão incompatível";
			String message = "A versão do arquivo *.PLA não é mais suportada.";
			JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
			return null;
		}
		
		Date mes = null;
		try 
		{
			mes = new SimpleDateFormat("yyyy-MM-dd").parse(month);
			
		} catch (ParseException e)
		{			
			e.printStackTrace();
			System.exit(-1);
		}

		Constants.setDate(mes);
		Plano plano = new Plano(mes);
		
// processamento interno do arquivo de dados
	// paradas
		String sqlParadas = "select equip, dia, hora, minuto, duracao, isFixed, nota from paradas";
		ResultSet paradasSet = null;
		try {
			
			paradasSet = connection.getResultSet(sqlParadas);
			while ( paradasSet.next() )
			{
				Equip equip = Equipamentos.createEquip(paradasSet.getInt(1));
				int dia  = paradasSet.getInt(2);
				int hora = paradasSet.getInt(3);
				int min  = paradasSet.getInt(4);
				int dura = paradasSet.getInt(5);
				boolean isFixed = paradasSet.getInt(6) == 1;
				String nota = paradasSet.getString(7);
				
				Parada temp = new Parada(equip, dia, hora, min, dura, nota, isFixed);
				plano.inserirParada(temp);
			}			
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	// carregar parâmetros
		String sqlParametros = "select idt, value from params";
		ResultSet parametrosSet = null;
		try
		{
			parametrosSet = connection.getResultSet(sqlParametros);
			//double[] valores = new double[EditorParametros.NUM_PARAMETROS];
			while ( paradasSet.next() )
			{
				EditorParametros.values[parametrosSet.getInt(1)] = parametrosSet.getDouble(2);
			}
			
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(-1);			
		}
	// alterações manuais
		String sqlAlteracoes = "select parametro, dia, valor from alteracs";
		ResultSet alteracoesSet = null;
		try{		
			alteracoesSet = connection.getResultSet(sqlAlteracoes);
			while( alteracoesSet.next() )
			{
				int parametro = alteracoesSet.getInt(1);
				int dia       = alteracoesSet.getInt(2);
				double valor  = alteracoesSet.getDouble(3);
				
				AlteracaoManual alteracao = new AlteracaoManual(parametro, dia, valor);
				plano.inserirAlteracaoManual(alteracao);
			}
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(-1);			
		}
	// dados previamente calculados
		String sqlPlano = String.format("select %s from plano", Plano.FIELD_LIST);
		ResultSet planoSet = null;
		try {
			planoSet = connection.getResultSet(sqlPlano);
			while( planoSet.next() )
			{
				int    dia      = planoSet.getInt(1);
				double[] values = new double[ Plano.FIELD_LIST_SIZE-1 ]; // (-1) excluding dia;
				
				for(int i=0; i<Plano.FIELD_LIST_SIZE-1; i++)
					values[i] = planoSet.getDouble(i+2);
				
				plano.setCalculatedValues(dia, values);
			}
			plano.setHasData(true);
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(-1);			
		}
	// demanda
		String sqlDemanda = "select aco, decendio1, decendio2, decendio3 from demanda";
		ResultSet demandaSet = null;
		try {
			demandaSet = connection.getResultSet(sqlDemanda);
			while( demandaSet.next() )
			{
				int aco  = demandaSet.getInt(1);
				int dec1 = demandaSet.getInt(2);
				int dec2 = demandaSet.getInt(3);
				int dec3 = demandaSet.getInt(4);
				plano.insertDemanda(aco, new int[]{dec1, dec2, dec3});
			}
			
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(-1);			
		}
		
// fim do processamento interno do arquivo de dados
		
		return plano;
	}
	
	public String getLocation()
	{
		return location;
	}
	public void setLocation(String location)
	{
		this.location = location;
	}
	
}
