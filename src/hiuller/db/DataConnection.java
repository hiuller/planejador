package hiuller.db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.sqlite.SQLiteConfig;

/**
 * Esta classe é um wrapper para os servicos de banco de dados do SQLite e deve ser o mais genérica possivel
 * para ser reaproveitada futuramente. 
 * @author Hiuller Usiminas
 *
 */
public class DataConnection
{

	// instance members
	private Connection connection;
	private Statement  statement;
	private String sql;
	
	public DataConnection(String dbPath, String sql, boolean readOnly)
	{
		this.sql = sql;
		
		try
		{
			connection = createMyConnection(dbPath, readOnly);
			statement = connection.createStatement();
			
		} catch (Exception e) { manageException(e); }
	}

	
	// public methods
	public ResultSet getData() throws SQLException
	{
		return statement.executeQuery(sql);
	}
	
	public ResultSet getResultSet(String sql) throws SQLException
	{
		return statement.executeQuery(sql);
	}
	
	public void execute(String sqlUpdate) throws SQLException
	{
		statement.executeUpdate(sqlUpdate);
	}
	
	public Object[] getTableList() throws SQLException
	{
		if(connection == null)			
			return null;
		
		ArrayList<String> list = new ArrayList<String>();
		DatabaseMetaData meta = connection.getMetaData();
		ResultSet tables = meta.getTables(null, null, "%", new String[]{"TABLE"});
		while(tables.next())
			list.add(tables.getString("table_name"));
		return list.toArray();
	}
	
	public String[] getViewList() throws SQLException
	{
		if(connection == null)			
			return null;
		
		ArrayList<String> list = new ArrayList<String>();
		DatabaseMetaData meta = connection.getMetaData();
		ResultSet views = meta.getTables(null, null, "%", new String[]{"VIEW"});
		while(views.next())
			list.add(views.getString("table_name"));
		
		String[] result = new String[list.size()];		
		for(int i=0; i<list.size(); i++)
			result[i] = list.get(i);
			
		return result;		
	}
	
	public void close()
	{
		// close all open connections seems to be not needed
	}


	public static Connection createMyConnection(String filePath, boolean readOnly) throws ClassNotFoundException, SQLException
	{
		// always use read only in this class
		SQLiteConfig config = new SQLiteConfig();
		config.setReadOnly(readOnly);
		
		Connection result = null;
		
		Class.forName("org.sqlite.JDBC");
		result = DriverManager.getConnection(String.format("jdbc:sqlite:%s", filePath), config.toProperties());
			
		return result;
	}
	
	public static String sqlFromFile(String filePath)
	{

		InputStream stream = null;
		
		try {
			
			stream = DataConnection.class.getClassLoader().getResourceAsStream(filePath);
			
		} catch (Exception e) { manageException(e); }
		
		if(stream == null)
		{
			System.out.printf("Arquivo %s não encontrado.\n", filePath);
			System.exit(-1);
		}
		
		StringBuilder result = new StringBuilder();
		BufferedReader bf = new BufferedReader(new InputStreamReader(stream));
	  
		try
		{
			
			for(String line = bf.readLine(); line != null; line = bf.readLine()) 
				    result.append(line + '\n');
				  
			bf.close();
			
		} catch (IOException e) { manageException(e); }
		
		return result.toString();
	}


	private static void manageException(Exception e)
	{
		
	}

}
