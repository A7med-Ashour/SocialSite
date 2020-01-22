package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
				default :
					/* Do Nothing */
					break;
			}
			
			URL = (URL == null) ?  request.getHeader("referer").replaceAll(".*social", "") : URL;
			getServletContext().getRequestDispatcher(URL).forward(request,response);
		
	}


	private String createHandler(int userID, HttpServletRequest request, HttpServletResponse response) {
		
		String URL = ( PostService.createPost(userID, request.getParameter("content")) ) ? "/profile.jsp" : null ;
		
		return URL;
	}

}
