package data;


import java.sql.ResultSet;
import java.sql.SQLException;

import help.DBUtil;
import help.DBUtil.DBAccess;
import help.Helper;
import help.SecurityUtil;
import model.User;

public class UserDB {
	
	public static boolean isExist(String email) {
		
		return findByEmail(email) != null ;
	}
	
	public static boolean isExist(String email, String password) {
		
		User user = findByEmail(email);
		
		return  (user != null) && (user.getPassword().equals(SecurityUtil.hashPassword(password + user.getSalt())));
	}

	public static User findByEmail(String email) {
		
		User user = null;
		//String query = "SELECT * FROM user WHERE email = '" + email + "'";
		String query = "SELECT * FROM user WHERE email = '" + email + "'";
		DBAccess access = DBUtil.getDBAccess(query, DBUtil.QueryType.SELECT);
		ResultSet rs = access.getResultSet();
		
		
		try {
			if(rs.next()) {
				
				user = User.builder().ID(rs.getInt("ID"))
									 .email(rs.getString("email"))
									 .password(rs.getString("password"))
									 .salt(rs.getString("salt"))
									 .name(rs.getString("name"))
									 .phone(rs.getString("phone")).ID(rs.getInt("ID"))
									 .male(rs.getBoolean("isMale"))
									 .dateOfBirth(rs.getDate("dateOfBirth"))
									 .build();
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DBUtil.freeDBAccess(access);
		}
		
		return user;
	}

	
	
	public static int insert(User user) {
		
		
		String salt = SecurityUtil.getSalt();
		String verificationCode = Helper.generateRandomString(5);
		
		/* INSERT USER */
		StringBuilder query = new StringBuilder("INSERT INTO user (email,password,salt,name,phone,isMale,dateOfBirth,joinedDate) values (");
		
		query.append("'"  + user.getEmail() + "'");
		query.append(",'" + SecurityUtil.hashPassword(user.getPassword() + salt) + "'");
		query.append(",'" + salt + "'");
		query.append(",'" + user.getName() + "'");
		query.append(",'" + user.getPhone() + "'");
		query.append(","  + user.isMale());
		query.append(","  + user.getDOB());
		query.append(",now());");
			
		DBAccess access = DBUtil.getDBAccess(query.toString(), DBUtil.QueryType.MODIFY);
		int ret = access.getCount();
		DBUtil.freeDBAccess(access);
		
		/* INSERT VERIFICATION CODE */
		query.setLength(0); /* CLEAR STRING BUILDER */
		query.append("INSERT INTO verification (userEmail,code,createdDate) VALUES ("); 
		query.append("'" + user.getEmail() + "'");
		query.append(",'" + verificationCode+ "'");
		query.append(",now())");
		
		access = DBUtil.getDBAccess(query.toString(), DBUtil.QueryType.MODIFY);
		DBUtil.freeDBAccess(access);
		
		return ret;
		
	}

	
	
	public static String getVerificationCode(String email) {
		String code = null;
		
		String query = "SELECT code FROM verification WHERE userEmail = '" + email + "'";
		DBAccess access = DBUtil.getDBAccess(query, DBUtil.QueryType.SELECT);
		ResultSet rs = access.getResultSet();
		
		try {
			if(rs.next()) {
				code = rs.getString("code"); 
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		DBUtil.freeDBAccess(access);
		
		return code;
	}
	
	public static void makeUserVerified(String email) {
		
		String query = "DELETE FROM verification WHERE userEmail = '" + email + "'";
		DBAccess access = DBUtil.getDBAccess(query, DBUtil.QueryType.MODIFY);
		DBUtil.freeDBAccess(access);
	}
	
	

}
