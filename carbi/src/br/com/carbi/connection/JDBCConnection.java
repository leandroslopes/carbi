
package br.com.carbi.connection;

import br.com.carbi.exception.ConnectionException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCConnection {

	private Connection connection = null;

	private String userName = null;
	private String password = null;
	private String url = null;
	private String jdbcDriver = null;
	private String dataBaseName = null;
	private String dataBasePrefix = null;

	public JDBCConnection() {
		super();

		userName = "sa";
		password = "";    
		jdbcDriver = "org.h2.Driver";
		dataBaseName = "carbi";
		dataBasePrefix = "jdbc:h2:~/";

		url = dataBasePrefix + dataBaseName;
	}

	public Connection getConnection() throws ConnectionException {
		try {
			if (connection == null) {
				Class.forName(jdbcDriver);
				connection = DriverManager.getConnection(url, userName, password);
			} else if (connection.isClosed()) {
				connection = null;
				return getConnection();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new ConnectionException("Classe JDBC nao encontrada: " + e.getMessage());
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ConnectionException("Erro ao acessar o banco de dados ou url nula: " + e.getMessage());
		}
		return connection;
	}

	public void closeConnection() throws ConnectionException {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new ConnectionException("Erro ao acessar o banco de dados: " + e.getMessage());
			}
		}
	}
}
