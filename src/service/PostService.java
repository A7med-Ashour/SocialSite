package service;

import java.util.List;

import data.PostDB;
import model.Post;

public class PostService {

	public static boolean createPost(int ownerID, String content) {
		
		return PostDB.insertByID(ownerID,content);
	}
	
	public static List<Post> getPriavtePostsByID(int ownerID){
		
		return PostDB.getPrivateByID(ownerID);
	}

	public static List<Post> getWallPostsByID(int id) {
		
		return PostDB.getWallPostsByID(id);
	}
}
