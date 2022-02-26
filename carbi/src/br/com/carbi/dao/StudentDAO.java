package br.com.carbi.dao;

import br.com.carbi.connection.JDBCConnection;
import br.com.carbi.exception.ConnectionException;
import br.com.carbi.model.Student;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;


public class StudentDAO {

	private Connection connection;
	private Statement statement;
	private BufferedReader bufferedReader;

	private ArrayList<String> COURSES = new ArrayList<String>(Arrays.asList("EDUCAÇÃO INFANTIL", "ENSINO FUNDAMENTAL", "ENSINO MÉDIO"));
	private ArrayList<String> FUNDAMENTAL_1 = new ArrayList<String>(Arrays.asList("1° ANO", "2° ANO", "3° ANO", "4° ANO", "5° ANO"));
	private ArrayList<String> FUNDAMENTAL_2 = new ArrayList<String>(Arrays.asList("6° ANO", "7° ANO", "8° ANO", "9° ANO"));

	public StudentDAO() throws ConnectionException {
		connection = new JDBCConnection().getConnection();
	}

	public void importStudent(File file) throws ConnectionException  {
		PreparedStatement preparedStatement = null;

		try {
			statement = connection.createStatement();

			String sqlDropTable = "DROP TABLE IF EXISTS carbi";
			statement.execute(sqlDropTable);

			String sqlCreateTable = "CREATE TABLE carbi ("
					+ "id INT PRIMARY KEY AUTO_INCREMENT, "
					+ "registration INT, "
					+ "name VARCHAR(255), "
					+ "course VARCHAR(100))";
			statement.execute(sqlCreateTable);

			String sqlInsertStudent = "INSERT INTO CARBI(name, registration, course) VALUES(?, ?, ?)";			

			bufferedReader = new BufferedReader(new FileReader(file));
			String line = "";

			while ((line = bufferedReader.readLine()) != null) {
				if (line != null) {
					String[] student = line.split(",");
					String course_aux = student[2].replaceAll("^\"|\"$", "");
					String course = "";

					if (COURSES.contains(course_aux)) { 
						preparedStatement = connection.prepareStatement(sqlInsertStudent);
						preparedStatement.setString(1, student[1].replaceAll("^\"|\"$", "")); //NAME
						preparedStatement.setInt(2, Integer.parseInt(student[0])); //REGISTRATION

						String series = student[3].replaceAll("^\"|\"$", ""); 
						if (FUNDAMENTAL_1.contains(series)) {
							course = "ENSINO FUNDAMENTAL 1";
						} else if (FUNDAMENTAL_2.contains(series)) {
							course = "ENSINO FUNDAMENTAL 2";
						} else {
							course = course_aux;
						}
						preparedStatement.setString(3, course); //COURSE
						preparedStatement.executeUpdate();
					}					
				} 
			}

			preparedStatement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ConnectionException("Erro ao importar o arquivo .csv: " + e.getMessage());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new ConnectionException("Arquivo .csv nao encontrado: " + e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			throw new ConnectionException("Erro ao ler arquivo .csv: " + e.getMessage());
		}
	}

	public ArrayList<Student> searchStudents(String name) throws ConnectionException {
		ArrayList<Student> students = new ArrayList<Student>();

		String sqlSelect = "SELECT * FROM carbi WHERE name LIKE ? ORDER BY course";

		PreparedStatement preparedStatement;

		try {
			preparedStatement = connection.prepareStatement(sqlSelect);

			preparedStatement.setString(1, name.toUpperCase() + "%");

			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				Student student = new Student();
				student.setRegistration(resultSet.getInt("registration"));
				student.setName(resultSet.getString("name"));
				student.setCourse(resultSet.getString("course"));

				students.add(student);
			}

			resultSet.close();
			preparedStatement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ConnectionException("Erro ao pesquisar alunos: " + e.getMessage());
		}

		return students;
	}
}
