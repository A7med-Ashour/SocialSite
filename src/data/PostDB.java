package data;

import help.DBUtil;
import help.DBUtil.DBAccess;

public class PostDB {

	public static boolean insertByID(int ownerID, String content) {
		
		String query = "INSERT INTO post (ownerID,content) VALUES ( " + ownerID + "  ,  \"" + content + "\" ) ";
		DBAccess access = DBUtil.getDBAccess(query, DBUtil.QueryType.MODIFY);
		
		return (access.getCount() > 0);
	}

	
}
