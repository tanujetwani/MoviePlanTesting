package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
	

		static final String url="jdbc:mysql://localhost:3306/movieplan";
		static final String username="root";
		static final String password="root";
		static final String classpath="com.mysql.cj.jdbc.Driver";
			public static Connection getConnection() throws SQLException, ClassNotFoundException {
				Class.forName(classpath);
				Connection con=DriverManager.getConnection(url,username,password);
				return con;
			}


	}



