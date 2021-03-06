package com.example.bot.spring;

import lombok.extern.slf4j.Slf4j;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.*;
import java.net.URISyntaxException;
import java.net.URI;

@Slf4j
public class SQLDatabaseEngine extends DatabaseEngine {
	@Override
	String search(String text) throws Exception {
		//Write your code here
		String result = null;
		try { 
			Connection connection = this.getConnection(); 
			PreparedStatement stmt = connection.prepareStatement( "SELECT * FROM chatbottable WHERE keyword like concat('%', ?, '%');"); 
			stmt.setString(1, text); //or some other variables
			ResultSet rs = stmt.executeQuery(); 
			while (rs.next()) { 
				result = rs.getString(2);
			} 
			rs.close(); 
			stmt.close(); 
			connection.close(); 
		} catch (Exception e) { 
			System.out.println(e); 
		}
		if (result != null)
			return result;
		throw new Exception("NOT FOUND");
	}
	
	
	private Connection getConnection() throws URISyntaxException, SQLException {
		 Connection connection;
		URI dbUri = new URI(System.getenv("DATABASE_URL"));

		String username = dbUri.getUserInfo().split(":")[0];
		String password = dbUri.getUserInfo().split(":")[1];
		String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() +  "?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";

		log.info("Username: {} Password: {}", username, password);
		log.info ("dbUrl: {}", dbUrl);
		
		//local test
		// String username = "programmer"; 
		// String password = "iamaprogrammer"; 
		// String dbUrl = "jdbc:postgresql://localhost:5432/chatbotDB"; 
		
		connection = DriverManager.getConnection(dbUrl, username, password);

		return connection;
	}

}
