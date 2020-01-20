package data;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
		StringBuilder query = new StringBuilder("INSERT INTO user (email,password,salt,name,phone,isMale,dateOfBirth,join_Date) values (");
		
		query.append("'"  + user.getEmail() + "'");
		query.append(",'" + SecurityUtil.hashPassword(user.getPassword() + salt) + "'");
		query.append(",'" + salt + "'");
		query.append(",'" + user.getName() + "'");
		query.append(",'" + user.getPhone() + "'");
		query.append(","  + user.isMale());
		query.append(","  + user.getDOB());
		query.append(",current_date());");
			
		DBAccess access = DBUtil.getDBAccess(query.toString(), DBUtil.QueryType.MODIFY);
		int ret = access.getCount();
		DBUtil.freeDBAccess(access);
		
		/* INSERT VERIFICATION CODE */
		query.setLength(0); /* CLEAR STRING BUILDER */
		query.append("INSERT INTO verification (userEmail,code,created_date) VALUES ("); 
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

	public static void insertResetPasswordToken(String email, String token) {
		
		StringBuilder query = new StringBuilder("INSERT INTO resetPassword (user_email,token,expire_date) VALUES (");
		query.append("'" + email + "'");
		query.append(",'" + token + "'");
		query.append(",NOW());");
		
		DBAccess access = DBUtil.getDBAccess(query.toString(), DBUtil.QueryType.MODIFY);
		DBUtil.freeDBAccess(access);
	}

	public static String getResetPasswordToken(String email) {
		
		String token = null,
				   query = "SELECT token, EXTRACT(HOUR FROM TIMEDIFF(NOW(),expire_date)) AS hours FROM resetPassword WHERE user_email = '" + email + "'";
		
		DBAccess access = DBUtil.getDBAccess(query, DBUtil.QueryType.SELECT);
		ResultSet rs = access.getResultSet();
		
		try {
			if(rs != null && rs.next()) {
				token = rs.getString("token");
				int hours = rs.getInt("hours");
				if(hours > 0) { /* TOKEN IS NOW EXPIRED */
					deleteResetPasswordToken(token);
					token = null;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DBUtil.freeDBAccess(access);
		}
		
		return token;
	}

	public static String getEmailByToken(String token) {
		
		String email = null,
				   query = "SELECT user_email FROM resetPassword WHERE token = '" + token + "'";
		
		DBAccess access = DBUtil.getDBAccess(query, DBUtil.QueryType.SELECT);
		ResultSet rs = access.getResultSet();
		
		try {
			if(rs != null && rs.next()) {
				email = rs.getString("user_email");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DBUtil.freeDBAccess(access);
		}
		
		return email;
	}

	public static int updatePassword(String email, String password) {
		
		String salt = SecurityUtil.getSalt();
		int count = 0;
		StringBuilder query = new StringBuilder("UPDATE user SET salt = ");
		
		query.append("'" + salt + "'");
		query.append(",password = '" + SecurityUtil.hashPassword(password + salt) + "'");
		query.append("WHERE email = '" + email + "'");
		
		DBAccess access = DBUtil.getDBAccess(query.toString(), DBUtil.QueryType.MODIFY);
		count = access.getCount();
		DBUtil.freeDBAccess(access);
		
		return count;
	}

	public static void deleteResetPasswordToken(String token) {
		
		String query = "DELETE FROM resetPassword WHERE token = '" + token + "'";
		DBAccess access = DBUtil.getDBAccess(query, DBUtil.QueryType.MODIFY);
		DBUtil.freeDBAccess(access);
		
	}
	
	public static List<User> getFriendsByID(int ID){
		
		List<User> friends = new ArrayList<>();
		
		
		String query =  "SELECT  ID , name ,email , phone FROM user JOIN friendRequest ON (user.ID = senderID OR user.ID = receiverID)"
								+ "WHERE user.ID != " +  ID + " AND (senderID = " + ID + " OR receiverID =" + ID + " ) AND state = 'ACCEPTED' ";
		/*
		String query = "SELECT  ID ,name, email , phone FROM user WHERE ID IN ("
				+ "SELECT  senderID As ID FROM friendRequest       Where state = 'ACCEPTED' AND receiverID = " + ID
				+ "UNION SELECT  receiverID FROM friendRequest Where state = 'ACCEPTED' AND senderID = " + ID
				+ ")";
		*/
		
		DBAccess access = DBUtil.getDBAccess(query,DBUtil.QueryType.SELECT );
		ResultSet rs = access.getResultSet();
		try {
			if(rs != null) {
					while(rs.next()) {
						User user = User.builder()
								.ID(rs.getInt("ID"))
								.email(rs.getString("email"))
								.name(rs.getString("name"))
								.password("**********")
								.phone(rs.getString("phone"))
								.build();
						friends.add(user);
					}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DBUtil.freeDBAccess(access);
		}
		
		return friends;
		
	}

	public static void getSearchResult(int ID, String value, List<User> results, Map<Integer, String> states) {
		
		String query = "SELECT user.ID , user.name ,  email ,CASE WHEN state IS NULL THEN 'NOT FRIEND' Else state END AS State FROM user"
				 + " left join "
				 + "("
				 + "SELECT u2.ID ,  CASE WHEN state = 'PENDING' AND receiverID = " + ID + " THEN 'REPLY' ELSE state END AS state"
				 + " FROM user as u2 join friendRequest ON (senderID = u2.ID  OR receiverID = u2.ID) Where (senderID = " + ID + "  OR receiverID = " + ID + ") AND u2.ID != "  + ID
				 + ") As requests ON user.ID = requests.ID where user.ID != " + ID + " AND (name LIKE '%" + value + "%' OR email LIKE '%"+ value + "%') ";
		DBAccess access = DBUtil.getDBAccess(query, DBUtil.QueryType.SELECT);
		ResultSet rs = access.getResultSet();
		User user = null;
		try {
			if(rs != null) {
					while(rs.next()) {
						user = User.builder()
								.ID(rs.getInt("ID"))
								.email(rs.getString("email"))
								.name(rs.getString("name"))
								.password("**********")
								.build();
						results.add(user);
						
						states.put(user.getID(),rs.getString("state"));
					}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DBUtil.freeDBAccess(access);
		}
	}

	public static void getRecievedRequests(int ID, List<User> results) {
		
		String query = "SELECT  ID , name , email FROM user JOIN friendRequest ON (senderID = user.ID) WHERE receiverID = "
				+ ID + " AND state = 'PENDING' ";
		
		DBAccess access = DBUtil.getDBAccess(query, DBUtil.QueryType.SELECT);
		ResultSet rs = access.getResultSet();
		User user = null;
		try {
			if(rs != null) {
					while(rs.next()) {
						user = User.builder()
								.ID(rs.getInt("ID"))
								.email(rs.getString("email"))
								.name(rs.getString("name"))
								.password("**********")
								.build();
						results.add(user);
					}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DBUtil.freeDBAccess(access);
		}
	}

	public static void getSentRequests(int ID, List<User> results) {
		
		String query = "SELECT  ID , name , email FROM user JOIN friendRequest ON (receiverID = user.ID) "
				  + "WHERE senderID = " + ID + " AND state = 'PENDING' ";

			DBAccess access = DBUtil.getDBAccess(query, DBUtil.QueryType.SELECT);
			ResultSet rs = access.getResultSet();
			User user = null;
			try {
				if(rs != null) {
					while(rs.next()) {
						user = User.builder()
								.ID(rs.getInt("ID"))
								.email(rs.getString("email"))
								.name(rs.getString("name"))
								.password("**********")
								.build();
						results.add(user);
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}finally {
				DBUtil.freeDBAccess(access);
			}
					
				}
	
}
