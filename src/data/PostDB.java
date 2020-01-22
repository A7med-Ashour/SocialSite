package data;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import help.DBUtil;
import help.DBUtil.DBAccess;
import model.Post;

public class PostDB {

	public static boolean insertByID(int ownerID, String content) {
		
		String query = "INSERT INTO post (ownerID,content) VALUES ( " + ownerID + "  ,  \"" + content + "\" ) ";
		DBAccess access = DBUtil.getDBAccess(query, DBUtil.QueryType.MODIFY);
		DBUtil.freeDBAccess(access);
		
		return (access.getCount() > 0);
	}

	public static List<Post> getPrivateByID(int ownerID) {
		
		String query = "SELECT * FROM post WHERE ownerID = " + ownerID + " ORDER BY created_date DESC";
		DBAccess access = DBUtil.getDBAccess(query, DBUtil.QueryType.SELECT);
		ResultSet rs = null;
		Post post = null;
		List<Post> results = new ArrayList<>();
		
		try {
			rs = access.getResultSet();
			if(rs != null) {
				while(rs.next()) {
					post = new Post();
					post.setID(rs.getInt("ID"));
					post.setOwnerID(rs.getInt("ownerID"));
					post.setContent(rs.getString("content"));
					post.setCreated_data(rs.getDate("created_date"));
					results.add(post);
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally{
			DBUtil.freeDBAccess(access);
		}
		
		
		return results;
	}

	public static List<Post> getWallPostsByID(int id) {
		String query = "SELECT * , name FROM post JOIN user ON ownerID = user.ID WHERE ownerID =  " + id + " OR ownerID" 
								+ " IN (SELECT senderID As friendID FROM friendRequest WHERE state = 'ACCEPTED' AND receiverID = " + id
								+ " UNION SELECT receiverID FROM friendRequest WHERE state = 'ACCEPTED' AND senderID =  " + id + " )"
								+ " ORDER BY created_date DESC";
		DBAccess access = DBUtil.getDBAccess(query, DBUtil.QueryType.SELECT);
		ResultSet rs = null;
		Post post = null;
		List<Post> results = new ArrayList<>();
		
		try {
			rs = access.getResultSet();
			if(rs != null) {
				while(rs.next()) {
					post = new Post();
					post.setID(rs.getInt("ID"));
					post.setOwnerID(rs.getInt("ownerID"));
					post.setOwnerName(rs.getString("name"));
					post.setContent(rs.getString("content"));
					post.setCreated_data(rs.getDate("created_date"));
					results.add(post);
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally{
			DBUtil.freeDBAccess(access);
		}
		
		
		return results;
	}

	
}
