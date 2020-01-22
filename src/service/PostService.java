package service;

import data.PostDB;

public class PostService {

	public static boolean createPost(int ownerID, String content) {
		
		return PostDB.insertByID(ownerID,content);
	}
}
