package help;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil {
	
	public static final String URL = "jdbc:mysql://localhost:3306/socialSite";
	public static final String USERNAME = "root";
	public static final String PASSWORD = "12345";
	public static final String JDBCDRIVER = "com.mysql.cj.jdbc.Driver";
	public static enum  QueryType {SELECT , MODIFY};
	
	
	public static DBAccess getDBAccess(String query,QueryType type) {
		Connection con = null;
		Statement stm = null;
		ResultSet rs = null;
		int count = 0;
		
		try {
			Class.forName(DBUtil.JDBCDRIVER);
			con = DriverManager.getConnection(DBUtil.URL,DBUtil.USERNAME,DBUtil.PASSWORD);
			stm = con.createStatement();
			if(type == QueryType.SELECT) {
				rs =  stm.executeQuery(query);
			}else {
				count = stm.executeUpdate(query);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return new DBAccess(con,stm,rs,count);
	}
	
	public static void freeDBAccess(DBAccess access) {
		
		try {
			if(access.getResultSet() != null) {access.getResultSet().close();}
			if(access.getStatement() != null) {access.getStatement().close();}
			if(access.getConnection() != null) {access.getConnection().close();} 
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static class DBAccess{
		
		private Connection connection;
		private Statement statement;
		private ResultSet resultSet;
		private int count;
		
		private DBAccess(Connection connection,Statement statement,ResultSet resultSet,int count) {
			this.connection = connection;
			this.statement 	= statement;
			this.resultSet 	= resultSet;
			this.count 		= count;
		}

		public Connection getConnection() {
			return connection;
		}

		public Statement getStatement() {
			return statement;
		}

		public ResultSet getResultSet() {
			return resultSet;
		}
		
		public int getCount() {
			return count;
		}
		
		
	}
}
