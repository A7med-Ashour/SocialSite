package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Post;
import model.User;
import service.PostService;

public class PostController extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doPost(request,response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		String action = request.getParameter("action"),
				URL = null;
		int userID;
		final String sessionID = request.getSession().getId().intern();
		synchronized(sessionID) {
			userID = ((User)request.getSession().getAttribute("user")).getID();
		}
		
	
			switch(action) {
				case "create" : 
					URL = createHandler(userID,request,response);
					break;
				case "refresh" :
					URL = refreshHandler(userID,request,response);
				default :
					/* Do Nothing */
					break;
			}
			
			URL = (URL == null) ?  "/social/home.jsp" : URL;
			response.sendRedirect(URL);;
		
	}


	private String refreshHandler(int userID, HttpServletRequest request, HttpServletResponse response) {
		
		List<Post> wallPosts = PostService.getWallPostsByID(userID);
		final String sessionID = request.getSession().getId().intern();
		synchronized(sessionID) {
			request.getSession().setAttribute("wallPosts", wallPosts);
		}
		
		return  "/social/home.jsp";
	}


	private String createHandler(int userID, HttpServletRequest request, HttpServletResponse response) {
		
		String URL = ( PostService.createPost(userID, request.getParameter("content")) ) ? "/social/profile.jsp" : null ;
		
		User user = null;
		final String sessionID = request.getSession().getId().intern();
		synchronized(sessionID) {
			user = (User)request.getSession().getAttribute("user");
		}
		
		user.setPosts(PostService.getPriavtePostsByID(userID));
		
		
		return URL;
	}

}
